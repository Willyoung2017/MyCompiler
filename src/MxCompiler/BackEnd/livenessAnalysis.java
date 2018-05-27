package MxCompiler.BackEnd;

import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.func;
import MxCompiler.IR.IRnodes.instructions.branch;
import MxCompiler.IR.IRnodes.instructions.instruction;
import MxCompiler.IR.IRnodes.instructions.jump;
import MxCompiler.IR.IRnodes.instructions.returnInstr;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virturalRegister;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class livenessAnalysis {
    private Map<String, func> funcMap;

    public livenessAnalysis(Map<String, func> funcMap){
        this.funcMap = funcMap;
    }

    private void livenessProcess(func function){
        List<basicBlock> reversePreOrder = function.getReversePreOrder();
        //init
        for(basicBlock bb : reversePreOrder){
            for(instruction instr = bb.getHead(); instr != null; instr = instr.getNext()){
                if(instr.liveIn != null){
                    instr.liveIn.clear();
                    instr.liveOut.clear();
                }
                else{
                    instr.liveIn = new HashSet<>();
                    instr.liveOut = new HashSet<>();
                }
            }
        }

        Set<virturalRegister> in = new HashSet<>();
        Set<virturalRegister> out = new HashSet<>();
        boolean changed = true;
        while(changed){
            changed = false;
            for(basicBlock bb :reversePreOrder){
                for(instruction instr = bb.getLast(); instr != null; instr = instr.getPrev()) {
                    in.clear();
                    out.clear();
                    in.addAll(instr.liveIn);
                    out.addAll(instr.liveOut);
                    instr.liveIn.clear();
                    instr.liveOut.clear();
                    /*
                    for (instruction successInstr) {
                        instr.liveOut.addAll(successInstr.liveIn);
                    }
                    */
                    if(instr instanceof branch){
                        instr.liveOut.addAll(((branch) instr).jumpto.getHead().liveIn);
                        instr.liveOut.addAll(((branch) instr).jumpother.getHead().liveIn);
                    }
                    else if(instr instanceof jump){
                        instr.liveOut.addAll(((jump)instr).jumpto.getHead().liveIn);
                    }
                    else if(!(instr instanceof returnInstr)){
                        assert instr.getNext() != null;
                        instr.liveOut.addAll(instr.getNext().liveIn);
                    }

                    for(register usedReg : instr.usedRegister) {
                        if(usedReg instanceof virturalRegister)
                            instr.liveIn.add((virturalRegister) usedReg);
                    }
                    instr.liveIn.addAll(instr.liveOut);
                    if(instr.defRegister instanceof virturalRegister){
                        instr.liveIn.remove(instr.defRegister);
                    }

                    if(!instr.liveIn.equals(in) || !instr.liveOut.equals(out)){
                        changed = true;
                    }
                }
            }
        }
    }

    public void runAnlysis(){
        funcMap.values().stream().forEach(this::livenessProcess);
    }
}

