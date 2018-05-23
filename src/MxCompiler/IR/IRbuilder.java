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
import MxCompiler.Exception.semanticException;
import MxCompiler.IR.IRnodes.*;
import MxCompiler.IR.IRnodes.instructions.*;

import java.math.BigInteger;
import java.util.*;

public class IRbuilder implements ASTVisitor {
    private basicBlock curBlock;
    private func curFunc;
    private boolean if_store;
    private Stack<basicBlock> breakLoopStack;
    private Stack<basicBlock> continueLoopStack;
    private List<staticData> dataList;
    public Map<String, func> funcMap;

    public IRbuilder(){
        curBlock = null;
        curFunc = null;
        breakLoopStack = new Stack<>();
        continueLoopStack = new Stack<>();
        dataList = new LinkedList<>();
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
        //node.parameterList.stream().forEachOrdered(this::visit);
        basicBlock startBlock = new basicBlock(node.name);
        curBlock = startBlock;
        curFunc.setFirstBlock(startBlock);
        visit(node.functionStmt);
        if(curFunc.returnInstrList.size() == 0){
            if(node.functionType instanceof voidType){
                returnInstr ret = new returnInstr(null);
                curBlock.pushBack(ret);
                curFunc.returnInstrList.add(ret);
            }
            else {
                returnInstr ret = new returnInstr(new intImd(0));
                curBlock.pushBack(ret);
                curFunc.returnInstrList.add(ret);
            }
        }

        if (curFunc.returnInstrList.size() > 1){
            basicBlock lastBlock = new basicBlock(node.name+"_last");
            virturalRegister newRetReg = new virturalRegister("ret");
            for(returnInstr ret : curFunc.returnInstrList){
                basicBlock thisBlock = ret.getItsBlock();
                if(ret.retReg != null){
                    ret.linkPrev(new move(ret.retReg, newRetReg));
                }
                ret.remove();
                thisBlock.pushBack(new jump(lastBlock));
            }
            lastBlock.pushBack(new returnInstr(newRetReg));
            curFunc.setLastBlock(lastBlock);
        }
        else {
            curFunc.setLastBlock(curFunc.returnInstrList.get(0).getItsBlock());
        }
        curFunc = null;
    }


    @Override
    public void visit(globalVarDec node) {
        staticData data = new staticData(node.name, node.type.size);
        node.nodeValue = data;
        dataList.add(data);
    }

    @Override
    public void visit(constructFuncDec node) {

    }

    @Override
    public void visit(varDec node) {
        virturalRegister reg = new virturalRegister(node.name);
        node.nodeValue = reg;
        if (node.variableExpression != null){
            boolean logicExpr = false;
            if(node.variableExpression instanceof binaryExpr){
                binaryOp operator = ((binaryExpr)node.variableExpression).operator;
                if(operator.equals(binaryOp.LOGICAL_AND)||operator.equals(binaryOp.LOGICAL_OR))
                    logicExpr = true;
            }
            else if(node.variableExpression instanceof unaryExpr){
                if(((unaryExpr)node.variableExpression).operator.equals(unaryOp.LOGIC_NOT))
                    logicExpr = true;
            }

            if(logicExpr) {
                node.variableExpression.setBlocks(new basicBlock(), new basicBlock());
            }

            visit(node.variableExpression);
            if(logicExpr) {
                boolean tmp = if_store;
                if_store = false;
                processShortPathEva(node.variableExpression, reg, 0, node.variableExpression.type.size);
                if_store = tmp;
            }
        }
        else {
            //init
            curBlock.pushBack(new move(new intImd(0), reg));
        }
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
        if(node.returnExpr.type instanceof voidType){
            curBlock.pushBack(new returnInstr(null));
        }
        else {
            boolean logicExpr = false;
            if(node.returnExpr instanceof binaryExpr){
                binaryOp operator = ((binaryExpr)node.returnExpr).operator;
                if(operator.equals(binaryOp.LOGICAL_AND)||operator.equals(binaryOp.LOGICAL_OR))
                    logicExpr = true;
            }
            else if(node.returnExpr instanceof unaryExpr){
                if(((unaryExpr)node.returnExpr).operator.equals(unaryOp.LOGIC_NOT))
                    logicExpr = true;
            }
            if(logicExpr){
                node.returnExpr.setBlocks(new basicBlock(), new basicBlock());
                visit(node.returnExpr);
                virturalRegister reg = new virturalRegister("ret");
                node.returnExpr.jumpto.pushBack(new move(new intImd(1), reg));
                node.returnExpr.jumpother.pushBack(new move(new intImd(0), reg));
                basicBlock end = new basicBlock();
                node.returnExpr.jumpto.pushBack(new jump(end));
                node.returnExpr.jumpother.pushBack(new jump(end));
                curBlock = end;
                returnInstr ret = new returnInstr(reg);
                ret.setItsBlock(curBlock);
                curBlock.pushBack(ret);
                curFunc.returnInstrList.add(ret);
            }
            else {
                visit(node.returnExpr);
                returnInstr ret = new returnInstr(node.returnExpr.nodeValue);
                ret.setItsBlock(curBlock);
                curBlock.pushBack(ret);
                curFunc.returnInstrList.add(ret);
            }
        }
    }

