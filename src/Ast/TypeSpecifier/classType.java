package Ast.TypeSpecifier;

import Ast.BuildAST.ASTVisitor;

public class classType extends typ{
    public String className;

    public classType(){
        className = null;
    }

    public classType(String className){
        this.className = className;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
