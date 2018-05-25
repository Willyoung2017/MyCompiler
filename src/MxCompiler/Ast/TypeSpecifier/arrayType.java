package MxCompiler.Ast.TypeSpecifier;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;

public class arrayType extends typ{
    public typ baseType;
    public expr index;
    public typ rootType = null;
    final public int size = 8;

    public arrayType(){
        baseType = null;
        index = null;
    }

    public arrayType(typ baseType, expr index){
        this.baseType = baseType;
        this.index = index;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
