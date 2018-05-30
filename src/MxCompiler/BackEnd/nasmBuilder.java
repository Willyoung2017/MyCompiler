package MxCompiler.BackEnd;

import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;
import MxCompiler.IR.IRnodes.instructions.*;
import MxCompiler.X86Related.x86RegisterSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class nasmBuilder implements IRVisitor {
    private int cmpFlag = -1;
    private Map<String, func> funcMap;
    private List<staticData> dataList;
    private basicBlock nextBlock;
    private func curFunction;
    private PrintStream out;
    private Map<stackSlot, physicRegister> slottoPhyMap = new HashMap<>();
    private Map<String, Integer> counter = new HashMap<>();
    private Map<basicBlock, String> labelMap = new HashMap<>();

    public nasmBuilder(Map<String, func> funcMap, List<staticData> dataList, PrintStream out){
        this.funcMap = funcMap;
        this.dataList = dataList;
        this.out = out;
    }

    public void runBuilder(){
        printExtern();
        printGlobal();
        System.out.println("SECTION .text\n");

        try {
            printBuiltin();
        }catch(Exception ex){
            System.err.println("File \"BuildinFunc.asm\" Not Found!");
        }

        funcMap.values().stream().forEach(this::visit);
        printStaticData();
    }

    private void printExtern(){
        out.println( "extern puts\nextern getchar\nextern putchar\nextern sprintf\n" +
                "extern __stack_chk_fail\nextern malloc\nextern printf\n" +
                "extern strlen\nextern memcpy\nextern scanf\n");
    }

    private void printGlobal(){
        for(staticData data : dataList){
            out.println("global"+data.getName());
        }

        for(func function : funcMap.values()){
            out.println("global"+function.getFuncName());
        }
    }

    private void printStaticData(){
        out.println("SECTION .data\talign=8");
        for(staticData data : dataList){
            out.println(data.getName() + ":\n\tdq 0\n");
        }
        out.println("intbuffer:\n\tdq 0\n" +
                "format1:\n\tdb\"%lld\",0\n" +
                "format2:\n\tdb\"%s\",0\n\n");
        out.println( "SECTION .bss\nstringbuffer:\n\tresb 256\n");
    }

    private void printBuiltin() throws Exception{
        String path = "src\\MxCompiler\\lib\\builtinFunc.asm";
        FileReader reader = new FileReader(path);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        while((line = br.readLine())!= null){
            out.println(line + "\n");
        }
        br.close();
        reader.close();
    }

    @Override
    public void visit(func node) {
        curFunction = node;
        out.println(node.getFuncName()+":");
        out.println("\tpush \trbp");
        out.println("\tmov \trbp, rsp");
        //callee save
        slottoPhyMap.clear();
        for(int i = 0; i <node.parameterList.size(); ++i){
            slottoPhyMap.put((stackSlot) node.parameterList.get(i), x86RegisterSet.FuncParamRegs.get(i));
        }

       for(int i = 0; i < node.preOrder.size(); ++i){
           if(i < node.preOrder.size() - 1) {
               nextBlock = node.preOrder.get(i + 1);
           }
           else{
               nextBlock = null;
           }
           visit(node.preOrder.get(i));
       }
    }

    private String allocateID(String name, Map<String, Integer> counter){
        Integer cnt = counter.get(name);
        if(cnt == null) cnt = 1;
        else ++cnt;
        counter.put(name, cnt);
        if(cnt == 1){
            return name;
        }
        else{
            return name + "_" +  cnt;
        }
    }

    private String getBlockLabel(basicBlock bb){
        String label = labelMap.get(bb);
        if(label == null){
            label = allocateID(bb.getName(), counter);
            labelMap.put(bb, label);
        }
        return label;
    }

    @Override
    public void visit(basicBlock node) {
        if(node == null) return;
        out.println(getBlockLabel(node) + ":");
        for(instruction instr = node.getHead(); instr != null; instr = instr.getNext()){
            visit(instr);
        }
        out.println();
    }

    @Override
    public void visit(instruction node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(binaryOpInstr node) {
        String op = null;

        out.print("\tmove \t");
        visit(node.result);
        out.print(", ");
        if(node.leftOperand instanceof intImd && node.rightOperand instanceof intImd){
            int lhs = ((intImd) node.leftOperand).getValue();
            int rhs = ((intImd) node.rightOperand).getValue();

            switch (node.operator) {
                case BITWISE_INCLUSIVE_OR:
                    out.print(lhs | rhs);
                    break;
                case BITWISE_EXCLUSIVE_OR:
                    out.print(lhs ^ rhs);
                    break;
                case BITWISE_AND:
                    out.print(lhs & rhs);
                    break;
                case MOD:
                    out.print(lhs % rhs);
                    break;
                case MUL:
                    out.print(lhs * rhs);
                    break;
                case DIV:
                    out.print(lhs / rhs);
                    break;
                case SUB:
                    out.print(lhs - rhs);
                    break;
                case ADD:
                    out.print(lhs + rhs);
                    break;
                case LEFT_SHIFT:
                    out.print(lhs << rhs);
                    break;
                case RIGHT_SHIFT:
                    out.print(lhs >> rhs);
            }
        }
        else {
            visit(node.leftOperand);
            out.println();
            out.print("\t");
            switch (node.operator) {
                case BITWISE_INCLUSIVE_OR:
                    op = "or";
                    break;
                case BITWISE_EXCLUSIVE_OR:
                    op = "xor";
                    break;
                case BITWISE_AND:
                    op = "and";
                    break;
                case MOD:
                    op = "mod";
                    break;
                case MUL:
                    op = "imul";
                    break;
                case DIV:
                    op = "div";
                    break;
                case SUB:
                    op = "sub";
                    break;
                case ADD:
                    op = "add";
                    break;
                case LEFT_SHIFT:
                    op = "shl";
                    break;
                case RIGHT_SHIFT:
                    op = "shr";
            }
        }
        out.print(op + " \t");
        visit(node.result);
        out.print(", ");
        visit(node.rightOperand);
        out.println();
    }

    private String Reverse(String op){
        if(op.equals("jge")){
            op = "jl";
        }
        else if(op.equals("jg")){
            op = "jle";
        }
        else if(op.equals("jle")){
            op = "jg";
        }
        else if(op.equals("jl")){
            op = "jge";
        }
        else if(op.equals("je")){
            op = "jne";
        }
        else if(op.equals("jne")){
            op = "je";
        }
        return op;
    }

    @Override
    public void visit(branch node) {
        out.print("\t");
        String op = null;

        switch(node.operator) {
            case GEQ:
                op = "jge";
                break;
            case GREATER:
                op = "jg";
                break;
            case LEQ:
                op = "jle";
                break;
            case LESS:
                op = "jl";
                break;
            case NOT_EQUAL:
                op = "jz";
                break;
            case EQUAL:
                op = "jnz";
        }
        if(cmpFlag != -1 && cmpFlag != 2){
            if(nextBlock == node.jumpto){
                if(cmpFlag == 1)
                    out.println();
                else out.println("jmp"+" \t"+getBlockLabel(node.jumpother));
            }
            else if(nextBlock == node.jumpother){
                if(cmpFlag == 1){
                    out.println("jmp"+" \t"+getBlockLabel(node.jumpto));
                }
                else {
                    out.println();
                }
            }
        }
        else{
            if(cmpFlag == -1) {
                if (nextBlock == node.jumpto) {
                    out.println(op + " \t" + getBlockLabel(node.jumpother));
                } else if (nextBlock == node.jumpother) {
                    out.println(Reverse(op) + " \t" + getBlockLabel(node.jumpto));
                }
            }
            else{
                if (nextBlock == node.jumpto) {
                    out.println(Reverse(op) + " \t" + getBlockLabel(node.jumpother));
                } else if (nextBlock == node.jumpother) {
                    out.println(op + " \t" + getBlockLabel(node.jumpto));
                }
            }
        }
        cmpFlag = -1;
    }

    @Override
    public void visit(cmp node) {
        if(node.leftOperand instanceof intImd && node.rightOperand instanceof intImd){
            boolean flag = false;
            int lhs = ((intImd) node.leftOperand).getValue();
            int rhs = ((intImd) node.rightOperand).getValue();
            switch(node.operator) {
                case GEQ:
                    flag = (lhs >= rhs);
                    break;
                case GREATER:
                    flag = (lhs > rhs);
                    break;
                case LEQ:
                    flag = (lhs <= rhs);
                    break;
                case LESS:
                    flag = (lhs < rhs);
                    break;
                case NOT_EQUAL:
                    flag = (lhs != rhs);
                    break;
                case EQUAL:
                    flag = (lhs == rhs);
            }
            if(flag) cmpFlag = 1;
            else cmpFlag = 0;
        }
        else {
            out.print("\tcmp \t");
            if (node.leftOperand instanceof intImd) {
                cmpFlag = 2;
                visit(node.rightOperand);
                out.print(", ");
                visit(node.leftOperand);
            }
            else{
                visit(node.leftOperand);
                out.print(", ");
                visit(node.rightOperand);
            }
        }
    }

    @Override
    public void visit(funCall node) {
        //caller save
        for(int i = 0; i < node.parameters.size(); ++i){
            out.println("\tmove \t"+ x86RegisterSet.FuncParamRegs.get(i).getName()+", ");
            visit(node.parameters.get(i));
            out.println();
        }

        out.println("\tcall \t"+node.function.getFuncName());
        if(node.returnReg != null) {
            out.print("\tmove \t");
            visit(node.returnReg);
            out.print(", rax\n");
        }
        //caller restore
    }

    @Override
    public void visit(heapAllocate node) {
        out.println("\tmov \trdi, ");
        visit(node.allocSize);
        out.println("\tcall malloc");
        out.print("\tmov \t");
        visit(node.destReg);
        out.print(", rax\n");
    }

    @Override
    public void visit(jump node) {
        if(curFunction.preOrder.contains(node.jumpto)) {
            out.println("\tjump \t" + getBlockLabel(node.jumpto));
        }
    }

    @Override
    public void visit(load node) {
        out.print("\tmov\t");
        visit(node.dest);
        out.print(", qword [");
        visit(node.addr);
        if(node.offset < 0){
            out.print(node.offset + "]");
        }else{
            out.print("+" + node.offset + "]");
        }
        System.out.println();
    }

    @Override
    public void visit(move node) {
        out.print("\tmove \t");
        visit(node.destReg);
        out.print(", ");
        visit(node.srcReg);
        out.println();
    }

    @Override
    public void visit(returnInstr node) {
        //callee restore
        if(node.retReg != null){
            out.print("\tmove\t rax, ");
            visit(node.retReg);
            out.println();
        }
        out.println("\tret");
        out.println();
    }

    @Override
    public void visit(store node) {
        out.print("\tmov\t qword [");
        visit(node.addr);
        if(node.offset < 0){
            out.print(node.offset + "]");
        }else{
            out.print("+" + node.offset + "]");
        }
        out.print(", ");
        visit(node.src);
        out.println();
    }

    @Override
    public void visit(unaryOpInstr node) {
        out.print("\t");
        String op = null;
        switch(node.operator){
            case NEG:
                op = "neg";
                break;
            case BITWISE_NOT:
                op = "not";
        }
        out.print(op+"\t ");
        visit(node.operand);
        out.println();
        out.print("\tmove\t ");
        visit(node.result);
        out.print(", ");
        visit(node.operand);
        out.println();
    }

    @Override
    public void visit(intValue node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(intImd node) {
        out.print(node.getValue());
    }

    @Override
    public void visit(register node) {
        if(node == null) return;
        node.accept(this);
    }

    @Override
    public void visit(virturalRegister node) {

    }

    @Override
    public void visit(physicRegister node) {
        out.print(node.getName());
    }

    @Override
    public void visit(stackSlot node) {
        physicRegister phyReg = slottoPhyMap.get(node);
        if(phyReg != null){//func para
            out.print(phyReg.getName());
        }
        else{//var that has been allocated reg i.e. in mem

        }
    }

    @Override
    public void visit(staticData node) {
        out.println( "qword [" + node.getName() + "]");
    }

    @Override
    public void visit(staticString node) {

    }
}