package Ast.TypeSpecifier;

import Ast.BuildAST.ASTVisitor;

public class boolType extends typ{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
