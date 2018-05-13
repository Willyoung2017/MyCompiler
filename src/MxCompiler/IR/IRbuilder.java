package MxCompiler.IR;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Declaration.*;
import MxCompiler.Ast.Expression.BinaryExpression.binaryExpr;
import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.Ast.Expression.PrimaryExpression.*;
import MxCompiler.Ast.Expression.SuffixExpression.*;
import MxCompiler.Ast.Expression.UnaryExpression.unaryExpr;
import MxCompiler.Ast.Expression.UnaryExpression.unaryOp;
import MxCompiler.Ast.Expression.expr;
import MxCompiler.Ast.Statement.*;
import MxCompiler.Ast.TypeSpecifier.*;
import MxCompiler.Ast.abstractSyntaxTree;
import MxCompiler.Ast.astNode;
import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.func;
import MxCompiler.IR.IRnodes.instructions.*;
import MxCompiler.IR.IRnodes.intImd;
import MxCompiler.IR.IRnodes.virturalRegister;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class IRbuilder implements ASTVisitor {
    private basicBlock curBlock;
    private func curFunc;
    private boolean if_store;
    private Stack<basicBlock> breakLoopStack;
    private Stack<basicBlock> continueLoopStack;

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
        node.classMems.stream().forEachOrdered(this::visit);
    }

    @Override
    public void visit(funcDec node) {
        curFunc = new func();
        curFunc.setFuncType(node.functionType);
        funcMap.put(node.name, curFunc);
        for (varDec p : node.parameterList) {
            virturalRegister reg = new virturalRegister(p.name);
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
    public void visit(compoundStmt node) {
        node.stmtList.stream().forEachOrdered(this::visit);
    }

    @Override
    public void visit(breakStmt node) {
        basicBlock outBlock = breakLoopStack.peek();
        curBlock.pushBack(new jump(outBlock));

    }

    @Override
    public void visit(continueStmt node) {
        basicBlock nextBlock = continueLoopStack.peek();
        curBlock.pushBack(new jump(nextBlock));
    }

    @Override
    public void visit(exprStmt node) {
        visit(node.expression);
    }

    @Override
    public void visit(forloopStmt node) {
        basicBlock condBlock = new basicBlock("for_cond");
        basicBlock stepBlock = new basicBlock("for_step");
        basicBlock bodyBlock = new basicBlock("for_body");
        basicBlock endBlock = new basicBlock("for_end");
        if(node.cond != null)
            node.cond.setBlocks(bodyBlock,endBlock);
        visit(node.init);
        curBlock.pushBack(new jump(condBlock));
        curBlock = condBlock;
        visit(node.cond);
        curBlock = bodyBlock;
        continueLoopStack.push(stepBlock);
        breakLoopStack.push(endBlock);
        visit(node.forBody);
        continueLoopStack.pop();
        breakLoopStack.pop();
        curBlock.pushBack(new jump(stepBlock));
        curBlock = stepBlock;
        visit(node.step);
        curBlock.pushBack(new jump(condBlock));
        curBlock = endBlock;
    }

    @Override
    public void visit(ifStmt node) {
        basicBlock condBlock = new basicBlock("if_cond");
        basicBlock thenBlock = new basicBlock("if_then");
        basicBlock elseBlock = new basicBlock("if_else");
        basicBlock endBlock = new basicBlock("if_end");
        if(node.cond != null){
            node.cond.setBlocks(thenBlock, elseBlock);
        }
        curBlock = condBlock;
        visit(node.cond);
        curBlock = thenBlock;
        visit(node.ifBody);
        curBlock.pushBack(new jump(endBlock));
        curBlock = elseBlock;
        visit(node.elseBody);
        curBlock.pushBack(new jump(endBlock));
        curBlock = endBlock;
    }

    @Override
    public void visit(returnStmt node) {

    }

    @Override
    public void visit(varDecStmt node) {

    }

    @Override
    public void visit(whileloopStmt node) {
        basicBlock condBlock = new basicBlock("while_cond");
        basicBlock bodyBlock = new basicBlock("while_body");
        basicBlock endBlock = new basicBlock("while_end");
        if(node.cond != null){
            node.cond.setBlocks(bodyBlock,endBlock);
        }
        curBlock = condBlock;
        visit(node.cond);
        curBlock = bodyBlock;
        continueLoopStack.push(condBlock);
        breakLoopStack.push(endBlock);
        visit(node.whileBody);
        continueLoopStack.pop();
        breakLoopStack.pop();
        curBlock.pushBack(new jump(condBlock));
        curBlock = endBlock;
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
        visit(node.leftOperand);
        visit(node.rightOperand);
        binaryOpInstr instr = new binaryOpInstr();
        instr.operator = node.operator;
        instr.leftOperand = node.leftOperand.reg;
        instr.rightOperand = node.rightOperand.reg;
        node.reg = new virturalRegister();
        instr.result = (virturalRegister) node.reg;
        curBlock.pushBack(instr);
    }

    private void processStrArithmeticExpr(binaryExpr node){
        binaryOpInstr instr = new binaryOpInstr();
    }

    private void processComparisonExpr(binaryExpr node){
        visit(node.leftOperand);
        visit(node.rightOperand);
        cmp instr = new cmp();
        instr.operator = node.operator;
        instr.leftOperand = node.leftOperand.reg;
        instr.rightOperand = node.rightOperand.reg;
        node.reg = new virturalRegister();
        instr.result = (virturalRegister) node.reg;
        curBlock.pushBack(instr);
        if(node.jumpto != null) { // in condexpr
            branch condInstr = new branch();
            condInstr.operator = node.operator;
            condInstr.jumpto = node.jumpto;
            condInstr.jumpother = node.jumpother;
            curBlock.pushBack(condInstr);
        }
    }

    private void processStrComparisonExpr(binaryExpr node){

    }

    private void processAssign(binaryExpr node){
        boolean logicExpr = false;
        boolean memAccessExpr = false;
        if(node.rightOperand instanceof binaryExpr){
            binaryOp operator = ((binaryExpr)node.rightOperand).operator;
            if(operator.equals(binaryOp.LOGICAL_AND)||operator.equals(binaryOp.LOGICAL_OR))
                logicExpr = true;
        }
        else if(node.rightOperand instanceof unaryExpr){
            if(((unaryExpr)node.rightOperand).operator.equals(unaryOp.LOGIC_NOT))
                logicExpr = true;
        }
        if(logicExpr){
            node.rightOperand.setBlocks(new basicBlock(), new basicBlock());
        }
        visit(node.rightOperand);

        if(node.leftOperand instanceof indexAccessExpr || node.leftOperand instanceof fieldmemAccessExpr){
            memAccessExpr = true;
        }

        if_store = memAccessExpr;
        visit(node.leftOperand);
        if_store = false;

        if(node.rightOperand.jumpto != null) {// short-path-evaluation: 0_or_1_assign
            if (memAccessExpr) {
                node.rightOperand.jumpto.pushBack(new store(new intImd(1), node.leftOperand.reg, node.leftOperand.offset));
                node.rightOperand.jumpother.pushBack(new store(new intImd(0), node.leftOperand.reg, node.leftOperand.offset));
            } else {
                node.rightOperand.jumpto.pushBack(new move(new intImd(1), (virturalRegister) node.leftOperand.reg));
                node.rightOperand.jumpother.pushBack(new move(new intImd(0), (virturalRegister) node.leftOperand.reg));
            }
            basicBlock end = new basicBlock();
            node.rightOperand.jumpto.pushBack(new jump(end));
            node.rightOperand.jumpother.pushBack(new jump(end));
            curBlock = end;
        }
        else{// simple assign: reg to mem/reg
            if (memAccessExpr){
                curBlock.pushBack(new store(node.rightOperand.reg, node.leftOperand.reg, node.leftOperand.offset));
            }
            else{
                curBlock.pushBack(new move(node.rightOperand.reg, (virturalRegister) node.leftOperand.reg));
            }
        }

        //instr.destReg = node.leftOperand.reg;
    }

    private void processLogicalExpr(binaryExpr node){
        if(node.operator.equals(binaryOp.LOGICAL_AND)) {
            basicBlock nextBlock = new basicBlock("rhs_block");
            node.leftOperand.setBlocks(nextBlock,node.jumpother);
            visit(node.leftOperand);
            curBlock = nextBlock;
        }
        else {
            basicBlock nextBlock = new basicBlock("rhs_block");
            node.leftOperand.setBlocks(node.jumpto, nextBlock);
            visit(node.rightOperand);
            curBlock = nextBlock;
        }

        node.rightOperand.setBlocks(node.jumpto,node.jumpother);
        visit(node.rightOperand);
    }

    @Override
    public void visit(primaryExpr node) {
        if (node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(boolConstant node) {
        if(node.value){
            node.reg =  new intImd(1);
        }
        else{
            node.reg = new intImd(0);
        }
    }

    @Override
    public void visit(identifier node) {
        expr expression = null;
        if(node.ent instanceof varDec){
            expression = ((varDec) node.ent).variableExpression;

        }
        else if(node.ent instanceof globalVarDec){
            expression = ((globalVarDec) node.ent).variableExpression;
        }
        else if(node.ent instanceof varDecStmt){
            expression = ((varDecStmt) node.ent).variableExpr;
        }
        if(expression == null){
            node.reg = new virturalRegister();
        }
        else{
            node.reg = expression.reg;
        }
        //node.reg = ((varDec)node.ent).variableExpression.reg;
        if(node.jumpto != null){ // in logicalExpr
            branch instr = new branch();
            instr.jumpto = node.jumpto;
            instr.jumpother = node.jumpother;
            curBlock.pushBack(instr);
        }
    }

    @Override
    public void visit(intConstant node) {
        node.reg = new intImd(node.value.intValue());
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
        visit(node.index);
        visit(node.array);
        //curBlock.pushBack(new binaryOpInstr(binaryOp.MUL,node.index.reg,));
        //if(if_store) {

        //}
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
        virturalRegister reg;
        switch (node.operator){
            case LOGIC_NOT:
                node.operand.setBlocks(node.jumpto, node.jumpother);
                visit(node.operand);
                break;
            case NEG:
                visit(node.operand);
                reg = new virturalRegister();
                node.reg = reg;
                curBlock.pushBack(new unaryOpInstr(unaryOp.NEG, node.operand.reg, reg));
                break;
            case POS:
                visit(node.operand);
                node.reg = node.operand.reg;
                break;
            case DECREMENT:
                visit(node.operand);
                processDEC(node);
            case INCREMENT:
                visit(node.operand);
                processINC(node);
            case BITWISE_NOT:
                visit(node.operand);
                reg = new virturalRegister();
                node.reg = reg;
                curBlock.pushBack(new unaryOpInstr(unaryOp.BITWISE_NOT, node.operand.reg, reg));
                break;
        }

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

