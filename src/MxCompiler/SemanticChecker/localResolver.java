package MxCompiler.SemanticChecker;

import MxCompiler.Ast.Declaration.*;
import MxCompiler.Ast.Expression.PrimaryExpression.*;
import MxCompiler.Ast.Expression.SuffixExpression.*;
import MxCompiler.Ast.Statement.*;
import MxCompiler.Ast.TypeSpecifier.*;
import MxCompiler.Ast.location;
import MxCompiler.Exception.semanticException;
import MxCompiler.Exception.compilationError;
import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Expression.BinaryExpression.binaryExpr;
import MxCompiler.Ast.Expression.UnaryExpression.unaryExpr;
import MxCompiler.Ast.Expression.expr;
import MxCompiler.Ast.abstractSyntaxTree;
import MxCompiler.Ast.astNode;

import java.util.LinkedList;
import java.util.List;

public class localResolver implements ASTVisitor {
    public LinkedList<scope> scopeStack;
    public compilationError error;
    public classDec thisClass;

    public localResolver(){
        scopeStack = new LinkedList<>();
        error = new compilationError();
    }

    private void pushScope(List<varDec> vars){
        localScope scp = new localScope(scopeStack.peek());
        for (astNode var : vars){
            scp.declareEntity(error, var);
        }
        scopeStack.push(scp);
    }

    public void checkMain(toplevelScope toplevel){
        compilationError e = new compilationError();
        location loc = new location(-1,-1);
        astNode mainNode = toplevel.get(e, "main", loc);
        if (mainNode == null){
            error.add(new semanticException("Main function not found!"));
        }
        else {
            if (!(mainNode instanceof funcDec)) {
                error.add(new semanticException("Main must be a function!"));
            }
            else {
                if (!(((funcDec) mainNode).functionType instanceof intType)) {
                    error.add(new semanticException("Returntype of Main must be INT!"));
                }
                if (((funcDec) mainNode).parameterList.size() != 0) {
                    error.add(new semanticException("Main cannot have parameters!"));
                }
            }
        }
    }

    @Override
    public void visit(abstractSyntaxTree node) {
        toplevelScope toplevel = new toplevelScope();
        scopeStack.add(toplevel);
        for (dec declaration: node.declarations){
            declaration.setScope(toplevel);
            if (!(declaration instanceof globalVarDec))
                toplevel.declareEntity(error, declaration);
        }
        node.setScope(toplevel);
        for (dec declaration: node.declarations){
                visit(declaration);
        }
        checkMain(toplevel);
    }

