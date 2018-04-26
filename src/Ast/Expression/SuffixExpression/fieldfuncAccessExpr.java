package Ast.Expression.SuffixExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

import java.util.LinkedList;
import java.util.List;

public class fieldfuncAccessExpr extends expr {
    public expr obj;
    //public String funcName;
    public List<expr>  parameters;

    public fieldfuncAccessExpr(){
        obj = null;
        name = null;
        parameters = new LinkedList<>();
    }

    public fieldfuncAccessExpr(expr obj, String funcName, List<expr> parameters){
        this.obj = obj;
        this.name = funcName;
        this.parameters = parameters;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
