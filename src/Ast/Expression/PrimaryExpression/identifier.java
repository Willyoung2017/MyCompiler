package Ast.Expression.PrimaryExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class identifier extends expr {
    public String name;

    public identifier(){
        name = null;
    }

    public identifier(String name){
        this.name = name;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
