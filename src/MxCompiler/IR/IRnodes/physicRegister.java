package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRVisitor;

public class physicRegister extends register{
    private String name;
    private boolean isCallerave;
    private boolean isCalleeSave;
    private boolean isGeneral;
    public physicRegister(){
        name = null;
        isGeneral = false;
        isCallerave = false;
        isCalleeSave = false;
    }
    public physicRegister(String name, boolean isCallerSave, boolean isCalleeSave, boolean isGeneral){
        this.name = name;
        this.isCalleeSave = isCalleeSave;
        this.isCallerave = isCallerSave;
        this.isGeneral = isGeneral;
    }

    public String getName(){
        return name;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
