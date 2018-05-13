package MxCompiler.Ast.Expression;
import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.astNode;
import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.virturalRegister;

public class expr extends astNode{
    public boolean isLvalue;
    public intValue reg;
    //for IR
    public basicBlock jumpto;
    public basicBlock jumpother;
    public int offset;

    public expr(){
        isLvalue = true;
        reg = null;
        jumpother = null;
        jumpto = null;
    }

    public void setBlocks(basicBlock jumpto, basicBlock jumpother){
        this.jumpto = jumpto;
        this.jumpother = jumpother;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
