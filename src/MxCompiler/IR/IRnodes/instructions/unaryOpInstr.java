package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.Ast.Expression.UnaryExpression.unaryOp;
import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;

import java.util.Map;

public class unaryOpInstr extends instruction{
    public intValue operand;
    public register result;
    public unaryOp operator;

    public unaryOpInstr(){
        operand = null;
        result = null;
        operator = null;
    }

    public unaryOpInstr(basicBlock itsBlock, unaryOp operator, intValue operand, register result){
        this.itsBlock = itsBlock;
        this.operand = operand;
        this.operator = operator;
        this.result = result;
        setUsedRegister();
    }

    public register getDefRegister(){
        return result;
    }

    public void setUsedRegister(){
        usedRegister.clear();
        if(operand instanceof register){
            usedRegister.add((register) operand);
        }
    }

    public void setDefRegister(register reg){
        result = reg;
    }

    public void resetUsedRegister(Map<virtualRegister, physicRegister> allocateMap){
        if(operand instanceof virtualRegister){
            operand = allocateMap.get(operand);
        }
        setUsedRegister();
    }

    public void accept(IRVisitor visitor){
        visitor.visit(this);
    }
}
