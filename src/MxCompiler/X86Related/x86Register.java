package MxCompiler.X86Related;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.physicRegister;

public class x86Register extends physicRegister {

    public x86Register(String name, boolean isCallerSave, boolean isCalleeSave, boolean isGeneral){
        super(name, isCallerSave, isCalleeSave, isGeneral);
    }

    public void accept(IRVisitor visitor) {
        visitor.visit(this);
    }

}
