package Ast.Expression.SuffixExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class selfInc extends expr {
    public expr operand;

    public selfInc(){
        operand = null;
    }

    public selfInc(expr operand){
        this.operand = operand;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
