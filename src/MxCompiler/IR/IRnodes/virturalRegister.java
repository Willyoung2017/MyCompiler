package MxCompiler.IR.IRnodes;

public class virturalRegister extends register{
    private static int arrangedNum = 0;
    private int num;
    public String varName;

    public virturalRegister(){
        varName = null;
        num = arrangedNum;
        arrangedNum++;
    }

    public virturalRegister(String varName){
        this.varName = varName;
        num = arrangedNum;
        arrangedNum++;
    }

    public void setNum(int value){
        num = value;
    }

    public int getNum(){
        return num;
    }

}
