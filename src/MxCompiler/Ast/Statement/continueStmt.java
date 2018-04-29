package MxCompiler.Ast.Statement;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class continueStmt extends stmt{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
