package Ast.TypeSpecifier;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class arrayType extends typ{
    public typ baseType;
    public expr index;

    public arrayType(){
        baseType = null;
        index = null;
    }

    public arrayType(typ baseType, expr index){
        this.baseType = baseType;
        this.index = index;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
