package Ast.Expression.PrimaryExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

import java.math.BigInteger;

public class intConstant extends expr {
    public BigInteger value;

    public intConstant(){
        value = null;
    }

    public intConstant(BigInteger value){
        this.value = value;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
