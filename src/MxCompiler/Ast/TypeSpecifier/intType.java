package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class intType extends typ{
    final public int size = 4;
    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
