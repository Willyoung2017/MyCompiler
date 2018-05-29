package MxCompiler.Ast.Expression;
import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.astNode;
import MxCompiler.IR.IRnodes.basicBlock;
import MxCompiler.IR.IRnodes.intValue;

public class expr extends astNode{
    public boolean isLvalue;
    //for IR
    public basicBlock jumpto;
    public basicBlock jumpother;
    public intValue nodeValue;
    public intValue addr;
    public int offset;

    public expr(){
        isLvalue = true;
        nodeValue = null;
        addr = null;
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
