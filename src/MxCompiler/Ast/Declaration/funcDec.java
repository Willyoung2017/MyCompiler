package MxCompiler.Ast.Declaration;

import MxCompiler.Ast.BuildAST.ASTVisitor;
import MxCompiler.Ast.Statement.compoundStmt;
import MxCompiler.Ast.TypeSpecifier.typ;

import java.util.LinkedList;
import java.util.List;

public class funcDec extends dec {
    public typ functionType;
    //public String functionName;
    public List<varDec> parameterList;
    public compoundStmt functionStmt;

    public funcDec(){
        name = null;
        functionType = null;
        functionStmt = null;
        parameterList = new LinkedList<>();
    }

    public funcDec(typ funcType, String funcName, List<varDec> paraList, compoundStmt funcStmt){
        functionType = funcType;
        name = funcName;
        functionStmt = funcStmt;
        parameterList = paraList;
    }

    public void accept(ASTVisitor visitor){
        visitor.visit(this);
    }
}
