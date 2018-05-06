package MxCompiler.IR;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Declaration.*;
import MxCompiler.Ast.Expression.BinaryExpression.binaryExpr;
import MxCompiler.Ast.Expression.PrimaryExpression.*;
import MxCompiler.Ast.Expression.SuffixExpression.*;
import MxCompiler.Ast.Expression.UnaryExpression.unaryExpr;
import MxCompiler.Ast.Expression.expr;
import MxCompiler.Ast.Statement.*;
import MxCompiler.Ast.TypeSpecifier.*;
import MxCompiler.Ast.abstractSyntaxTree;
import MxCompiler.IR.IRnodes.basicBlock;

public class IRbuilder implements ASTVisitor {
    private basicBlock curBlock;

    @Override
    public void visit(abstractSyntaxTree node) {
        node.declarations.stream().forEachOrdered(this::visit);
    }

    @Override
    public void visit(dec node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(classDec node) {

    }

    @Override
    public void visit(funcDec node) {

    }

    @Override
    public void visit(globalVarDec node) {

    }

    @Override
    public void visit(constructFuncDec node) {

    }

    @Override
    public void visit(varDec node) {

    }

    @Override
    public void visit(memberDec node) {

    }

    @Override
    public void visit(stmt node) {

    }

    @Override
    public void visit(breakStmt node) {

    }

    @Override
    public void visit(compoundStmt node) {

    }

    @Override
    public void visit(continueStmt node) {

    }

    @Override
    public void visit(exprStmt node) {

    }

    @Override
    public void visit(forloopStmt node) {

    }

    @Override
    public void visit(ifStmt node) {

    }

    @Override
    public void visit(returnStmt node) {

    }

    @Override
    public void visit(varDecStmt node) {

    }

    @Override
    public void visit(whileloopStmt node) {

    }

    @Override
    public void visit(expr node) {

    }

    @Override
    public void visit(binaryExpr node) {

    }

    @Override
    public void visit(primaryExpr node) {

    }

    @Override
    public void visit(boolConstant node) {

    }

    @Override
    public void visit(identifier node) {

    }

    @Override
    public void visit(intConstant node) {

    }

    @Override
    public void visit(newExpr node) {

    }

    @Override
    public void visit(Null node) {

    }

    @Override
    public void visit(stringConstant node) {

    }

    @Override
    public void visit(fieldfuncAccessExpr node) {

    }

    @Override
    public void visit(fieldmemAccessExpr node) {

    }

    @Override
    public void visit(funcCall node) {

    }

    @Override
    public void visit(indexAccessExpr node) {

    }

    @Override
    public void visit(selfDec node) {

    }

    @Override
    public void visit(selfInc node) {

    }

    @Override
    public void visit(suffixExpr node) {

    }

    @Override
    public void visit(unaryExpr node) {

    }

    @Override
    public void visit(typ node) {

    }

    @Override
    public void visit(arrayType node) {

    }

    @Override
    public void visit(boolType node) {

    }

    @Override
    public void visit(classType node) {

    }

    @Override
    public void visit(intType node) {

    }

    @Override
    public void visit(nullType node) {

    }

    @Override
    public void visit(stringType node) {

    }

    @Override
    public void visit(voidType node) {

    }
}

