package ca.cmic.rules;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.BinaryExpressionTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.LiteralTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Rule(key = "SQL-102")
public class SQLInjection2Check extends IssuableSubscriptionVisitor {

        private static final String MESSAGE = "SQL-102 : Make sure using a dynamically formatted SQL query(parameter/inline) is safe here.";
        private final Set<Symbol> symbols = new HashSet<>();

        @Override
        public List<Tree.Kind> nodesToVisit() {
                return ImmutableList.of(Tree.Kind.METHOD_INVOCATION);
        }

        @Override
        public void visitNode(Tree tree) {
                MethodInvocationTree mit = (MethodInvocationTree) tree;

                Symbol.MethodSymbol methodSymbol = (Symbol.MethodSymbol) mit.symbol();
                if (methodSymbol == null) {
                        return;
                }

                List<ExpressionTree> arguments = mit.arguments();
                for (ExpressionTree argument : arguments) {
                        if (argument.is(Tree.Kind.PLUS)) {
                                BinaryExpressionTree binaryExpressionTree = (BinaryExpressionTree) argument;
                                if (isUserInput(binaryExpressionTree.leftOperand()) || isUserInput(binaryExpressionTree.rightOperand())) {
                                        reportIssue(binaryExpressionTree.operatorToken(), MESSAGE);
                                }
                        }
                }
        }

        private boolean isUserInput(ExpressionTree expressionTree) {
                if (expressionTree.is(Tree.Kind.STRING_LITERAL)) {
                        LiteralTree literalTree = (LiteralTree) expressionTree;
                        String value = literalTree.value();
                        return value.startsWith("'") || value.startsWith("\"");
                } else {
                        Symbol symbol = null;
                        if (expressionTree.is(Tree.Kind.IDENTIFIER)) {
                                symbol = ((org.sonar.plugins.java.api.tree.IdentifierTree) expressionTree).symbol();
                        } else if (expressionTree.is(Tree.Kind.MEMBER_SELECT)) {
                                symbol = ((org.sonar.plugins.java.api.tree.MemberSelectExpressionTree) expressionTree).identifier().symbol();
                        }
                        return symbol != null && symbol.isVariableSymbol() && isUserInput(symbol);
                }
        }

        private boolean isUserInput(Symbol symbol) {
                return symbol.type().is("java.lang.String") && !symbol.metadata().isAnnotatedWith("javax.persistence.Column");
        }

}
