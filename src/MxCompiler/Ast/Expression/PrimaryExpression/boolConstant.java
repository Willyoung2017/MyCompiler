package MxCompiler.Ast.Expression.PrimaryExpression;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;

public class boolConstant extends expr {
    public Boolean value;

    public boolConstant(){
        value = null;
    }

    public boolConstant(Boolean value){
        this.value = value;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
