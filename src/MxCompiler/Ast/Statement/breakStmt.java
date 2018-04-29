package MxCompiler.Ast.Statement;

import MxCompiler.Ast.BuildAST.ASTVisitor;

public class breakStmt extends stmt{

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
