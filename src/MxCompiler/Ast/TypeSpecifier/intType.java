package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class intType extends typ{
    public intType(){
        size = 8;
    }
    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
