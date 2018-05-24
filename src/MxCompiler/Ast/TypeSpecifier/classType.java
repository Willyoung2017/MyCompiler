package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class classType extends typ{
    //public String className;
    final public int size = 8;

    public classType(){
        name = null;
    }

    public classType(String className){
        this.name = className;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
