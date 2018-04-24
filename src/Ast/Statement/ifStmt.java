package Ast.Statement;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class ifStmt extends stmt{
    public expr cond;
    public stmt ifBody, elseBody;

    public ifStmt(){
        cond = null;
        ifBody = null;
        elseBody = null;
    }

    public ifStmt(expr condExpr, stmt ifBodyStmt, stmt elseBodyStmt){
        cond = condExpr;
        ifBody = ifBodyStmt;
        elseBody = elseBodyStmt;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
