package MxCompiler.IR.IRnodes;

import MxCompiler.IR.IRVisitor;

public class physicRegister extends register{
    private String name;
    private boolean isCallerSave;
    private boolean isCalleeSave;
    private boolean isGeneral;

    public physicRegister(){
        name = null;
        isCalleeSave = false;
        isCallerSave = false;
        isGeneral = false;
    }
    public physicRegister(String name, boolean isCallerSave, boolean isCalleeSave, boolean isGeneral){
        this.name = name;
        this.isCallerSave = isCallerSave;
        this.isCalleeSave = isCalleeSave;
        this.isGeneral = isGeneral;
    }

    public boolean isCallerSave(){
        return isCallerSave;
    }

    public boolean isCalleeSave(){
        return isCalleeSave;
    }

    public boolean isGeneral() {
        return isGeneral;
    }

    public String getName(){
        return name;
    }

    @Override
    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }
}
