package MxCompiler.Ast.Declaration;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.expr;
import MxCompiler.Ast.TypeSpecifier.typ;
import MxCompiler.IR.IRnodes.intValue;


public class varDec extends dec{
    public typ variableType;
    //public String variableName;
    public expr variableExpression;
    //for IR
    public intValue nodeValue;

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
