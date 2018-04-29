package Ast.Declaration;

import Ast.BuildAST.ASTVisitor;
import Ast.Expression.expr;
import Ast.TypeSpecifier.typ;


public class varDec extends dec{
    public typ variableType;
    //public String variableName;
    public expr variableExpression;

    public varDec(){
        variableType = null;
        name = null;
        variableExpression = null;
    }

    public varDec(typ varType, String varName, expr varExpr) {
        variableType = varType;
        name = varName;
        variableExpression = varExpr;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }

}
