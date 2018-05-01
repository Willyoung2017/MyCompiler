package MxCompiler.Ast.Statement;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;
import MxCompiler.Ast.TypeSpecifier.typ;

public class varDecStmt extends stmt{
    public typ variableType;
    public expr variableExpr;

    public varDecStmt(){
        variableExpr = null;
        name = null;
        variableType = null;
    }

    public varDecStmt(typ varType, String varName, expr varExpr){
        variableType = varType;
        name = varName;
        variableExpr = varExpr;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
