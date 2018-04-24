package Ast.Statement;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class returnStmt extends stmt{
    public expr returnExpr;

    public  returnStmt(){
        returnExpr = null;
    }

    public returnStmt(expr returnExpression){
        returnExpr = returnExpression;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
