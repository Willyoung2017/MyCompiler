package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class nullType extends typ{
    public final int size = 0;
    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
