package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class nullType extends typ{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
