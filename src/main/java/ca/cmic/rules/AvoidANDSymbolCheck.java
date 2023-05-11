/*The class defines a constant OPERATORS_TO_CHECK with the value "&", indicating that only the "&" operator should be flagged. It also defines a constant REPORT_MESSAGE that will be used in the event that an issue is found.

The nodesToVisit() method overrides a method in the IssuableSubscriptionVisitor class to specify which types of AST nodes the rule should subscribe to. In this case, it subscribes to the AND and AND_ASSIGNMENT nodes.

The visitNode() method overrides another method in the IssuableSubscriptionVisitor class and is called whenever an AST node of the subscribed types is encountered during analysis. The method checks whether the operator used in the binary expression is contained within the OPERATORS_TO_CHECK list, and if so, it reports an issue using the reportIssue() method, passing in the AST node and the REPORT_MESSAGE. 

In this case, an AST (Abstract Syntax Tree) node refers to a node in the Java AST that represents a binary expression using the "&" operator.

In the visitNode() method, the tree parameter is the AST node that the visitor is currently visiting. Since this class only subscribes to AND and AND_ASSIGNMENT nodes, the tree parameter will always be a BinaryExpressionTree node representing a binary expression.

The BinaryExpressionTree class is part of the SonarQube Java plugin API and represents a binary expression in the Java AST. The operatorToken() method of this class returns the SyntaxToken representing the operator used in the expression.

So, in summary, the AST node in this case is a BinaryExpressionTree node representing a binary expression using the "&" operator, and the visitNode() method extracts the operator used in the expression by calling the operatorToken() method on the BinaryExpressionTree object.*/

package ca.cmic.rules;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.BinaryExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Arrays;
import java.util.List;

@Rule(key = "Java-101")
public class AvoidANDSymbolCheck extends IssuableSubscriptionVisitor {

    //private static final List<String> OPERATORS_TO_CHECK = Arrays.asList("&");
    private static final String OPERATORS_TO_CHECK = "&";

    private static final String REPORT_MESSAGE = "Java-101: Avoid using the symbol \""+ OPERATORS_TO_CHECK + "\", as it can lead to unexpected behavior.";

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Arrays.asList(Tree.Kind.AND, Tree.Kind.AND_ASSIGNMENT);
    }

    @Override
    public void visitNode(Tree tree) {
        BinaryExpressionTree binaryExpression = (BinaryExpressionTree) tree;
        String operator = binaryExpression.operatorToken().text();
        if (OPERATORS_TO_CHECK.contains(operator)) {
            reportIssue(binaryExpression, REPORT_MESSAGE);
        }
    }
}
//test commit
