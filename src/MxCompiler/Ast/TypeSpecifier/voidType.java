package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class voidType extends typ{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
