package Ast.Expression.SuffixExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class fieldmemAccessExpr extends expr {
    public expr obj;
    public String fieldMem;

    public fieldmemAccessExpr(){
        obj = null;
        fieldMem = null;
    }

    public fieldmemAccessExpr(expr obj, String fieldMem){
        this.obj = obj;
        this.fieldMem = fieldMem;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
