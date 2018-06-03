package MxCompiler.BackEnd;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRnodes.*;
import MxCompiler.IR.IRnodes.instructions.*;
import MxCompiler.X86Related.funcInfo;
import MxCompiler.X86Related.x86RegisterSet;

import java.util.HashMap;
import java.util.Map;

import static MxCompiler.X86Related.x86RegisterSet.*;

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
        for(int i = 0; i < function.parameterList.size(); ++i){
            if(i <= 5) {
                //firstInstr.linkPrev(new store(firstBlock, x86RegisterSet.FuncParamRegs.get(i), function.parameterList.get(i), 0, 8));
                //need change to move for paraList is all reg
                firstInstr.linkPrev(new move(firstBlock, x86RegisterSet.FuncParamRegs.get(i), function.parameterList.get(i)));
            }
            else{//need add load from stackSlot to reg when i > 6 for paraList is all reg
                firstInstr.linkPrev(new load(firstBlock, function.paraSlotList.get(i-6), function.parameterList.get(i),0,8));
            }
        }
        lastInstr.linkPrev(new binaryOpInstr(lastBlock, binaryOp.ADD, x86RegisterSet.rsp, new intImd(info.totalSize), x86RegisterSet.rsp));
        lastInstr.linkPrev(new pop(lastBlock, x86RegisterSet.rbp));
        for(basicBlock bb : function.preOrder){
            for(instruction instr = bb.getHead(); instr != null; instr = instr.getNext()){
                if(instr instanceof funCall){
                    for(int i = 0; i < ((funCall)instr).parameters.size(); ++i){
                        if(i <= 5) {
                            instr.linkPrev(new move(bb, ((funCall)instr).parameters.get(i), x86RegisterSet.FuncParamRegs.get(i)));
                        }
                        else {
                            instr.linkPrev(new push(bb,  ((funCall)instr).parameters.get(((funCall)instr).parameters.size()-1-i+6)));
                            instr.linkNext(new pop(bb, x86RegisterSet.rax));
                        }
                    }
                    for(virtualRegister reg : instr.liveOut){
                        if(function.vrMap == null) continue;
                        if(function.vrMap.get(reg) == null) continue;
                        register toPush = function.vrMap.get(reg).color;
                        if(toPush == instr.getDefRegister()) continue;
                        if(toPush instanceof physicRegister){
                            instr.linkPrev(new push(bb, toPush));
                            instr.linkNext(new pop(bb, toPush));
                        }
                    }
                }
            }
        }
    }
}
