package MxCompiler.Ast.Expression.UnaryExpression;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;

public class unaryExpr extends expr {
    public expr operand;
    public unaryOp operator;

    public unaryExpr(){
        operand = null;
        operator = null;
    }

    public unaryExpr(expr operand, unaryOp operator){
        this.operand = operand;
        this.operator = operator;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}

