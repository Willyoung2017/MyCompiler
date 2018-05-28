package MxCompiler.IR.IRnodes.instructions;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRnodes.intValue;
import MxCompiler.IR.IRnodes.physicRegister;
import MxCompiler.IR.IRnodes.register;
import MxCompiler.IR.IRnodes.virturalRegister;

import java.util.Map;

public class cmp extends instruction{
    public intValue leftOperand;
    public intValue rightOperand;
    public register result;
    public binaryOp operator;

    public cmp(){
        leftOperand = null;
        rightOperand = null;
        result = null;
        operator = null;
    }
    public cmp(binaryOp operator, intValue leftOperand, intValue rightOperand, register result){
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.result = result;
        setUsedRegister();
    }

    @Override
    public register getDefRegister() {
        return null;
    }

    @Override
    public void setUsedRegister() {
        usedRegister.clear();
        if(leftOperand instanceof register) usedRegister.add((register) leftOperand);
        if(rightOperand instanceof register) usedRegister.add((register) rightOperand);
    }


    public void setDefRegister(register reg){

    }



    public void resetUsedRegister(Map<virturalRegister, physicRegister> allocateMap){
        if(leftOperand instanceof  virturalRegister)  leftOperand = allocateMap.get(leftOperand);
        if(rightOperand instanceof virturalRegister) rightOperand = allocateMap.get(rightOperand);
        setUsedRegister();
    }
}
