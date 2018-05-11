package MxCompiler.Ast.Expression;
import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.astNode;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.virturalRegister;

public class expr extends astNode{
    public boolean isLvalue;
    public intValue reg;
    public expr(){
        isLvalue = true;
        reg = null;
    }
    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
