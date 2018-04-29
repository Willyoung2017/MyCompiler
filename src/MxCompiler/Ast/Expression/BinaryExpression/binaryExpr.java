package MxCompiler.Ast.Expression.BinaryExpression;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;

public class binaryExpr extends expr {
    public expr leftOperand, rightOperand;
    public binaryOp operator;

    public binaryExpr(){
        leftOperand = null;
        rightOperand = null;
        operator = null;
    }

    public binaryExpr(expr leftOperand, expr rightOperand, binaryOp operator){
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
