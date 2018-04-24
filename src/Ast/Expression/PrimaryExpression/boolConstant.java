package Ast.Expression.PrimaryExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class boolConstant extends expr {
    public Boolean value;

    public boolConstant(){
        value = null;
    }

    public boolConstant(Boolean value){
        this.value = value;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
