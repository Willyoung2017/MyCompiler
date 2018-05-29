package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class boolType extends typ{
    public boolType(){ size = 8;}
    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
