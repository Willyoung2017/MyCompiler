package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class classType extends typ{
    //public String className;

    public classType(){
        name = null;
        size = 8;
    }

    public classType(String className){
        this.name = className;
        size = 8;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
