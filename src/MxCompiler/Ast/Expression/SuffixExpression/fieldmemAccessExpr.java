package MxCompiler.Ast.Expression.SuffixExpression;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;

public class fieldmemAccessExpr extends expr {
    public expr obj;
    //public String fieldMem;

    public fieldmemAccessExpr(){
        obj = null;
        name = null;
    }

    public fieldmemAccessExpr(expr obj, String fieldMem){
        this.obj = obj;
        this.name = fieldMem;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
