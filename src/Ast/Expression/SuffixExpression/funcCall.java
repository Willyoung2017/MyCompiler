package Ast.Expression.SuffixExpression;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

import java.util.LinkedList;
import java.util.List;

public class funcCall extends expr {
    public expr obj;
    public List<expr> parameters;

    public funcCall(){
        obj = null;
        parameters = new LinkedList<>();
    }

    public funcCall(expr obj, List<expr> parameters){
        this.obj = obj;
        this.parameters = parameters;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
