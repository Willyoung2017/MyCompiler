package MxCompiler.SemanticChecker;

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
import MxCompiler.Ast.astNode;
import MxCompiler.Exception.compilationError;
import MxCompiler.Exception.semanticException;

import java.util.Stack;

public class dereferenceChecker implements ASTVisitor {
    public Stack<astNode> loopStack;
    public compilationError error;
    public dereferenceChecker(){
        loopStack = new Stack<>();
        error  = new compilationError();
    }
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
        if(node == null) return;
        node.classMems.stream().forEachOrdered(this::visit);
    }

    @Override
    public void visit(funcDec node) {
        if(node == null) return;
        visit(node.functionStmt);
    }

    @Override
    public void visit(globalVarDec node) {
        if(node == null) return;
        visit(node.variableExpression);
    }

    @Override
    public void visit(constructFuncDec node) {
        if(node == null) return;
        visit(node.funcStmt);
    }

    @Override
    public void visit(varDec node) {
        if(node == null) return;
        visit(node.variableExpression);
    }

    @Override
    public void visit(memberDec node) {
        if(node == null) return;
        visit(node.declaration);
    }

    @Override
    public void visit(stmt node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(breakStmt node) {
        if(node == null) return;
        if(loopStack.isEmpty()){
            error.add(new semanticException("BreakStatement can only exist in Loop!"+node.loc.locString()));
        }
    }

    @Override
    public void visit(compoundStmt node) {
        if(node == null) return;
        node.stmtList.stream().forEachOrdered(this::visit);
    }

    @Override
    public void visit(continueStmt node) {
        if(node == null) return;
        if(loopStack.isEmpty()){
            error.add(new semanticException("ContinueStatement can only exist in Loop!"+node.loc.locString()));
        }
    }

    @Override
    public void visit(exprStmt node) {
        if(node == null) return;
        visit(node.expression);
    }

    @Override
    public void visit(forloopStmt node) {
        if(node == null) return;
        loopStack.push(new astNode());
        visit(node.init);
        visit(node.cond);
        visit(node.step);
        visit(node.forBody);
        loopStack.pop();
    }

    @Override
    public void visit(ifStmt node) {
        if(node == null) return;
        visit(node.cond);
        visit(node.ifBody);
        visit(node.elseBody);
    }

    @Override
    public void visit(returnStmt node) {
        if(node == null) return;
        visit(node.returnExpr);
    }

    @Override
    public void visit(varDecStmt node) {
        if(node == null) return;
        visit(node.variableExpr);
    }

    @Override
    public void visit(whileloopStmt node) {
        if(node == null) return;
        loopStack.push(new astNode());
        visit(node.cond);
        visit(node.whileBody);
        loopStack.pop();
    }

    @Override
    public void visit(expr node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(binaryExpr node) {
        if(node == null) return;
        visit(node.leftOperand);
        if(!node.leftOperand.isLvalue)
            error.add(new semanticException("Left Operand must be leftvalue!"+node.loc.locString()));
        visit(node.rightOperand);
    }

    @Override
    public void visit(primaryExpr node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(boolConstant node) {
        if(node == null) return;
        node.isLvalue = false;
    }

    @Override
    public void visit(identifier node) {
        /*astNode ent = node.ent;
        if(ent != null){
            if(ent.type instanceof arrayType)
                node.isLvalue = false;
            else node.isLvalue = true;
        }*/
    }

    @Override
    public void visit(intConstant node) {
        if(node == null) return;
        node.isLvalue = false;
    }

    @Override
    public void visit(newExpr node) {
        if(node == null) return;
        node.isLvalue = false;
    }

    @Override
    public void visit(Null node) {
        if(node == null) return;
        node.isLvalue = false;
    }

    @Override
    public void visit(stringConstant node) {
        if(node == null) return;
        node.isLvalue = false;
    }

    @Override
    public void visit(fieldfuncAccessExpr node) {
        if(node == null) return;
        visit(node.obj);
        //node.isLvalue
    }

    @Override
    public void visit(fieldmemAccessExpr node) {
        if(node == null) return;
        visit(node.obj);
    }

    @Override
    public void visit(funcCall node) {
        if(node == null) return;
        visit(node.obj);
        node.isLvalue = node.obj.isLvalue;
    }

    @Override
    public void visit(indexAccessExpr node) {
        if(node == null) return;
        visit(node.array);
        visit(node.index);
    }

    @Override
    public void visit(selfDec node) {
        if(node == null) return;
        visit(node.operand);
        if(!node.operand.isLvalue)
            error.add(new semanticException("selfDec must be LeftValue!"+node.loc.locString()));
    }

    @Override
    public void visit(selfInc node) {
        if(node == null) return;
        visit(node.operand);
        if(!node.operand.isLvalue)
            error.add(new semanticException("selfInc must be LeftValue!"+node.loc.locString()));
    }

    @Override
    public void visit(suffixExpr node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(unaryExpr node) {
        if(node == null) return;
        visit(node.operand);
    }

    @Override
    public void visit(typ node) {
        if(node == null) return;
        node.accept(this);
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