    @Override
    public void visit(varDecStmt node) {
        virturalRegister reg = new virturalRegister(node.name);
        node.nodeValue = reg;

        if (node.variableExpr != null){
            boolean logicExpr = false;
            if(node.variableExpr instanceof binaryExpr){
                binaryOp operator = ((binaryExpr)node.variableExpr).operator;
                if(operator.equals(binaryOp.LOGICAL_AND)||operator.equals(binaryOp.LOGICAL_OR))
                    logicExpr = true;
            }
            else if(node.variableExpr instanceof unaryExpr){
                if(((unaryExpr)node.variableExpr).operator.equals(unaryOp.LOGIC_NOT))
                    logicExpr = true;
            }

            if(logicExpr) {
                node.variableExpr.setBlocks(new basicBlock(), new basicBlock());
            }

            visit(node.variableExpr);
            if(logicExpr) {
                boolean tmp = if_store;
                if_store = false;
                processShortPathEva(node.variableExpr, reg, 0, node.variableExpr.type.size);
                if_store = tmp;
            }
        }
        else {
            //init
            curBlock.pushBack(new move(new intImd(0), reg));
        }
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
        instr.leftOperand = node.leftOperand.nodeValue;
        instr.rightOperand = node.rightOperand.nodeValue;
        node.nodeValue = new virturalRegister();
        instr.result = (virturalRegister) node.nodeValue;
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
        instr.leftOperand = node.leftOperand.nodeValue;
        instr.rightOperand = node.rightOperand.nodeValue;
        node.nodeValue = new virturalRegister();
        instr.result = (virturalRegister) node.nodeValue;
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
        int accessSize = node.rightOperand.type.size;
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
                node.rightOperand.jumpto.pushBack(new store(new intImd(1), node.leftOperand.addr, node.leftOperand.offset, accessSize));
                node.rightOperand.jumpother.pushBack(new store(new intImd(0), node.leftOperand.addr, node.leftOperand.offset, accessSize));
            } else {
                node.rightOperand.jumpto.pushBack(new move(new intImd(1), (virturalRegister) node.leftOperand.nodeValue));
                node.rightOperand.jumpother.pushBack(new move(new intImd(0), (virturalRegister) node.leftOperand.nodeValue));
            }
            basicBlock end = new basicBlock();
            node.rightOperand.jumpto.pushBack(new jump(end));
            node.rightOperand.jumpother.pushBack(new jump(end));
            curBlock = end;
        }
        else{// simple assign: reg to mem/reg
            if (memAccessExpr){
                curBlock.pushBack(new store(node.rightOperand.nodeValue, node.leftOperand.addr, node.leftOperand.offset, accessSize));
            }
            else{
                curBlock.pushBack(new move(node.rightOperand.nodeValue, (virturalRegister) node.leftOperand.nodeValue));
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
            node.nodeValue =  new intImd(1);
        }
        else{
            node.nodeValue = new intImd(0);
        }
    }

    @Override
    public void visit(identifier node) {
         //Question remains
        if(node.ent instanceof varDec){
            node.nodeValue = ((varDec) node.ent).nodeValue;

        }
        else if(node.ent instanceof globalVarDec){
            node.nodeValue = ((globalVarDec)node.ent).nodeValue;
        }
        else if(node.ent instanceof varDecStmt){
            node.nodeValue = ((varDecStmt)node.ent).nodeValue;
        }

        if(node.jumpto != null){ // in logicalExpr
            branch instr = new branch();
            instr.jumpto = node.jumpto;
            instr.jumpother = node.jumpother;
            curBlock.pushBack(instr);
        }
    }

    @Override
    public void visit(intConstant node) {
        node.nodeValue = new intImd(node.value.intValue());
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
    private void processBuiltinFunc(funcCall node){
        //To be done
    }
    @Override
    public void visit(funcCall node) {
        node.parameters.stream().forEachOrdered(this::visit);
        if (node.obj.name.equals("getInt")||node.obj.name.equals("print") || node.obj.name.equals("println")||node.obj.name.equals("toString")||node.obj.name.equals("getString")){
            processBuiltinFunc(node);
        }

        func function = funcMap.get(node.name);
        virturalRegister reg = new virturalRegister();
        funCall call = new funCall(reg,function);
        for(expr p : node.parameters) {
            call.parameters.add(p.nodeValue);
        }
        curBlock.pushBack(call);
        if(node.jumpto != null){//bool rettype
            branch instr = new branch();
            instr.jumpto = node.jumpto;
            instr.jumpother = node.jumpother;
            curBlock.pushBack(instr);
        }
    }

    @Override
    public void visit(indexAccessExpr node) { // Question remains about offset
        boolean tmp = if_store;
        if_store = false;
        visit(node.index);
        visit(node.array);
        if_store = tmp;
        int size = node.array.type.size;
        virturalRegister reg = new virturalRegister();
        curBlock.pushBack(new binaryOpInstr(binaryOp.MUL, node.index.nodeValue, new intImd(size), reg));
        curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, node.array.nodeValue, reg, reg)); // address saved in reg
        //curBlock.pushBack(new binaryOpInstr(binaryOp.MUL,node.index.reg,));
        if(if_store) {
            node.addr = reg;
            node.offset = 0;
        }
        else{
            curBlock.pushBack(new load(reg, reg, 0,size));
        }

        if(node.jumpto != null){
            branch instr = new branch();
            instr.jumpto = node.jumpto;
            instr.jumpother = node.jumpother;
            curBlock.pushBack(instr);
        }
        node.nodeValue = reg;
    }

    @Override
    public void visit(selfDec node) {
        boolean memAccess = false;
        if(node.operand instanceof indexAccessExpr || node.operand instanceof fieldmemAccessExpr){
            memAccess = true;
        }
        boolean tmp;
        tmp = if_store;
        //need store and load together in selfDec;
        if_store = memAccess;
        visit(node.operand);
        if_store  = false;
        visit(node.operand);
        if_store = tmp;
        virturalRegister oldValue = new virturalRegister();
        curBlock.pushBack(new move(node.operand.nodeValue,oldValue));
        node.nodeValue = oldValue;
        if (memAccess) {
            virturalRegister reg = new virturalRegister();
            curBlock.pushBack(new binaryOpInstr(binaryOp.SUB, node.operand.nodeValue, new intImd(1), reg));
            curBlock.pushBack(new store(reg, node.operand.addr, node.operand.offset, node.operand.type.size));
        }
        else {
            curBlock.pushBack(new binaryOpInstr(binaryOp.SUB, node.operand.nodeValue, new intImd(1), (virturalRegister) node.operand.nodeValue));
        }
    }

    @Override
    public void visit(selfInc node) {
        boolean memAccess = false;
        if(node.operand instanceof indexAccessExpr || node.operand instanceof fieldmemAccessExpr){
            memAccess = true;
        }
        boolean tmp;
        tmp = if_store;
        if_store = memAccess;
        visit(node.operand);
        if_store  = false;
        visit(node.operand);
        if_store = tmp;
        virturalRegister oldValue = new virturalRegister();
        curBlock.pushBack(new move(node.operand.nodeValue,oldValue));
        node.nodeValue = oldValue;
        if (memAccess) {
            virturalRegister reg = new virturalRegister();
            curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, node.operand.nodeValue, new intImd(1), reg));
            curBlock.pushBack(new store(reg, node.operand.addr, node.operand.offset, node.operand.type.size));
        }
        else {
            curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, node.operand.nodeValue, new intImd(1), (virturalRegister) node.operand.nodeValue));
        }
    }

    @Override
    public void visit(suffixExpr node) {
        if(node == null) return;
        node.accept(this);
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
                node.nodeValue = reg;
                curBlock.pushBack(new unaryOpInstr(unaryOp.NEG, node.operand.nodeValue, reg));
                break;
            case POS:
                visit(node.operand);
                node.nodeValue = node.operand.nodeValue;
                break;
            case DECREMENT:
                processDEC(node);
            case INCREMENT:
                processINC(node);
            case BITWISE_NOT:
                visit(node.operand);
                reg = new virturalRegister();
                node.nodeValue = reg;
                curBlock.pushBack(new unaryOpInstr(unaryOp.BITWISE_NOT, node.operand.nodeValue, reg));
                break;
        }

    }

    private void processDEC(unaryExpr node) {
        boolean memAccess = false;
        if(node.operand instanceof indexAccessExpr || node.operand instanceof fieldmemAccessExpr){
            memAccess = true;
        }
        boolean tmp;
        tmp = if_store;
        if_store = memAccess;
        visit(node.operand);
        if_store  = false;
        visit(node.operand);
        if_store = tmp;
        if (memAccess) {
            virturalRegister reg = new virturalRegister();
            curBlock.pushBack(new binaryOpInstr(binaryOp.SUB, node.operand.nodeValue, new intImd(1), reg));
            curBlock.pushBack(new store(reg, node.operand.addr, node.operand.offset, node.operand.type.size));
            node.nodeValue = reg;
        }
        else {
            curBlock.pushBack(new binaryOpInstr(binaryOp.SUB, node.operand.nodeValue, new intImd(1), (virturalRegister) node.operand.nodeValue));
            node.nodeValue = node.operand.nodeValue;
        }
    }

    private void processINC(unaryExpr node){
        boolean memAccess = false;
        if(node.operand instanceof indexAccessExpr || node.operand instanceof fieldmemAccessExpr){
            memAccess = true;
        }
        boolean tmp;
        tmp = if_store;
        if_store = memAccess;
        visit(node.operand);
        if_store  = false;
        visit(node.operand);
        if_store = tmp;
        if (memAccess) {
            virturalRegister reg = new virturalRegister();
            curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, node.operand.nodeValue, new intImd(1), reg));
            curBlock.pushBack(new store(reg, node.operand.addr, node.operand.offset, node.operand.type.size));
            node.nodeValue = reg;
        }
        else {
            curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, node.operand.nodeValue, new intImd(1), (virturalRegister) node.operand.nodeValue));
            node.nodeValue = node.operand.nodeValue;
        }
    }
    private void processShortPathEva(expr node, intValue nodeValue, int offset, int accessSize){
        if(if_store){
            node.jumpto.pushBack(new store(new intImd(1), nodeValue, offset, accessSize));
            node.jumpother.pushBack(new store(new intImd(0), nodeValue, offset, accessSize));
        }
        else{
            node.jumpto.pushBack(new move(new intImd(1), (virturalRegister)nodeValue));
            node.jumpother.pushBack(new move(new intImd(0), (virturalRegister)nodeValue));
        }
        basicBlock end  = new basicBlock();
        node.jumpto.pushBack(new jump(end));
        node.jumpother.pushBack(new jump(end));
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

