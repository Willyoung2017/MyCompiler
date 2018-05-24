package MxCompiler.IR.IRnodes;

public class staticData extends intValue{
    public int len;
    public String varName;

    public staticData(String varName, int len){
        this.varName = varName;
        this.len = len;
    }
}
