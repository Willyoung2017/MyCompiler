package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.physicRegister;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virturalRegister;

import java.util.*;

public abstract class instruction {
    private instruction prev;
    private instruction next;
    protected basicBlock itsBlock;

    //for liveness
    public List<register> usedRegister = new LinkedList<>();
    public Set<virturalRegister> liveIn = null;
    public Set<virturalRegister> liveOut = null;

    public instruction(){
        prev = null;
        next = null;
        itsBlock = null;
    }

    public instruction(basicBlock itsBlock, instruction prev, instruction next){
        this.prev = prev;
        this.next = next;
        this.itsBlock = itsBlock;
    }

    public void linkPrev(instruction instr){
        instr.prev= prev;
        instr.next = this;
        if(prev != null) prev.next = instr;
        prev = instr;
        if(this == itsBlock.getHead()) itsBlock.setHead(instr);
    }

    public void linkNext(instruction instr){
        instr.prev = this;
        instr.next = next;
        if(next != null) next.prev = instr;
        next = instr;
        if(this == itsBlock.getLast()) itsBlock.setLast(instr);

    }

    public void remove(){
        if(prev != null) prev.next = next;
        if(next != null) next.prev = prev;
        if(itsBlock != null && this == itsBlock.getHead()) itsBlock.setHead(next);
        if(itsBlock != null && this == itsBlock.getLast()) itsBlock.setLast(prev);
    }

    public void setItsBlock(basicBlock itsBlock) {
        this.itsBlock = itsBlock;
    }

    public basicBlock getItsBlock(){
        return  itsBlock;
    }

    public instruction getNext(){
        return next;
    }

    public instruction getPrev(){
        return prev;
    }

    public List<register> getUsedRegister(){
        return usedRegister;
    }

    public abstract register getDefRegister();
    public abstract void setUsedRegister();
    public abstract void resetUsedRegister(Map<virturalRegister, physicRegister> allocateMap);
    public abstract void setDefRegister(register reg);
    public abstract void accept(IRVisitor visitor);
}
