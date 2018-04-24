package Ast.Declaration;

import Ast.BuildAST.ASTVisitor;
import Ast.Statement.compoundStmt;

public class constructFuncDec extends dec{
    public String funcName;
    public compoundStmt funcStmt;

    public constructFuncDec(){
        funcName = null;
        funcStmt = null;
    }

    public constructFuncDec(String funcName, compoundStmt funcStmt){
        this.funcName = funcName;
        this.funcStmt = funcStmt;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
