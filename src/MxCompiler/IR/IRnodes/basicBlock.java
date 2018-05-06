package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRnodes.instructions.instruction;

import java.util.HashSet;
import java.util.Set;

public class basicBlock {
    private instruction head;
    private instruction last;
    private String name;
    private Set<basicBlock> pred;
    private Set<basicBlock> next;

    public basicBlock(){
        head = null;
        last = null;
        name = null;
        pred = new HashSet<>();
        next = new HashSet<>();
    }

    public basicBlock(String name){
        this.name = name;
    }

    public void pushBack(instruction next){
        if(last == null){
            head = last = next;
        }
        else{
            last.linkNext(next);
            last = next;
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
