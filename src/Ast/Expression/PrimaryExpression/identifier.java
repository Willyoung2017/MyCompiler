package Ast.Expression.PrimaryExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;
import Ast.astNode;

public class identifier extends expr {
    //public String name;
    public astNode ent;

    public identifier(){
        name = null;
    }

    public identifier(String name){
        this.name = name;
    }

    public void setEnt(astNode ent){
        this.ent = ent;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
