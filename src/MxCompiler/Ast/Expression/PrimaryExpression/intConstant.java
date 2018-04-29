package MxCompiler.Ast.Expression.PrimaryExpression;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;

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
