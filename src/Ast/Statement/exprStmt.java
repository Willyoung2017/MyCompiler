package Ast.Statement;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;

public class exprStmt extends stmt{
    public expr expression;

    public exprStmt(){
        expression = null;
    }

    public exprStmt(expr expression){
        this.expression = expression;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
