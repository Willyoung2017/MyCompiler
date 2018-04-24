package Ast.TypeSpecifier;

import Ast.BuildAST.ASTVisitor;

public class intType extends typ{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
