package MxCompiler.X86Related;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.physicRegister;

public class x86Register extends physicRegister {
    private int num;

    public x86Register(int num, String name, boolean isCallerSave, boolean isCalleeSave, boolean isGeneral){
        super(name,isCallerSave,isCalleeSave,isGeneral);
        this.num = num;
    }

}
