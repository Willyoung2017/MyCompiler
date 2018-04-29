package MxCompiler.Ast.Declaration;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Statement.compoundStmt;

import java.util.LinkedList;
import java.util.List;

public class constructFuncDec extends dec{
    //public String funcName;
    public compoundStmt funcStmt;
    public List<varDec> parameters;

    public constructFuncDec(){
        name = null;
        funcStmt = null;
        parameters = new LinkedList<>();
    }

    public constructFuncDec(String funcName, compoundStmt funcStmt, List<varDec> parameters){
        this.name = funcName;
        this.funcStmt = funcStmt;
        this.parameters = parameters;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
