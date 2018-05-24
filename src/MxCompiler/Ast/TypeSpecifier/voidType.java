package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class voidType extends typ{
    public final int size = 0;
    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
