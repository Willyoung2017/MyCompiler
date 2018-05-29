package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class voidType extends typ{
    public voidType(){
        size = 8;
    }
    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
