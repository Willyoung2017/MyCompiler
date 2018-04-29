package MxCompiler.Ast.Expression.SuffixExpression;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.UnaryExpression.unaryOp;
import MxCompiler.Ast.Expression.expr;

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
