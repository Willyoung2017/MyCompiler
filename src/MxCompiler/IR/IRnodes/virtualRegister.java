package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRVisitor;

public class virtualRegister extends register{
    //private static int arrangedNum = 0;
    //private int num;
    private String name;

    public virtualRegister(){
        name = null;
        //num = arrangedNum;
        //arrangedNum++;
    }

    public virtualRegister(String name){
        this.name = name;
        //num = arrangedNum;
        //arrangedNum++;
    }

    public String getName(){
        return name;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
