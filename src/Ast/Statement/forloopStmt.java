package Ast.Statement;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class forloopStmt extends stmt{
    public expr init, step, cond;
    public stmt forBody;

    public forloopStmt(){
        init = null;
        step = null;
        cond = null;
    }

    public forloopStmt(expr initExpr, expr stepExpr, expr condExpr, stmt forBodyStmt){
        init = initExpr;
        step = stepExpr;
        cond = condExpr;
        forBody = forBodyStmt;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
