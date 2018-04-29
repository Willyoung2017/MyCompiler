package MxCompiler.Ast.Expression.PrimaryExpression;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;

public class stringConstant extends expr {
    public String value;

    public  stringConstant(){
        value = null;
    }

    public stringConstant(String value){
        this.value = value;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
