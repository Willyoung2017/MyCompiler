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
    private builtinFunc builtinFunction;
    private boolean isinClass;
    private boolean if_store;
    private classDec thisClass;
    private virturalRegister thisReg;
    private Stack<basicBlock> breakLoopStack;
    private Stack<basicBlock> continueLoopStack;
    private Map<String, staticString> stringMap;
    private List<staticData> dataList;
    private List<arrayType> dimList;
    private symbolTable symTable;
    public Map<String, func> funcMap;

    public IRbuilder(){
        isinClass = false;
        if_store = false;
        curBlock = null;
        curFunc = null;
        thisReg = null;
        builtinFunction = new builtinFunc();
        symTable = new symbolTable();
        breakLoopStack = new Stack<>();
        continueLoopStack = new Stack<>();
        dataList = new LinkedList<>();
        dimList = new LinkedList<>();
        funcMap = new HashMap<>();
        stringMap = new HashMap<>();
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
        isinClass = true;
        thisClass = node;
        symbol sym = new symbol();
        sym.setSymbol(node);
        symTable.classMap.put(node, sym);
        for(dec mem : node.classMems){
            if(mem instanceof funcDec){
                //Todo
                visit(mem);
            }
            else if (mem instanceof constructFuncDec){
                //Todo
                visit(mem);
            }
        }
        isinClass = false;
    }

    @Override
    public void visit(funcDec node) {
        curFunc = new func();
        curFunc.setFuncType(node.functionType);
        funcMap.put(node.name, curFunc);
        for (varDec p : node.parameterList) {
            virturalRegister reg = new virturalRegister(p.name);
            curFunc.parameterList.add(reg);
            p.nodeValue = reg; // important!!!
        }
        if(isinClass){
            thisReg = new virturalRegister("this");
            curFunc.parameterList.add(thisReg);
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
                thisBlock.addNext(lastBlock);
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
            else{
                curBlock.pushBack(new move(node.variableExpression.nodeValue, reg));
            }
        }
        else {
            //init
            curBlock.pushBack(new move(new intImd(0), reg));
        }
    }

    @Override
    public void visit(memberDec node) {
        if(node == null) return;
        node.accept(this);
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
        curBlock.addNext(outBlock);
    }

    @Override
    public void visit(continueStmt node) {
        basicBlock nextBlock = continueLoopStack.peek();
        curBlock.pushBack(new jump(nextBlock));
        curBlock.addNext(nextBlock);
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
        curBlock.addNext(condBlock);
        curBlock = condBlock;
        visit(node.cond);
        curBlock = bodyBlock;
        continueLoopStack.push(stepBlock);
        breakLoopStack.push(endBlock);
        visit(node.forBody);
        continueLoopStack.pop();
        breakLoopStack.pop();
        curBlock.pushBack(new jump(stepBlock));
        curBlock.addNext(stepBlock);
        curBlock = stepBlock;
        visit(node.step);
        curBlock.pushBack(new jump(condBlock));
        curBlock.addNext(condBlock);
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
        curBlock.addNext(endBlock);
        curBlock = elseBlock;
        visit(node.elseBody);
        curBlock.pushBack(new jump(endBlock));
        curBlock.addNext(endBlock);
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
                boolean tmp = if_store;
                if_store = false;
                processShortPathEva(node.returnExpr, reg, 0,node.returnExpr.type.size);
                if_store = tmp;
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
            else{
                curBlock.pushBack(new move(node.variableExpr.nodeValue, reg));
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
        curBlock.addNext(condBlock);
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
                else processStrExpr(node);
                break;

            case GEQ:
            case LEQ:
            case LESS:
            case GREATER:
            case EQUAL:
            case NOT_EQUAL:
                if(node.leftOperand.type instanceof intType)
                    processComparisonExpr(node);
                else processStrExpr(node);
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

    private void processStrExpr(binaryExpr node){
        visit(node.leftOperand);
        visit(node.rightOperand);
        virturalRegister reg = new virturalRegister();
        funCall call = null;
        switch(node.operator){
            case ADD:
                call = new funCall(reg, builtinFunction.builtinStringAdd);
                break;
            case EQUAL:
                call = new funCall(reg, builtinFunction.builtinStringEqual);
                break;
            case LEQ:
                call = new funCall(reg, builtinFunction.builtinStingLessEqual);
                break;
            case LESS:
                call = new funCall(reg, builtinFunction.builtinStringLess);
                break;
            case GEQ:
                call = new funCall(reg, builtinFunction.builtinStringGreaterEqual);
                break;
            case GREATER:
                call = new funCall(reg, builtinFunction.builtinStringGreater);
        }
        call.parameters.add(node.leftOperand.nodeValue);
        call.parameters.add(node.rightOperand.nodeValue);
        curBlock.pushBack(call);
        node.nodeValue = reg;

        if(node.jumpto != null){ // in condexpr
            branch condInstr = new branch();
            condInstr.operator = node.operator;
            condInstr.jumpto = node.jumpto;
            condInstr.jumpother = node.jumpother;
            curBlock.pushBack(condInstr);
            curBlock.addNext(condInstr.jumpto);
            curBlock.addNext(condInstr.jumpother);
        }

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
            curBlock.addNext(condInstr.jumpto);
            curBlock.addNext(condInstr.jumpother);
        }
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

        boolean tmp = if_store;
        if_store = memAccessExpr;
        visit(node.leftOperand);

        if(node.rightOperand.jumpto != null) {// short-path-evaluation: 0_or_1_assign
            if_store = memAccessExpr;
            processShortPathEva(node.rightOperand, node.leftOperand.nodeValue, node.leftOperand.offset, accessSize);
        }
        else{// simple assign: reg to mem/reg
            if (memAccessExpr){
                curBlock.pushBack(new store(node.rightOperand.nodeValue, node.leftOperand.addr, node.leftOperand.offset, accessSize));
            }
            else{
                curBlock.pushBack(new move(node.rightOperand.nodeValue, (virturalRegister) node.leftOperand.nodeValue));
            }
        }
        if_store = tmp;
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
            if(isinClass && node.nodeValue == null){ // memaccess in memFunc
                symbol sym = symTable.classMap.get(thisClass);
                int offset = sym.offsetMap.get(node.name);
                virturalRegister reg = new virturalRegister();
                curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, thisReg, new intImd(offset),reg));
                node.nodeValue = reg;
            }
        }
        else if(node.ent instanceof globalVarDec){
            node.nodeValue = ((globalVarDec)node.ent).nodeValue;
        }
        else if(node.ent instanceof varDecStmt){
            node.nodeValue = ((varDecStmt)node.ent).nodeValue;
        }
        else if (node.ent instanceof  classDec){ // mem this
            node.nodeValue = thisReg;
        }

        if(node.ent.type instanceof classType && !node.name.equals("this")){ // class shili name
            symbol sym = symTable.symbolMap.get(node.ent.type.name);
            symTable.symbolMap.putIfAbsent(node.name, sym);
        }


        if(node.jumpto != null){ // in logicalExpr
            branch instr = new branch();
            instr.operator = binaryOp.EQUAL;
            instr.jumpto = node.jumpto;
            instr.jumpother = node.jumpother;
            curBlock.pushBack(instr);
            curBlock.addNext(instr.jumpto);
            curBlock.addNext(instr.jumpother);
        }
    }

    @Override
    public void visit(intConstant node) {
        node.nodeValue = new intImd(node.value.intValue());
    }

    private virturalRegister processIndexList(){
        int offset = 0;
        virturalRegister addr = new virturalRegister();
        for(arrayType dim : dimList){

            curBlock.pushBack(new store(dim.index.nodeValue, addr,offset*8,8));
            ++offset;
        }
        return addr;
    }

    private void newArray(int order, virturalRegister reg){
        int dim = dimList.size();
        arrayType node = dimList.get(order);
        typ baseType = dimList.get(dim-1-order).baseType;
        expr index = node.index;
        /*
         int[][][] a = new int[3][4][5]
         convert  to:

         int[][][] a = new int[3][][];

         for int i = 0; i < 3; ++i
             a[i] = new int[4][];
             for int j = 0; j < 4; ++j
                 a[i][j] = new int[5]

        needed_order:
        indexList: 3, 4, 5
        typeList: int[3][4][5].baseType, int[3][4].baseType, int[3].baseType
        actual_order:
        dimList: arrayNode int[3], arrayNode int[3][4], arrayNode int[3][4][5]

        for int i = 0; i < prv_order_dim; ++i
           prv_order_type[i] = new order_type[order_dim]
        */
        if(order == 0){
            curBlock.pushBack(new binaryOpInstr(binaryOp.MUL, index.nodeValue, new intImd(baseType.size), reg));
            //allocate space for storing size
            curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, reg, new intImd(8), reg));
            curBlock.pushBack(new heapAllocate(reg, reg));
            curBlock.pushBack(new store(index.nodeValue, reg, 0, 8));
            newArray(order + 1, reg);
        }
        else {
            if(order == dim) return;

            arrayType prvNode = dimList.get(order - 1);
            expr prvIndex = prvNode.index;
            typ prvBaseType = dimList.get(dim-order).baseType;
            basicBlock condBlock = new basicBlock("for_cond");
            basicBlock stepBlock = new basicBlock("for_step");
            basicBlock bodyBlock = new basicBlock("for_body");
            basicBlock endBlock = new basicBlock("for_end");

            //init
            virturalRegister init_reg = new virturalRegister("init_i");
            curBlock.pushBack(new move(new intImd(0), init_reg));
            curBlock.pushBack(new jump(condBlock));
            curBlock.addNext(condBlock);

            //cond
            curBlock = condBlock;
            virturalRegister cmp_reg = new virturalRegister("cond_result");
            curBlock.pushBack(new cmp(binaryOp.LESS, init_reg, prvIndex.nodeValue,cmp_reg));
            curBlock.pushBack(new branch(binaryOp.LESS,bodyBlock, endBlock));
            curBlock.addNext(bodyBlock);
            curBlock.addNext(endBlock);

            //body
            curBlock = bodyBlock;
            virturalRegister leftAddrReg = new virturalRegister();
            curBlock.pushBack(new binaryOpInstr(binaryOp.MUL, init_reg, new intImd(prvBaseType.size),leftAddrReg));
            curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, leftAddrReg, reg, leftAddrReg)); //reg is the baseAddr of the prvNewArray
                                                                                               // & leftAddrReg + offset(8) is the place to store the addr of newArray
            virturalRegister rightAddrReg= new virturalRegister();
            curBlock.pushBack(new binaryOpInstr(binaryOp.MUL, index.nodeValue, new intImd(baseType.size), rightAddrReg));
            //allocate space for storing size
            curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, reg, new intImd(8), rightAddrReg));
            curBlock.pushBack(new heapAllocate(rightAddrReg, rightAddrReg)); //rightAddrReg is baseAddr of the NewArray
            curBlock.pushBack(new store(index.nodeValue, rightAddrReg, 0, 8)); //store size in rightAddrReg
            curBlock.pushBack(new store(rightAddrReg, leftAddrReg, 8, 8));
            curBlock.pushBack(new jump(stepBlock));
            curBlock.addNext(stepBlock);

            //step
            curBlock = stepBlock;
            curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, init_reg, new intImd(1), init_reg));
            curBlock.pushBack(new jump(condBlock));
            curBlock.addNext(condBlock);
            curBlock = endBlock;

            //end
            newArray(order + 1, rightAddrReg);

        }
    }


    @Override
    public void visit(newExpr node) {
        virturalRegister reg = new virturalRegister();
        if(node.type instanceof classType){
            symbol sym = symTable.symbolMap.get(node.type.name);
            int classSize = sym.size;
            curBlock.pushBack(new heapAllocate(reg, new intImd(classSize)));
        }
        else if(node.type instanceof arrayType){
            //Todo
            //consider it as a builtin func
            dimList.clear();
            visit(node.type);
            newArray(0, reg);
            //int dim;
            typ rootType = ((arrayType) node.type).rootType;
            /*funCall call = new funCall(reg, builtinFunction.buildinNewArray);
            call.parameters.add(new intImd(rootType.size)); //registerSize
            call.parameters.add(new intImd(dim)); //dim
            virturalRegister indexBase = processIndexList();
            call.parameters.add(indexBase);//indexList

            expr index = ((arrayType) node.type).index;
            typ baseType = ((arrayType) node.type).baseType;
            curBlock.pushBack(new binaryOpInstr(binaryOp.MUL, index.nodeValue, new intImd(baseType.size), reg));
            //allocate space for storing size
            curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, reg, new intImd(8), reg));
            curBlock.pushBack(new heapAllocate(reg, reg));
            curBlock.pushBack(new store(index.nodeValue, reg, 0, 8));
            */
        }
        node.nodeValue = reg;
    }

    @Override
    public void visit(Null node) {
        node.nodeValue = new intImd(0);
    }

    @Override
    public void visit(stringConstant node) {
        staticString str = stringMap.get(node.value);
        if(str == null){
            str = new staticString(node.value);
            stringMap.put(node.value, str);
        }
        node.nodeValue = str;
    }

    private void processBuiltinMemFunc(fieldfuncAccessExpr node){
        //Question remains
        virturalRegister reg = null;
        if(node.name.equals("size") || node.name.equals("length")){
            //Question remains
            reg = new virturalRegister("size");
            curBlock.pushBack(new load(node.obj.nodeValue, reg,0,8));
        }
        else if(node.name.equals("substring")){
            reg = new virturalRegister("substring");
            funCall call = new funCall(reg, builtinFunction.builtinSubstring);
            call.parameters.add(node.obj.nodeValue);
            call.parameters.add(node.parameters.get(0).nodeValue);
            call.parameters.add(node.parameters.get(1).nodeValue);
            curBlock.pushBack(call);
        }
        else if(node.name.equals("parseInt")){
            reg = new virturalRegister("parseInt");
            funCall call = new funCall(reg, builtinFunction.builtinParseInt);
            call.parameters.add(node.obj.nodeValue);
            curBlock.pushBack(call);
        }
        else if(node.name.equals("ord")){
            //Question remains
            reg = new virturalRegister("ord");
            curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, node.obj.nodeValue, node.parameters.get(0).nodeValue, reg));
            curBlock.pushBack(new load(reg,reg, 8,1));
        }
        node.nodeValue = reg;
    }

    @Override
    public void visit(fieldfuncAccessExpr node) {
        visit(node.obj);
        node.parameters.stream().forEachOrdered(this::visit);
        if(node.name.equals("size")||node.name.equals("length")||node.name.equals("substring")||node.name.equals("parseInt")||node.name.equals("ord")){
            processBuiltinMemFunc(node);
            return;
        }
        func function = funcMap.get(node.name);
        virturalRegister reg = new virturalRegister();
        funCall call = new funCall(reg,function);
        for(expr p : node.parameters) {
            call.parameters.add(p.nodeValue);
        }
        call.parameters.add(node.obj.nodeValue);
        curBlock.pushBack(call);

        if(node.jumpto != null){//bool rettype
            branch instr = new branch();
            instr.operator = binaryOp.EQUAL;
            instr.jumpto = node.jumpto;
            instr.jumpother = node.jumpother;
            curBlock.pushBack(instr);
            curBlock.addNext(instr.jumpto);
            curBlock.addNext(instr.jumpother);
        }
        node.nodeValue = reg;
    }

    @Override
    public void visit(fieldmemAccessExpr node) {
        boolean tmp = if_store;
        if_store = false;
        visit(node.obj);
        if_store = tmp;

        intValue addr = node.obj.nodeValue;
        symbol sym = symTable.symbolMap.get(node.obj.type.name);
        int offset = sym.offsetMap.get(node.name);
        int accessSize = sym.sizeMap.get(node.name);
        if(if_store){ // if store give addr & offset
            node.addr = addr;
            node.offset = offset;
        }
        else{
            virturalRegister reg = new virturalRegister();
            curBlock.pushBack(new load(addr, reg, offset, accessSize));
            if(node.jumpto != null){
                curBlock.pushBack(new branch(binaryOp.EQUAL, node.jumpto, node.jumpother));
                curBlock.addNext(node.jumpto);
                curBlock.addNext(node.jumpother);
            }
            node.nodeValue = reg;
        }

    }

    private void processPrint(expr para, boolean println){
        //Todo
        if(para instanceof binaryExpr){

        }
        else if(para instanceof funcCall && ((funcCall)para).obj.name.equals("toString")){

        }
        else{

        }
    }
    private void processBuiltinFunc(funcCall node){
        virturalRegister reg = null;
        if(node.name.equals("getInt")){
            reg = new virturalRegister("getInt");
            funCall call = new funCall(reg, builtinFunction.builtinGetInt);
            curBlock.pushBack(call);
        }
        else if(node.name.equals("getString")){
            reg = new virturalRegister("getString");
            funCall call = new funCall(reg, builtinFunction.builtinGetString);
            curBlock.pushBack(call);
        }
        else if(node.name.equals("print")){
            funCall call = new funCall(null, builtinFunction.builtinPrintString);
            call.parameters.add(node.parameters.get(0).nodeValue);
            curBlock.pushBack(call);
        }
        else if(node.name.equals("println")){
            funCall call = new funCall(null, builtinFunction.builtinPrintlnString);
            call.parameters.add(node.parameters.get(0).nodeValue);
            curBlock.pushBack(call);
        }
        else if(node.name.equals("toString")){
            reg = new virturalRegister("toString");
            funCall call = new funCall(reg, builtinFunction.builtintoString);
            call.parameters.add(node.parameters.get(0).nodeValue);
            curBlock.pushBack(call);
        }
        node.nodeValue = reg;
    }

    @Override
    public void visit(funcCall node) {
        node.parameters.stream().forEachOrdered(this::visit);
        if (node.obj.name.equals("getInt")||node.obj.name.equals("print") || node.obj.name.equals("println")||node.obj.name.equals("toString")||node.obj.name.equals("getString")){
            processBuiltinFunc(node);
            return;
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
            instr.operator = binaryOp.EQUAL;
            instr.jumpto = node.jumpto;
            instr.jumpother = node.jumpother;
            curBlock.pushBack(instr);
            curBlock.addNext(instr.jumpto);
            curBlock.addNext(instr.jumpother);
        }
        node.nodeValue = reg;
    }

    @Override
    public void visit(indexAccessExpr node) { // Question remains about offset
        boolean tmp = if_store;
        int sizeofInt = 8;
        if_store = false;
        visit(node.index);
        visit(node.array);
        if_store = tmp;
        int size = node.array.type.size;
        virturalRegister reg = new virturalRegister();
        curBlock.pushBack(new binaryOpInstr(binaryOp.MUL, node.index.nodeValue, new intImd(size), reg));
        curBlock.pushBack(new binaryOpInstr(binaryOp.ADD, node.array.nodeValue, reg, reg)); // address saved in reg
        if(if_store) {
            node.addr = reg;
            node.offset = sizeofInt; // consider baseAddr has stored size of array;
        }
        else{
            curBlock.pushBack(new load(reg, reg, sizeofInt, size));
            node.nodeValue = reg;
        }

        if(node.jumpto != null){
            branch instr = new branch();
            instr.operator = binaryOp.EQUAL;
            instr.jumpto = node.jumpto;
            instr.jumpother = node.jumpother;
            curBlock.pushBack(instr);
            curBlock.addNext(instr.jumpto);
            curBlock.addNext(instr.jumpother);
        }
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
        node.jumpto.addNext(end);
        node.jumpother.pushBack(new jump(end));
        node.jumpother.addNext(end);
        curBlock = end;
    }

    @Override
    public void visit(typ node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(arrayType node) {
        visit(node.baseType);
        visit(node.index);
        dimList.add(node);
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

