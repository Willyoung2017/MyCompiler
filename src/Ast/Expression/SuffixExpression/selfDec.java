package Ast.Expression.SuffixExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class selfDec extends expr {
    public expr operand;

    public selfDec(){
        operand = null;
    }

    public selfDec(expr operand){
        this.operand = operand;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}

