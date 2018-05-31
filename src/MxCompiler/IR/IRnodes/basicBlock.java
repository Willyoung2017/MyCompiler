package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRnodes.instructions.branch;
import MxCompiler.IR.IRnodes.instructions.instruction;
import MxCompiler.IR.IRnodes.instructions.jump;

import java.util.HashSet;
import java.util.Set;

public class basicBlock {
    private instruction head = null;
    private instruction last = null;
    private String name;
    private Set<basicBlock> pred = new HashSet<>();
    private Set<basicBlock> next = new HashSet<>();
    private boolean ended = false;
    public basicBlock(){
        name = null;
    }


    public basicBlock(String name){
        this.name = name;
    }

    public void remove(){
            if(pred != null) {
                for (basicBlock bb : pred) {
                    bb.next.remove(this);
                    for(instruction instr = bb.getLast(); instr != null; instr = instr.getPrev()){
                        if(instr instanceof jump && ((jump)instr).jumpto == this){
                            instr.remove();
                        }
                    }
                }
            }
            if(next != null) {
                for (basicBlock bb : next) {
                    bb.pred.remove(this);
                    for(instruction instr = bb.getLast(); instr != null; instr = instr.getPrev()){
                        if(instr instanceof jump && ((jump)instr).jumpto == this){
                            instr.remove();
                        }
                    }
                }
            }
    }

    public void pushBack(instruction next){
        if(ended) return;
        else {
            if(next instanceof jump || next instanceof branch){
                ended = true;
            }
            if (last == null) {
                head = last = next;
            } else {
                last.linkNext(next);
                last = next;
            }
        }
    }

    public void addNext(basicBlock nxt){
        if(nxt != null) {
            this.next.add(nxt);
            nxt.pred.add(this);
        }
    }

    public void setHead(instruction head){
        this.head = head;
    }

    public void setLast(instruction last){
        this.last = last;
    }

    public instruction getHead(){
        return head;
    }

    public instruction getLast(){
        return last;
    }

    public String getName(){
        return name;
    }

    public Set<basicBlock> getNext() {
        return next;
    }

    public Set<basicBlock> getPred() {
        return pred;
    }
}
