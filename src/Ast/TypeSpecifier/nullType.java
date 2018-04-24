package Ast.TypeSpecifier;

import Ast.BuildAST.ASTVisitor;

public class nullType extends typ{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
