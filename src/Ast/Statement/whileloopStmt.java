package Ast.Statement;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class whileloopStmt extends stmt{
    public expr cond;
    public stmt whileBody;

    public whileloopStmt(){
        cond = null;
        whileBody = null;
    }

    public whileloopStmt(expr condExpr, stmt whileBodyStmt){
        cond = condExpr;
        whileBody = whileBodyStmt;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
