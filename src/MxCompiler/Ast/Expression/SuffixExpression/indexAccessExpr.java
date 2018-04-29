package MxCompiler.Ast.Expression.SuffixExpression;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;

public class indexAccessExpr extends expr {
    public expr array;
    public expr index;

    public indexAccessExpr(){
        array = null;
        index = null;
    }

    public indexAccessExpr(expr array, expr index){
        this.array = array;
        this.index = index;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
