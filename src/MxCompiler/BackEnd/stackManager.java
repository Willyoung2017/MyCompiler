package MxCompiler.BackEnd;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.func;
import MxCompiler.IR.IRnodes.instructions.binaryOpInstr;
import MxCompiler.IR.IRnodes.instructions.instruction;
import MxCompiler.IR.IRnodes.instructions.pop;
import MxCompiler.IR.IRnodes.intImd;
import MxCompiler.X86Related.funcInfo;
import MxCompiler.X86Related.x86RegisterSet;

import java.util.Map;

public class stackManager {
    private Map<String, func> funcMap;


    /*
    --------------caller---------------------------------
        ........
        CALLER_SAVE
        rbp + Y      nth_para - lastParaAddr
        ........
        rbp + 16     7th_para
        rbp + 8      return_addr
    --------------caller----------------------------------
        call

        rbp          old_ebp value : the rsp of callee i.e. push rbp

    --------------callee----------------------------------
        rbp - 8     1st_para - firstParaAddr
        ........
        rbp - 48    6th_para
        CALLEE_SAVE
        LOCAL_VARS
        ........
        rbp - X     rsp - current stack pointer
    --------------callee-----------------------------------
    */

    public stackManager(Map<String, func> funcMap){
        this.funcMap = funcMap;
    }

    public void runManager(){
        funcMap.values().stream().forEach(this::manageFunc);
    }

    private void manageFunc(func function){
        funcInfo info = new funcInfo(function);
        info.setOffset();
        info.setoffsetMap();

        basicBlock firstBlock = function.getFirstBlock();
        basicBlock lastBlock = function.getLasstBlock();
        instruction firstInstr = firstBlock.getHead();
        instruction lastInstr = lastBlock.getLast();
        firstInstr.linkPrev(new binaryOpInstr(firstBlock, binaryOp.SUB, x86RegisterSet.rsp, new intImd(info.totalSize), x86RegisterSet.rsp));
        lastInstr.linkPrev(new pop(lastBlock, x86RegisterSet.rbp));
        lastInstr.linkPrev(new binaryOpInstr(lastBlock, binaryOp.ADD, x86RegisterSet.rsp, new intImd(info.totalSize), x86RegisterSet.rsp));

    }
}
