package Ast.BuildAST;

import Ast.Declaration.*;
import Ast.Expression.BinaryExpression.binaryExpr;
import Ast.Expression.PrimaryExpression.*;
import Ast.Expression.SuffixExpression.*;
import Ast.Expression.UnaryExpression.unaryExpr;
import Ast.Expression.expr;
import Ast.Statement.*;
import Ast.TypeSpecifier.*;
import Ast.abstractSyntaxTree;

public interface ASTVisitor {
    public void visit(abstractSyntaxTree node);

    public void visit(dec node);
    public void visit(classDec node);
    public void visit(funcDec node);
    public void visit(globalVarDec node);
    public void visit(constructFuncDec node);
    public void visit(varDec node);
    public void visit(memberDec node);

    public void visit(stmt node);
    public void visit(breakStmt node);
    public void visit(compoundStmt node);
    public void visit(continueStmt node);
    public void visit(exprStmt node);
    public void visit(forloopStmt node);
    public void visit(ifStmt node);
    public void visit(returnStmt node);
    public void visit(varDecStmt node);
    public void visit(whileloopStmt node);

    public void visit(expr node);
    public void visit(binaryExpr node);
    public void visit(primaryExpr node);
    public void visit(boolConstant node);
    public void visit(identifier node);
    public void visit(intConstant node);
    public void visit(newExpr node);
    public void visit(Null node);
    public void visit(stringConstant node);
    public void visit(fieldfuncAccessExpr node);
    public void visit(fieldmemAccessExpr node);
    public void visit(funcCall node);
    public void visit(indexAccessExpr node);
    public void visit(selfDec node);
    public void visit(selfInc node);
    public void visit(suffixExpr node);
    public void visit(unaryExpr node);

    public void visit(typ node);
    public void visit(arrayType node);
    public void visit(boolType node);
    public void visit(classType node);
    public void visit(intType node);
    public void visit(nullType node);
    public void visit(stringType node);
    public void visit(voidType node);

}
