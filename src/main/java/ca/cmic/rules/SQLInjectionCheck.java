package ca.cmic.rules;

/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.semantic.MethodMatchers;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.sonar.plugins.java.api.semantic.MethodMatchers.CONSTRUCTOR;

@Rule(key = "SQL-101")
public class SQLInjectionCheck extends IssuableSubscriptionVisitor{


    private static final String STRING_CLASS = "java.lang.String";
    private static final String EXECUTE_QUERY_MESSAGE = "SQL-101 : Make sure using a dynamically formatted SQL query(parameter/inline) is safe here.";
    private static final String JAVA_SQL_STATEMENT = "java.sql.Statement";
    private static final String JAVA_SQL_CONNECTION = "java.sql.Connection";
    private static final String SPRING_JDBC_OPERATIONS = "org.springframework.jdbc.core.JdbcOperations";
    /**
     * MethodMathcers to suspect methods which are executing sql queries
     */
    private static final MethodMatchers SQL_INJECTION_SUSPECTS = MethodMatchers.or(
            MethodMatchers.create()
                    .ofSubTypes("org.hibernate.Session")
                    .names("createQuery", "createSQLQuery")
                    .withAnyParameters()
                    .build(),
            MethodMatchers.create()
                    .ofSubTypes(JAVA_SQL_STATEMENT)
                    .names("executeQuery", "execute", "executeUpdate", "executeLargeUpdate", "addBatch")
                    .withAnyParameters()
                    .build(),
            MethodMatchers.create()
                    .ofSubTypes(JAVA_SQL_CONNECTION)
                    .names("prepareStatement", "prepareCall", "nativeSQL")
                    .withAnyParameters()
                    .build(),
            MethodMatchers.create()
                    .ofTypes("javax.persistence.EntityManager")
                    .names("createNativeQuery", "createQuery")
                    .withAnyParameters()
                    .build(),
            MethodMatchers.create().ofSubTypes(SPRING_JDBC_OPERATIONS)
                    .names("batchUpdate", "execute", "query", "queryForList", "queryForMap", "queryForObject",
                            "queryForRowSet", "queryForInt", "queryForLong", "update")
                    .withAnyParameters()
                    .build(),
            MethodMatchers.create()
                    .ofTypes("org.springframework.jdbc.core.PreparedStatementCreatorFactory")
                    .names(CONSTRUCTOR, "newPreparedStatementCreator")
                    .withAnyParameters()
                    .build(),
            MethodMatchers.create()
                    .ofSubTypes("javax.jdo.PersistenceManager")
                    .names("newQuery")
                    .withAnyParameters()
                    .build(),
            MethodMatchers.create()
                    .ofSubTypes("javax.jdo.Query")
                    .names("setFilter", "setGrouping")
                    .withAnyParameters()
                    .build());

