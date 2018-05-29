package MxCompiler.BackEnd;

import MxCompiler.IR.IRnodes.*;
import MxCompiler.IR.IRnodes.instructions.funCall;
import MxCompiler.IR.IRnodes.instructions.instruction;
import MxCompiler.IR.IRnodes.instructions.load;
import MxCompiler.IR.IRnodes.instructions.store;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class registerAllocator {
    private Map<String, func> funcMap;
    private List<physicRegister> physicRegisterList;
    private Map<virturalRegister, physicRegister> allocateMap;
    private List<stackSlot> stackSlotList = new LinkedList<>();
    private Map<virturalRegister, stackSlot> slotMap = new HashMap<>();

    public registerAllocator(Map<String, func> funcMap, List<physicRegister> physicRegisterList){
        this.funcMap = funcMap;
        this.physicRegisterList = physicRegisterList;
    }

    public void runAllocator(){
        funcMap.values().stream().forEach(this::allocateRegister);
    }

    public void allocateRegister(func function){

        for(virturalRegister para : function.parameterList){
            stackSlot slot = new stackSlot();
            slotMap.put(para, slot);
            stackSlotList.add(slot);
        }

        List<basicBlock> order = function.preOrder;
        int cnt = 0;
        for(basicBlock curBlock : order){
            for(instruction instr = curBlock.getHead(); instr != null; instr = instr.getNext()){
                for (register usedReg : instr.usedRegister) {
                    if (usedReg instanceof virturalRegister) {
                        physicRegister allocatedReg = allocateMap.get(usedReg);
                        if (allocatedReg == null) {
                            allocatedReg = physicRegisterList.get(cnt++);
                            allocateMap.put((virturalRegister) usedReg, allocatedReg);
                            stackSlot slot = slotMap.get(usedReg);
                            if (slot != null) {
                                instr.linkPrev(new load(curBlock, slot, allocatedReg, 0, 8));
                            }
                        }
                        instr.resetUsedRegister(allocateMap);
                    }
                }

                register defReg = instr.getDefRegister();

                if(defReg instanceof virturalRegister) {
                    physicRegister allocatedReg = allocateMap.get(defReg);
                    if(allocatedReg == null){
                        allocatedReg = physicRegisterList.get(cnt++);
                        allocateMap.put((virturalRegister) defReg, allocatedReg);
                        stackSlot slot = new stackSlot();
                        slotMap.put((virturalRegister)defReg, slot);
                        stackSlotList.add(slot);
                        instr.linkNext(new store(curBlock, allocatedReg, slot, 0, 8));
                        instr = instr.getNext();
                    }
                    instr.setDefRegister(allocatedReg);
                }
            }
        }
    }
}
