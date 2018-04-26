package Ast.TypeSpecifier;

import Ast.BuildAST.ASTVisitor;

public class classType extends typ{
    //public String className;

    public classType(){
        name = null;
    }

    public classType(String className){
        this.name = className;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
