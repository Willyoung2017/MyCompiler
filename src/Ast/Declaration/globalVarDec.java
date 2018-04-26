package Ast.Declaration;
import Ast.BuildAST.ASTVisitor;
import Ast.TypeSpecifier.*;
import Ast.Expression.*;

public class globalVarDec extends dec{
    public typ variableType;
    //public String variableName;
    public expr variableExpression;

    public globalVarDec(){
        variableType = null;
        name = null;
        variableExpression = null;
    }

    public globalVarDec(typ varType, String varName, expr varExpr){
        variableType = varType;
        name = varName;
        variableExpression = varExpr;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}