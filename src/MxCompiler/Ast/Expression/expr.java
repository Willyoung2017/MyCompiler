package MxCompiler.Ast.Expression;
import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.astNode;

public class expr extends astNode{
    public boolean isLvalue;

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