    @Override
    public void visit(dec node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(classDec node){
        if(node != null) {
            localScope scp = new localScope(scopeStack.peek());
            thisClass = node;
            for(memberDec dec : node.classMems){
                scp.declareEntity(error,dec.declaration);
            }
            scopeStack.push(scp);
            node.setScope(scp);
            node.classMems.stream().forEachOrdered(this::visit);
            scopeStack.pop();
        }
    }

    @Override
    public void visit(funcDec node) {
        if(node != null) {
            visit(node.functionType);
            pushScope(node.parameterList);
            node.parameterList.forEach(this::visit);
            visit(node.functionStmt);
            node.setScope(scopeStack.pop());
        }
    }

    @Override
    public void visit(globalVarDec node) {
        if(node != null){
            (scopeStack.peek()).declareEntity(error, node);
            visit(node.variableType);
            visit(node.variableExpression);
        }
    }

    @Override
    public void visit(constructFuncDec node) {
        if(node != null) {
            pushScope(node.parameters);
            visit(node.funcStmt);
            node.setScope(scopeStack.pop());
        }
    }

    @Override
    public void visit(varDec node) {
        if(node != null){
            node.setScope(scopeStack.peek());
            visit(node.variableType);
            visit(node.variableExpression);
        }
    }

    @Override
    public void visit(memberDec node) {
        if(node != null){
            node.setScope(scopeStack.peek());
            visit(node.declaration);
        }
    }

    @Override
    public void visit(stmt node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(breakStmt node) {

    }

    @Override
    public void visit(compoundStmt node) {
        if(node != null){
            localScope scp = new localScope(scopeStack.peek());
            scopeStack.push(scp);
            node.setScope(scopeStack.peek());
            node.stmtList.stream().forEachOrdered(this::visit);
            scopeStack.pop();
        }
    }

    @Override
    public void visit(continueStmt node) {

    }

    @Override
    public void visit(exprStmt node) {
        if(node != null){
            node.setScope(scopeStack.peek());
            visit(node.expression);
        }
    }

    @Override
    public void visit(forloopStmt node) {
        if(node != null){
            visit(node.init); // init dec to be done
            visit(node.cond);
            visit(node.step);
            if(node.forBody != null){
                localScope scp = new localScope(scopeStack.peek());
                scopeStack.push(scp);
                node.forBody.setScope(scp);
                visit(node.forBody);
                scopeStack.pop();
            }
        }
    }

    @Override
    public void visit(ifStmt node) {
        if(node != null) {
            visit(node.cond);
            if(node.ifBody != null){
                localScope scp = new localScope(scopeStack.peek());
                scopeStack.push(scp);
                node.ifBody.setScope(scp);
                visit(node.ifBody);
                scopeStack.pop();
            }
            if(node.elseBody != null) {
                localScope scp = new localScope(scopeStack.peek());
                scopeStack.push(scp);
                node.elseBody.setScope(scp);
                visit(node.elseBody);
                scopeStack.pop();
            }
        }
    }

    @Override
    public void visit(returnStmt node) {
        if(node != null){
            visit(node.returnExpr);
        }
    }

    @Override
    public void visit(varDecStmt node) {
        if(node != null){
            (scopeStack.peek()).declareEntity(error, node);
            node.setScope(scopeStack.peek());
            visit(node.variableType);
            visit(node.variableExpr);
        }
    }

    @Override
    public void visit(whileloopStmt node) {
        if(node != null){
            visit(node.cond);
            if(node.whileBody != null) {
                localScope scp = new localScope(scopeStack.peek());
                scopeStack.push(scp);
                node.whileBody.setScope(scp);
                visit(node.whileBody);
                scopeStack.pop();
            }
        }
    }

    @Override
    public void visit(expr node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(binaryExpr node) {
        if(node != null){
            visit(node.leftOperand);
            visit(node.rightOperand);
        }
    }

    @Override
    public void visit(primaryExpr node) {

    }

    @Override
    public void visit(boolConstant node) {

    }

    @Override
    public void visit(identifier node) {
        node.setScope(scopeStack.peek());
        if(node.name.equals("this")){
            node.setEnt(thisClass);
            return;
        }
        astNode ent = (scopeStack.peek()).get(error,node.name, node.loc);
        if(ent != null) {
            node.setEnt(ent);
        }
    }

    @Override
    public void visit(intConstant node) {

    }

    @Override
    public void visit(newExpr node) {
        if (node != null){
            visit(node.newName);
            node.setScope(scopeStack.peek());
        }
    }

    @Override
    public void visit(Null node) {

    }

    @Override
    public void visit(stringConstant node) {

    }
//To be done
    @Override
    public void visit(fieldfuncAccessExpr node) {
        if (node != null) {
            node.setScope(scopeStack.peek());
            visit(node.obj);
            node.parameters.stream().forEachOrdered(this::visit);
        }
    }

    @Override
    public void visit(fieldmemAccessExpr node) {
        if (node != null){
            node.setScope(scopeStack.peek());
            visit(node.obj);
        }
    }

    @Override
    public void visit(funcCall node) {
        if (node != null){
            node.parameters.stream().forEachOrdered(this::visit);
            if(node.obj.name.equals("getInt")||node.obj.name.equals("getString")||node.obj.name.equals("toString")||node.obj.name.equals("print")||node.obj.name.equals("println"))
                return;
            visit(node.obj);

        }
    }

    @Override
    public void visit(indexAccessExpr node) {
        if (node != null){
            node.setScope(scopeStack.peek());
            visit(node.array);
            visit(node.index);
        }
    }

    @Override
    public void visit(selfDec node) {
        if(node != null){
            visit(node.operand);
        }
    }

    @Override
    public void visit(selfInc node) {
        if(node != null){
            visit(node.operand);
        }
    }

    @Override
    public void visit(suffixExpr node) {
        if(node != null){
            visit(node.operand);
        }
    }

    @Override
    public void visit(unaryExpr node) {
        if(node != null){
            visit(node.operand);
        }
    }

    @Override
    public void visit(typ node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(arrayType node) {
        if(node != null){
            node.setScope(scopeStack.peek());
            visit(node.baseType);
            visit(node.index);
        }
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