    // List of SQL query methods that should be checked for potential SQL injection vulnerabilities
    static List<String> SQL_QUERY_METHODS = Arrays.asList("createCallableStatement", "createPreparedStatement", "createStatement", "createViewObjectFromQueryStmt", "setWhereClause");

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Arrays.asList(Tree.Kind.METHOD_INVOCATION, Tree.Kind.VARIABLE);
    }
    private static boolean anyMatch(Tree tree) {

        if (tree.is(Tree.Kind.METHOD_INVOCATION)) {
            return SQL_INJECTION_SUSPECTS.matches((MethodInvocationTree) tree);
        }

        return false;
    }
    private static boolean anyQueryMethod(String queryMethodName) {
        return SQL_QUERY_METHODS.contains(queryMethodName);
    }

    // This method visit every node and verify if there is a variable/parameter or a method invocation, then checks expression having any dynamic concatenation.
    // if tree found is kind of variable, then we are converting tree to Variable tree object;
    // if tree found is kind of Method Invocation, then we are converting tree to MethodInvocation tree object;
    @Override
    public void visitNode(Tree tree) {
				  /* if(tree.is(Tree.Kind.VARIABLE)) {
					 VariableTree vt = (VariableTree) tree;
//					 System.out.println("vt=="+vt.simpleName().name());
//					 System.out.println("vt firsttoken =="+vt.firstToken().text());
//					 System.out.println("vt lasttoken =="+vt.lastToken().text());
					 ExpressionTree ex = vt.initializer();
					 if(ex != null) {
						 if(ex.symbolType().is(STRING_CLASS)) {
							 checkExpressionTree(ex);
						 }

					 }

//					 IdentifierTree identifier = vt.simpleName();
//					 System.out.println("Identifier name = "+identifier.name());
//					 System.out.println(" identifier first == "+identifier.firstToken().text());
//					 System.out.println(" identifier last == "+identifier.lastToken().text());


				  } */
        //else
        if(tree.is(Tree.Kind.METHOD_INVOCATION)) {
            MethodInvocationTree mit = (MethodInvocationTree) tree;
            //System.out.println("mit firsttoken =="+mit.firstToken().text());
            //System.out.println("mit lasttoken =="+mit.lastToken().text());
            //System.out.println("mit simple name == "+mit.methodSelect().lastToken().text());
            boolean result = anyMatch(tree);
            boolean queryMethodResult = anyQueryMethod(mit.methodSelect().lastToken().text());
            //System.out.println("result = "+result);
            //System.out.println(" queryMethod result = "+queryMethodResult);
            if(result || queryMethodResult) {
                Arguments args = mit.arguments();
                if(args != null) {
                    args.forEach(ex -> {
                        //System.out.println("ex.symbolType()::= "+ex.symbolType());

                        if(ex.symbolType().is(STRING_CLASS)) {
                            checkExpressionTree(ex);
                        }
                    });
                }
            }
        }
    }

    // This method checks if expression has any dynamic concatenation or any reassignments and report issue.

    private void checkExpressionTree( ExpressionTree sqlArg) {

        //System.out.println("sqlArg::= "+sqlArg);
        //System.out.println("Tree.Kind.IDENTIFIER:::"+Tree.Kind.IDENTIFIER);

        if (isDynamicConcatenation(sqlArg)) {
            reportIssue(sqlArg, EXECUTE_QUERY_MESSAGE);

        } else if (sqlArg.is(Tree.Kind.IDENTIFIER)) {
            IdentifierTree identifierTree = (IdentifierTree) sqlArg;
            Symbol symbol = identifierTree.symbol();
            //System.out.println("identifierTree.symbol()::= "+identifierTree.symbol());

            //System.out.println("symbol.declaration()::= "+symbol.declaration());


            ExpressionTree initializerOrExpression = getInitializerOrExpression(
                    symbol.declaration());
            //System.out.println("initializerOrExpression::= "+initializerOrExpression);

            //System.out.println("symbol.owner().declaration()::= "+symbol.owner().declaration()
                    //+"symbol.usages()::= "+symbol.usages());

            List<AssignmentExpressionTree> reassignments = getReassignments(
                    symbol.owner().declaration(), symbol.usages());

            if ((initializerOrExpression != null && isDynamicConcatenation(initializerOrExpression))
                    || reassignments.stream()
                    .anyMatch(SQLInjectionCheck::isDynamicPlusAssignment)) {
                reportIssue(sqlArg, EXECUTE_QUERY_MESSAGE, secondaryLocations(initializerOrExpression,
                        reassignments, identifierTree.name()), null);
            }
        }
    }


    private static List<JavaFileScannerContext.Location> secondaryLocations(@Nullable ExpressionTree initializerOrExpression,
                                                                            List<AssignmentExpressionTree> reassignments,
                                                                            String identifierName) {
        List<JavaFileScannerContext.Location> secondaryLocations = reassignments.stream()
                .map(assignment -> new JavaFileScannerContext.Location(String.format("SQL Query is assigned to '%s' -Custom", getVariableName(assignment)), assignment.expression()))
                .collect(Collectors.toList());

        if (initializerOrExpression != null) {
            secondaryLocations.add(new JavaFileScannerContext.Location(String.format("SQL Query is dynamically formatted and assigned to '%s' -Custom",
                    identifierName),
                    initializerOrExpression));
        }
        return secondaryLocations;
    }

    private static String getVariableName(AssignmentExpressionTree assignment) {
        ExpressionTree variable = assignment.variable();
        return ((IdentifierTree) variable).name();
    }



    private static boolean isDynamicPlusAssignment(ExpressionTree arg) {
        return arg.is(Tree.Kind.PLUS_ASSIGNMENT) && !((AssignmentExpressionTree) arg).expression().asConstant().isPresent();
    }

    private static boolean isDynamicConcatenation(ExpressionTree arg) {
        return arg.is(Tree.Kind.PLUS) && !arg.asConstant().isPresent();
    }

    public static List<AssignmentExpressionTree> getReassignments(@Nullable Tree ownerDeclaration,
                                                                  List<IdentifierTree> usages) {
        if (ownerDeclaration != null) {
            List<AssignmentExpressionTree> assignments = new ArrayList<>();
            for (IdentifierTree usage : usages) {
                checkAssignment(usage).ifPresent(assignments::add);
            }
            return assignments;
        }
        return new ArrayList<>();
    }

    private static Optional<AssignmentExpressionTree> checkAssignment(IdentifierTree usage) {
        Tree previousTree = usage;
        Tree nonParenthesisParent = previousTree.parent();

        while (nonParenthesisParent.is(Tree.Kind.PARENTHESIZED_EXPRESSION)) {
            previousTree = nonParenthesisParent;
            nonParenthesisParent = previousTree.parent();
        }

        if (nonParenthesisParent instanceof AssignmentExpressionTree) {
            AssignmentExpressionTree assignment = (AssignmentExpressionTree) nonParenthesisParent;
            if (assignment.variable().equals(previousTree)) {
                return Optional.of(assignment);
            }
        }
        return Optional.empty();
    }

    @CheckForNull
    public static ExpressionTree getInitializerOrExpression(@Nullable Tree tree) {
        if (tree == null) {
            return null;
        }
        if (tree.is(Tree.Kind.VARIABLE)) {
            return ((VariableTree) tree).initializer();
        } else if (tree.is(Tree.Kind.ENUM_CONSTANT)) {
            return ((EnumConstantTree) tree).initializer();
        } else if (tree instanceof AssignmentExpressionTree) {
            // All kinds of Assignment
            return ((AssignmentExpressionTree) tree).expression();
        }
        // Can be other declaration, like class
        return null;
    }

}
