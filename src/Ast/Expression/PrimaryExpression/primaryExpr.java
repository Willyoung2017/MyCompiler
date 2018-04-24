package Ast.Expression.PrimaryExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class primaryExpr extends expr {

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
