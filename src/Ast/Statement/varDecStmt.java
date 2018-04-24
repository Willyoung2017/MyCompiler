package Ast.Statement;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;
import Ast.TypeSpecifier.typ;

public class varDecStmt extends stmt{
    public typ variableType;
    public String variableName;
    public expr variableExpr;

    public varDecStmt(){
        variableExpr = null;
        variableName = null;
        variableType = null;
    }

    public varDecStmt(typ varType, String varName, expr varExpr){
        variableType = varType;
        variableName = varName;
        variableExpr = varExpr;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
