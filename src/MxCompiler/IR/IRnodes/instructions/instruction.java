package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.IR.IRnodes.basicBlock;

public abstract class instruction {
    private instruction prev;
    private instruction next;
    protected basicBlock itsBlock;

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
        prev = instr;
        instr.next = this;
    }

    public void linkNext(instruction instr){
        next = instr;
        instr.prev = this;
    }
}
