package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class stringType extends typ{
    public final int size = 8;
    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
