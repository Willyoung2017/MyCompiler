package MxCompiler;

import MxCompiler.Ast.Expression.BinaryExpression.binaryOp;
import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRnodes.*;
import MxCompiler.IR.IRnodes.instructions.*;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class errorBuilder implements IRVisitor {
    private int plusOffset = 0;
    private int cmpFlag = -1;
    private int cntLen = 0;
    private Map<String, func> funcMap;
    private List<staticData> dataList;
    private List<staticString> stringPool;
    private basicBlock nextBlock;
    private func curFunction;
    private PrintStream out;
    private Map<String, Integer> counter = new HashMap<>();
    private Map<basicBlock, String> labelMap = new HashMap<>();

    public errorBuilder(Map<String, func> funcMap, List<staticData> dataList, List<staticString> stringPool, PrintStream out){
        this.funcMap = funcMap;
        this.dataList = dataList;
        this.stringPool = stringPool;
        this.out = out;
    }

    public void runBuilder(){
        //printExtern();
        //printGlobal();
       //out.println("SECTION .text\n");
        boolean flag = true;
        for(func fun : funcMap.values()){
            if(fun.getFuncName().equals("main")){
                flag = false;
            }
            if(flag){
                continue;
            }
            visit(fun);
        }
        //funcMap.values().stream().forEach(this::visit);
       /* try {
            printBuiltin();
        }catch(Exception ex){
            System.err.println("File \"BuildinFunc.asm\" Not Found!");
        }*/
        printStaticData();
    }

    private void printExtern(){
        out.println( "extern puts\nextern getchar\nextern putchar\nextern sprintf\n" +
                "extern __stack_chk_fail\nextern malloc\nextern printf\n" +
                "extern strlen\nextern memcpy\nextern scanf\n");
    }

    private void printGlobal(){
        for(staticData data : dataList){
            out.println("global "+data.getName());
        }

        for(func function : funcMap.values()){
            out.println("global "+function.getFuncName());
        }
    }

    private String toInt(String value){
        StringBuffer str = new StringBuffer();
        int n = value.length();
        for (int j = 0; j < n; j++) {
            if (value.charAt(j) == '\\') {
                ++j;
                ++cntLen;
                if (value.charAt(j) == 'n') {
                    str.append("  10,");
                }
                if (value.charAt(j) == '\"') {
                    str.append("  34,");
                }
                if (value.charAt(j) == '\\') {
                    str.append("  92,");
                }
            } else {
                ++cntLen;
                str.append(String.format(" %3s,", (int) value.charAt(j)));
            }
        }
        str.append("   0\n");
        return str.toString();
    }
    private String getstaticString(String value){
        StringBuilder sb  = new StringBuilder();
        value.chars().forEach(i->sb.append(String.format("%02X",i)).append("H, "));
        sb.append("00H");
        return sb.toString();
    }

    private void printStaticData(){
        out.println("SECTION .data\talign=8");
        for(staticData data : dataList){
            out.print("data_"+data.getName() + ":\n\tdq\t 0\n");
        }
        for(staticString str : stringPool){
            cntLen = 0;
            String tmp = toInt(str.value);
            out.printf("\n\tdq\t %d\n", cntLen);
            out.print(str.getName()+ ":\n\tdb\t ");
            out.print(tmp+"\n");
        }
        out.println("intbuffer:\n\tdq 0\n" +
                "format1:\n\tdb\"%lld\",0\n" +
                "format2:\n\tdb\"%s\",0\n\n");
        out.println( "SECTION .bss\nstringbuffer:\n\tresb 256\n");
    }

    private void printBuiltin() throws Exception{
        String path = "src/MxCompiler/lib/builtinFunc.asm";
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
        out.println("\tpush\t rbp");
        out.println("\tmov\t rbp, rsp");
        //callee save

       for(int i = 0; i < node.getreversepostOrderVisit().size(); ++i){
           if(i < node.reversePostOrder.size() - 1) {
               nextBlock = node.reversePostOrder.get(i + 1);
           }
           else{
               nextBlock = null;
           }
           visit(node.reversePostOrder.get(i));
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
        if(node.leftOperand instanceof intImd && node.rightOperand instanceof intImd){
            int lhs = ((intImd) node.leftOperand).getValue();
            int rhs = ((intImd) node.rightOperand).getValue();
            out.print("\tmov\t ");
            visit(node.result);
            out.print(", ");
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
            out.println();
        }
        else {
            if (node.operator == binaryOp.DIV) {
                out.print("\tmov\t r15, ");
                visit(node.rightOperand);
                out.println();
                out.print("\tmov\t rdx, 0\n");
                out.print("\tmov\t rax, ");
                visit(node.leftOperand);
                out.println();
                out.print("\tidiv\t r15\n");
                out.print("\tmov\t ");
                visit(node.result);
                out.print(", rax\n");
            } else if (node.operator == binaryOp.MOD) {
                out.print("\tmov\t r15, ");
                visit(node.rightOperand);
                out.println();
                out.print("\tmov\t rdx, 0\n");
                out.print("\tmov\t rax , ");
                visit(node.leftOperand);
                out.println();
                out.print("\tidiv\t r15\n");
                out.print("\tmov\t ");
                visit(node.result);
                out.print(", rdx\n");
            }
            else {
                out.print("\tmov\t r15, ");
                visit(node.leftOperand);
                out.println();
                if(node.operator == binaryOp.LEFT_SHIFT || node.operator == binaryOp.RIGHT_SHIFT){
                    out.print("\tmov\t rcx, ");
                    visit(node.rightOperand);
                    out.println();
                    op = node.operator == binaryOp.LEFT_SHIFT ? "\tshl\t " : "\tshr\t ";
                    out.print(op + "r15, cl\n");
                }
                else {
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
                        case MUL:
                            op = "imul";
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

                    out.print("\t" + op + "\t r15, ");
                    visit(node.rightOperand);
                    out.println();
                }
                out.print("\tmov\t ");
                visit(node.result);
                out.print(", r15\n");
            }
        }
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
        if(nextBlock == node.jumpto && !curFunction.reversePostOrder.contains(node.jumpother)){
            return;
        }
        else if(nextBlock == node.jumpother && !curFunction.reversePostOrder.contains(node.jumpto)){
            return;
        }
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
                op = "jne";
                break;
            case EQUAL:
                op = "je";
        }
        if(cmpFlag != -1){
            if(nextBlock == node.jumpto){
                if(cmpFlag == 1)
                    out.println();
                else
                    out.println("jmp"+"\t "+getBlockLabel(node.jumpother));
            }
            else if(nextBlock == node.jumpother){
                if(cmpFlag == 1){
                    out.println("jmp"+"\t "+getBlockLabel(node.jumpto));
                }
                else {
                    out.println();
                }
            }
        }
        else{
                if (nextBlock == node.jumpto) {
                    out.println(Reverse(op) + "\t " + getBlockLabel(node.jumpother));
                } else if (nextBlock == node.jumpother) {
                    out.println(op + "\t " + getBlockLabel(node.jumpto));
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
            out.print("\tmov\t r15, ");
            visit(node.leftOperand);
            out.println();
            out.print("\tcmp\t r15");
            out.print(", ");
            visit(node.rightOperand);
            out.println();
        }
    }

    @Override
    public void visit(funCall node) {
        //caller save
        /*
        for(int i = 0; i < node.parameters.size(); ++i){
            if(i <= 5) {
                out.print("\tmov\t " + x86RegisterSet.FuncParamRegs.get(i).getName() + ", ");
                visit(node.parameters.get(i));
            }
            else {
                out.print("\tpush\t ");
                visit(node.parameters.get(node.parameters.size()-1-i+6));
            }

            out.println();
        }*/

        out.println("\tcall\t "+node.function.getFuncName());
        if(node.returnReg != null) {
            out.print("\tmov\t ");
            visit(node.returnReg);
            out.print(", rax\n");
        }
        //caller restore
    }

    @Override
    public void visit(heapAllocate node) {
        out.print("\tmov\t rdi, ");
        visit(node.allocSize);
        out.println();
        out.println("\tcall malloc");
        out.print("\tmov\t ");
        visit(node.destReg);
        out.print(", rax\n");
    }

    @Override
    public void visit(jump node) {
        if(curFunction.reversePostOrder.contains(node.jumpto) && nextBlock != node.jumpto) {
            out.println("\tjmp\t " + getBlockLabel(node.jumpto));
        }
    }

    @Override
    public void visit(load node) {
        //NOT_OK:
        //load  dest       addr
        //load  register   stackSlot   PAT
        //load  staticData stackSlot   PAT
        //load  staticData register
        //load  staticData staticData  e.g. global classmemAccess
        //load  register   staticData
        /*
            mov addr dest
            convert to:
            mov r15 addr
            mov r14 qword[r15 + offset]
            mov dest r14

            PAT:
            addr: stackSlot(offset is set)
                mov r15 addr
                mov dest r15

        */
        if(node.dest instanceof staticData || node.addr instanceof staticData || node.addr instanceof stackSlot) {
            out.print("\tmov\t r15, ");
            if(node.addr instanceof stackSlot){
                plusOffset = node.offset;
                visit(node.addr);
                plusOffset = 0;
                out.println();
                out.print("\tmov\t ");
                visit(node.dest);
                out.print(", r15\n");
            }
            else {
                visit(node.addr);
                out.println();
                out.print("\tmov\t r14, ");
                out.print("qword [ r15");
                if (node.offset != 0) {
                    if (node.offset > 0) {
                        out.print("+ " + node.offset);
                    } else out.print("- " + (-node.offset));
                }
                out.print(" ]\n");
                out.print("\tmov\t ");
                visit(node.dest);
                out.print(", r14\n");
            }
        }

        /*
        if(node.addr instanceof staticData){

            out.print("\tmov\t r15,");
            if (node.offset == 0) {
                out.print("\tmov\t r15,");
                //visit(node.dest);
                //out.print(", ");
                visit(node.addr);
                out.println();

            }
            else {
                out.print("\tmov\t r15, ");
                //visit(node.dest);
                //out.print(", ");
                visit(node.addr);
                out.println();
                out.print("\tmov\t ");
                visit(node.dest);
                out.print(", qword [");
                visit(node.dest);
                if (node.offset < 0) {
                    out.print(node.offset + "]");
                }  else if (node.offset == 0){
                    out.print("]");
                }else {
                    out.print("+" + node.offset + "]");
                }
                out.println();
            }
        }*/
        else{
        out.print("\tmov\t ");
        visit(node.dest);
        out.print(", qword [ ");
        visit(node.addr);
        if (node.offset < 0) {
            out.print(" - " + (-node.offset) + " ]");
        } else if (node.offset == 0) {
            out.print(" ]");
        } else {
            out.print(" + " + node.offset + " ]");
        }
        out.println();
    }
    }

    @Override
    public void visit(move node) {
        if(node.srcReg instanceof staticData && node.destReg instanceof staticData){
            out.print("\tmov\t r15, ");
            visit(node.srcReg);
            out.println();
            out.print("\tmov\t ");
            visit(node.destReg);
            out.print(", r15\n");
        }
        else {
            out.print("\tmov\t ");
            visit(node.destReg);
            out.print(", ");
            visit(node.srcReg);
            out.println();
        }
    }

    @Override
    public void visit(returnInstr node) {
        //callee restore
        if(node.retReg != null){
            out.print("\tmov\t rax, ");
            visit(node.retReg);
            out.println();
        }
        out.println("\tret");
        out.println();
    }

    @Override
    public void visit(store node) {
        //NOT_OK:
        //store addr        src
        //store stackSlot   register    PAT
        //store stackSlot   staticData  PAT
        //store staticData  intImd      PAT
        //store register    staticData
        //store staticData  staticData
        //store staticData  register
        /*
          mov addr src
          convert to:
          mov r15  addr
          mov r14  src
          mov qword[r15 + offset] r14

          PAT:
          mov r15  src
          mov addr r15
         */

        if(node.addr instanceof staticData || node.src instanceof staticData || node.addr instanceof stackSlot) {
            if(node.src instanceof intImd){
                out.print("\tmov\t ");
                visit(node.addr);
                out.print(", ");
                visit(node.src);
                out.println();
                return;
            }
            out.print("\tmov\t r15, ");
            if(node.addr instanceof stackSlot) {
                visit(node.src);
                out.println();
                out.print("\tmov\t ");
                plusOffset = node.offset;
                visit(node.addr);
                plusOffset = 0;
                out.print(", r15\n");
            }
            else {
                visit(node.addr);
                out.println();
                out.print("\tmov\t r14, ");
                visit(node.src);
                out.println();
                out.print("\tmov\t qword [ r15");
                if (node.offset != 0) {
                    if (node.offset > 0) {
                        out.print(" + " + node.offset);
                    } else out.print("- " + (-node.offset));
                }
                out.print(" ]");
                out.print(", r14\n");
            }
        }

     /*   if(node.addr instanceof staticData){
            if(node.offset == 0){
                out.print("\tmov\t ");
                visit(node.addr);
                out.print(", ");
                visit(node.src);
                out.println();
            }
            else {
                out.print("\tmov\t ");
                out.print("r15, ");
                visit(node.addr);
                out.println();
                out.print("\tmov\t qword [r15");
                if (node.offset < 0) {
                    out.print(node.offset + "]");
                }  else if (node.offset == 0){
                    out.print("]");
                }else {
                    out.print("+" + node.offset + "]");
                }
                out.print(", ");
                visit(node.src);
                out.println();
            }
        }*/
        else {
            out.print("\tmov\t qword [ ");
            visit(node.addr);
            if (node.offset < 0) {
                out.print(" - " + (-node.offset) + " ]");
            } else if (node.offset == 0){
                out.print(" ]");
            }
            else{
                out.print(" + " + node.offset + " ]");
            }

            out.print(", ");
            visit(node.src);
            out.println();
       }
    }

    @Override
    public void visit(unaryOpInstr node) {
        out.print("\t");
        if(node.operand instanceof intImd){
            out.print("mov\t ");
            visit(node.result);
            out.print(", ");
            int lhs = ((intImd) node.operand).getValue();
            switch(node.operator){
                case NEG:
                    lhs = -lhs;
                    break;
                case BITWISE_NOT:
                    lhs = ~lhs;
            }
            out.print(lhs + "\n");
        }
        else {
            out.print("\tmov\t r15, ");
            visit(node.operand);
            out.println();
            String op = null;
            switch (node.operator) {
                case NEG:
                    op = "neg";
                    break;
                case BITWISE_NOT:
                    op = "not";
            }
            out.print("\t" + op + "\t r15\n");
            //out.println();
            out.print("\tmov\t ");
            visit(node.result);
            out.print(", r15");
            out.println();
        }
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
    public void visit(virtualRegister node) {

    }

    @Override
    public void visit(physicRegister node) {
        out.print(node.getName());
    }

    @Override
    public void visit(stackSlot node) {
        int offset = curFunction.info.stackSlotOffsetMap.get(node);
        out.print("qword [ rbp");
        if(offset > 0)
            out.print(" + " + (plusOffset + offset));
        else if (offset != 0)
            out.print(" - " +  (-(offset + plusOffset)));
            out.print(" ]");
    }

    public void visit(pop node){
        out.print("\tpop\t ");
        visit(node.dest);
        out.println();
    }


    public void visit(push node){
        out.print("\tpush\t ");
        visit(node.dest);
        out.println();
    }

    @Override
    public void visit(staticData node) {
        out.print( "qword [ " + "data_" + node.getName() + " ]");
    }

    @Override
    public void visit(staticString node) {
        out.print(node.getName());
    }
}
