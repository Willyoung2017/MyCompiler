package MxCompiler.BackEnd;

import MxCompiler.IR.IRnodes.*;
import MxCompiler.IR.IRnodes.instructions.*;
import MxCompiler.X86Related.x86RegisterSet;

import java.util.*;

public class registerAllocator {
    private int count = 0;
    private Map<String, func> funcMap;
    private List<physicRegister> physicRegisterList;
    private Map<virturalRegister, physicRegister> allocateMap = new HashMap<>();
    private Map<virturalRegister, stackSlot> slotMap = new HashMap<>();
    public registerAllocator(Map<String, func> funcMap, List<physicRegister> physicRegisterList){
        this.funcMap = funcMap;
        this.physicRegisterList = physicRegisterList;
    }

    public void runAllocator(){
        funcMap.values().stream().forEach(this::allocateRegister);
    }

    private stackSlot getStackSlot(virturalRegister reg, func function){
        stackSlot slot = slotMap.get(reg);
        if(slot == null){
            if(reg.getName() == null){
                slot = new stackSlot("tmp"+(count++), function);
            }
            else {
                slot = new stackSlot(reg.getName(), function);
            }
            slotMap.put(reg, slot);
        }
        return slot;
    }

    public void allocateRegister(func function){
        slotMap.clear();
        int cnt = 0;
        for(register para : function.parameterList){
            stackSlot slot = new stackSlot(para.getName(), function);
            slotMap.put((virturalRegister) para, slot);
            function.parameterList.set(cnt++, slot);
        }

        List<basicBlock> order = function.getPreOrder();

        for(basicBlock curBlock : order){
            for(instruction instr = curBlock.getHead(); instr != null; instr = instr.getNext()){
                cnt = 0;
                Collection<register> used = instr.usedRegister;
                if(!used.isEmpty()) {
                    allocateMap.clear();
                    for (register usedReg : instr.usedRegister) {
                        if (usedReg instanceof virturalRegister) {
                            physicRegister allocatedReg = allocateMap.get(usedReg);
                            if (allocatedReg == null) {
                                allocatedReg = physicRegisterList.get(cnt++);
                                allocateMap.put((virturalRegister) usedReg, allocatedReg);
                            }
                            stackSlot slot = getStackSlot((virturalRegister) usedReg, function);
                            instr.linkPrev(new load(curBlock, slot, allocatedReg, 0, 8));
                            //instr.linkPrev(new move(curBlock, slot, allocatedReg));
                        }
                    }
                    instr.resetUsedRegister(allocateMap);
                }

                register defReg = instr.getDefRegister();

                if(defReg instanceof virturalRegister) {
                    physicRegister allocatedReg = allocateMap.get(defReg);
                    if(allocatedReg == null) {
                        allocatedReg = physicRegisterList.get(cnt++);
                        allocateMap.put((virturalRegister) defReg, allocatedReg);
                    }
                    stackSlot slot = getStackSlot((virturalRegister) defReg, function);
                    slotMap.put((virturalRegister)defReg, slot);
                    instr.linkNext(new store(curBlock, allocatedReg, slot, 0, 8));
                    //instr.linkNext(new move(curBlock, allocatedReg, slot));
                    instr.setDefRegister(allocatedReg);
                    instr = instr.getNext();
                }
            }
        }
    }
}
