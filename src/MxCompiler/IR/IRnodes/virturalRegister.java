package MxCompiler.IR.IRnodes;

public class virturalRegister {
    private static int arrangedNum = 0;
    private int num;

    public void setNum(int value){
        num = value;
    }

    public virturalRegister arrangeNum(){
        num = arrangedNum;
        ++arrangedNum;
        return this;
    }
}
