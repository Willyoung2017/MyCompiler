package Ast.Expression.PrimaryExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class stringConstant extends expr {
    public String value;

    public  stringConstant(){
        value = null;
    }

    public stringConstant(String value){
        this.value = value;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
