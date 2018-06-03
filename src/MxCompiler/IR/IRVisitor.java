package MxCompiler.IR;

import MxCompiler.IR.IRnodes.*;
import MxCompiler.IR.IRnodes.instructions.*;

public interface IRVisitor {
    void visit(func node);
    void visit(basicBlock node);

    void visit(instruction node);
    void visit(binaryOpInstr node);
    void visit(branch node);
    void visit(cmp node);
    void visit(funCall node);
    void visit(heapAllocate node);
    void visit(jump node);
    void visit(load node);
    void visit(move node);
    void visit(returnInstr node);
    void visit(store node);
    void visit(unaryOpInstr node);
    void visit(pop node);
    void visit(push node);

    void visit(intValue node);
    void visit(intImd node);
    void visit(register node);
    void visit(virtualRegister node);
    void visit(physicRegister node);
    void visit(stackSlot node);
    void visit(staticData node);
    void visit(staticString node);

}
