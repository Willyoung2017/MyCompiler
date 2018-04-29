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
import Ast.BuildAST.Declaration.*;
import Ast.BuildAST.Expression.PrimaryExpression.*;
import Ast.BuildAST.Expression.SuffixExpression.*;
import Ast.BuildAST.Statement.*;
import Ast.BuildAST.TypeSpecifier.*;

import java.io.PrintStream;

public class ASTViewer implements ASTVisitor {
    PrintStream out;

    public ASTViewer(PrintStream out){
        this.out = out;
    }

    @Override
    public void visit(abstractSyntaxTree node) {
        if (node != null) {
            out.println("-----------myAST-------------");
            //out.println("[Root]{");
            node.declarations.stream().forEachOrdered(this::visit);
            //out.println("}");
        }
    }

    @Override
    public void visit(dec node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(classDec node) {
        if(node != null){
            out.println("[Class: "+node.name+"]{"+node.loc.locString());
            out.println("ClassMembers:");
            node.classMems.stream().forEachOrdered(this::visit);
            out.println("}");
        }
    }

    @Override
    public void visit(funcDec node) {
        if (node != null) {
            out.println("[Func: " + node.name + "]{" + node.loc.locString());
            out.println("ReturnType: ");
            visit(node.functionType);
            out.println("Parameters: ");
            node.parameterList.stream().forEachOrdered(this::visit);
            out.println("FuncBody: ");
            visit(node.functionStmt);
            out.println("}");
        }
    }

    @Override
    public void visit(globalVarDec node) {
        if (node != null){
            out.println("[GlobalVar: "+node.name+"]{"+node.loc.locString());
            out.println("VarType: ");
            visit(node.variableType);
            out.println("VarExpr: ");
            visit(node.variableExpression);
            out.println("}");
        }
    }

    @Override
    public void visit(constructFuncDec node) {
        if (node != null){
            out.println("[ConstructFunction: "+node.name+"]{"+node.loc.locString());
            out.println("FuncBody: ");
            visit(node.funcStmt);
            out.println("}");
        }

    }

    @Override
    public void visit(varDec node) {
        if (node != null){
            out.println("[Var: "+node.name+"]{"+node.loc.locString());
            out.println("VarType: ");
            visit(node.variableType);
            out.println("VarExpr: ");
            visit(node.variableExpression);
            out.println("}");
        }
    }

    @Override
    public void visit(memberDec node) {
        if (node != null){
            visit(node.declaration);
        }
    }

    @Override
    public void visit(stmt node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(breakStmt node) {
        if (node != null){
            out.println("[BreakStatement]");
        }

    }

    @Override
    public void visit(compoundStmt node) {
        if (node != null){
            node.stmtList.stream().forEachOrdered(this::visit);
        }
    }

    @Override
    public void visit(continueStmt node) {
        if (node != null){
            out.println("[ContinueStatement]");
        }
    }

    @Override
    public void visit(exprStmt node) {
        if (node != null){
            out.println("[ExpressionStatement]{");
            visit(node.expression);
            out.println("}");
        }
    }

    @Override
    public void visit(forloopStmt node) {
        if (node != null){
            out.println("[ForLoop]{"+node.loc.locString());
            out.println("init: ");
            visit(node.init);
            out.println("step: ");
            visit(node.step);
            out.println("cond: ");
            visit(node.cond);
            out.println("ForBody: ");
            visit(node.forBody);
            out.println("}");
        }
    }

    @Override
    public void visit(ifStmt node) {
        if (node != null){
            out.println("[IfStatement]{"+node.loc.locString());
            out.println("cond: ");
            visit(node.cond);
            out.println("IfBody: ");
            visit(node.ifBody);
            out.println("ElseBody: ");
            visit(node.elseBody);
            out.println("}");
        }
    }

    @Override
    public void visit(returnStmt node) {
        if (node != null){
            out.println("[ReturnStatement]{"+node.loc.locString());
            out.println("ReturnType: ");
            visit(node.returnExpr);
            out.println("}");
        }
    }

    @Override
    public void visit(varDecStmt node) {
        if (node != null){
            out.println("[VarStatement]{"+node.loc.locString());
            out.println("VarName: "+node.name);
            out.println("VarType: ");
            visit(node.variableType);
            out.println("VarExpr: ");
            visit(node.variableExpr);
            out.println("}");
        }
    }

    @Override
    public void visit(whileloopStmt node) {
        if (node != null){
            out.println("[WhileLoop]{"+node.loc.locString());
            out.println("cond: ");
            visit(node.cond);
            out.println("WhileBody: ");
            visit(node.whileBody);
            out.println("}");
        }
    }

    @Override
    public void visit(expr node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(binaryExpr node) {
        if (node != null){
            out.println("[BinExpr]{"+node.loc.locString());
            out.println("LeftOperand: ");
            visit(node.leftOperand);
            out.println("Operator: "+node.operator.toString());
            out.println("RightOperand: ");
            visit(node.rightOperand);
            out.println("}");
        }
    }

    @Override
    public void visit(primaryExpr node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(boolConstant node) {
        if (node != null){
            out.println("[BoolConstant: "+node.value.toString()+"]"+node.loc.locString());
        }
    }

    @Override
    public void visit(identifier node) {
        if (node != null){
            out.println("[Identifier: "+node.name+"]"+node.loc.locString());
        }
    }

    @Override
    public void visit(intConstant node) {
        if (node != null){
            out.println("[IntConstant: "+node.value.toString()+"]"+node.loc.locString());
        }
    }

    @Override
    public void visit(newExpr node) {
        if (node != null){
            out.println("[NewExpr]{"+node.loc.locString());
            out.println("NewType: ");
            visit(node.newName);
            out.println("}");
        }
    }

    @Override
    public void visit(Null node) {
        if (node != null){
            out.println("[NULL]"+node.loc.locString());
        }
    }

    @Override
    public void visit(stringConstant node) {
        if (node != null){
            out.println("[StringConstant: "+node.value+"]"+node.loc.locString());
        }
    }

    @Override
    public void visit(fieldfuncAccessExpr node) {
        if (node != null){
            out.println("[FieldFuncAccessExpr]{"+node.loc.locString());
            out.println("obj: ");
            visit(node.obj);
            out.println("FuncName: "+node.name);
            out.println("parameters: ");
            node.parameters.stream().forEachOrdered(this::visit);
        }
    }

    @Override
    public void visit(fieldmemAccessExpr node) {
        if (node != null){
            out.println("[FieldMemAccessExpr]{"+node.loc.locString());
            out.println("obj: ");
            visit(node.obj);
            out.println("Mem: "+node.name);
            out.println("}");
        }
    }

    @Override
    public void visit(funcCall node) {
        if (node != null){
            out.println("[FuncCall]{"+node.loc.locString());
            out.println("obj: ");
            visit(node.obj);
            out.println("parameters: ");
            node.parameters.stream().forEachOrdered(this::visit);
        }
    }

    @Override
    public void visit(indexAccessExpr node) {
        if (node != null){
            out.println("[IndexAccessExpr]{"+node.loc.locString());
            out.println("ArrayExpr: ");
            visit(node.array);
            out.println("DimensionExpr: ");
            visit(node.index);
            out.println("}");
        }
    }

    @Override
    public void visit(selfDec node) {
        if (node != null){
            out.println("[SelfDec]{"+node.loc.locString());
            out.println("operand: ");
            visit(node.operand);
            out.println("}");
        }
    }

    @Override
    public void visit(selfInc node) {
        if (node != null){
            out.println("[SelfInc]{"+node.loc.locString());
            out.println("operand: ");
            visit(node.operand);
            out.println("}");
        }
    }

    @Override
    public void visit(suffixExpr node) {
        if (node != null){
            out.println("[SuffixExpr]{"+node.loc.locString());
            out.println("operand: ");
            visit(node.operand);
            out.println("operator: "+node.operator.toString());
            out.println("}");
        }
    }

    @Override
    public void visit(unaryExpr node) {
        if (node != null){
            out.println("[UnaryExpr]{"+node.loc.locString());
            out.println("operator: "+node.operator.toString());
            out.println("operand: ");
            visit(node.operand);
            out.println("}");
        }
    }

    @Override
    public void visit(typ node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(arrayType node) {
        if (node != null){
            out.println("(arrayType){");
            out.println("BaseType: ");
            visit(node.baseType);
            out.println("Dimension: ");
            visit(node.index);
            out.println("}");
        }
    }

    @Override
    public void visit(boolType node) {
        if (node != null){
            out.println("(boolType)");
        }
    }

    @Override
    public void visit(classType node) {
        if (node != null){
            out.println("(classType: "+node.name+")");
        }
    }

    @Override
    public void visit(intType node) {
        if (node != null){
            out.println("(intType)");
        }
    }

    @Override
    public void visit(nullType node) {
        if (node != null){
            out.println("(nullType)");
        }
    }

    @Override
    public void visit(stringType node) {
        if (node != null){
            out.println("(stringType)");
        }
    }

    @Override
    public void visit(voidType node) {
        if (node != null){
            out.println("(voidType)");
        }
    }
}
