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
import MxCompiler.Ast.astNode;
import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.func;
import MxCompiler.IR.IRnodes.instructions.binaryOpInstr;
import MxCompiler.IR.IRnodes.instructions.jump;
import MxCompiler.IR.IRnodes.intImd;
import MxCompiler.IR.IRnodes.virturalRegister;

import java.util.HashMap;
import java.util.Map;

public class IRbuilder implements ASTVisitor {
    private basicBlock curBlock;
    private basicBlock nextBlock;
    private basicBlock preBlock;
    private func curFunc;
    public Map<String, func> funcMap;

    public IRbuilder(){
        curBlock = null;
        funcMap = new HashMap<>();
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

    }

    @Override
    public void visit(funcDec node) {
        curFunc = new func();
        curFunc.setFuncType(node.functionType);
        funcMap.put(node.name, curFunc);
        for (varDec p : node.parameterList) {
            virturalRegister reg = new virturalRegister();
            reg.varName = p.name;
            curFunc.parameterList.add(reg);
        }
        node.parameterList.stream().forEachOrdered(this::visit);
        visit(node.functionStmt);

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
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(breakStmt node) {
        curBlock.pushBack(new jump(curBlock, nextBlock));

    }

    @Override
    public void visit(compoundStmt node) {
        node.stmtList.stream().forEachOrdered(this::visit);
    }

    @Override
    public void visit(continueStmt node) {
        curBlock.pushBack(new jump(curBlock, preBlock));
    }

    @Override
    public void visit(exprStmt node) {
        visit(node.expression);
    }

    @Override
    public void visit(forloopStmt node) {
        basicBlock initBlock = new basicBlock("for_init");
        curBlock = initBlock;
        visit(node.init);
        basicBlock condBlock = new basicBlock("for_cond");
        curBlock = condBlock;
        visit(node.cond);
        basicBlock stepBlock = new basicBlock("for_step");
        curBlock = stepBlock;
        visit(node.step);
        visit(node.forBody);
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
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(binaryExpr node) {
        switch(node.operator){
            case BITWISE_INCLUSIVE_OR:
            case BITWISE_EXCLUSIVE_OR:
            case BITWISE_AND:
            case MOD:
            case MUL:
            case DIV:
            case SUB:
            case ADD:
            case LEFT_SHIFT:
            case RIGHT_SHIFT:
                if(node.leftOperand.type instanceof intType)
                    processArithmeticExpr(node);
                else processStrArithmeticExpr(node);
                break;

            case GEQ:
            case LEQ:
            case LESS:
            case GREATER:
            case EQUAL:
            case NOT_EQUAL:
                if(node.leftOperand.type instanceof intType)
                    processComparisonExpr(node);
                else processStrComparisonExpr(node);
                break;

            case ASSIGN:
                processAssign(node);
                break;

            case LOGICAL_OR:
            case LOGICAL_AND:
                processLogicalExpr(node);
                break;
        }
    }

    private void processArithmeticExpr(binaryExpr node){
        binaryOpInstr instr = new binaryOpInstr();
        instr.operator = node.operator;
        instr.leftOperand = node.leftOperand.reg;
        instr.rightOperand = node.rightOperand.reg;
        node.reg = new virturalRegister();
        curBlock.pushBack(instr);
    }

    private void processStrArithmeticExpr(binaryExpr node){
        binaryOpInstr instr = new binaryOpInstr();
        

    }

    private void processComparisonExpr(binaryExpr node){

    }

    private void processStrComparisonExpr(binaryExpr node){

    }

    private void processAssign(binaryExpr node){

    }

    private void processLogicalExpr(binaryExpr node){

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
        node.reg = new intImd(node.value);
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

