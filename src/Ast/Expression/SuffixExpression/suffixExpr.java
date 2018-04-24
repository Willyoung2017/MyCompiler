package Ast.Expression.SuffixExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.UnaryExpression.unaryOp;
import Ast.Expression.expr;

public class suffixExpr extends expr {
    public unaryOp operator;
    public expr operand;

    public suffixExpr(){
        operator = null;
        operand = null;
    }

    public suffixExpr(unaryOp operator, expr operand){
        this.operand = operand;
        this.operator = operator;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
