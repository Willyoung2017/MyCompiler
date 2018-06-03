package MxCompiler.X86Related;

import MxCompiler.IR.IRnodes.physicRegister;

import java.util.*;

public class x86RegisterSet {
    public static final x86Register rax = new x86Register(0, "rax", true, false, false);
    public static final x86Register rbx = new x86Register(1, "rbx", false, true, false);
    public static final x86Register rcx = new x86Register(2, "rcx", true, false, false);
    public static final x86Register rdx = new x86Register(3, "rdx", true, false, false);
    public static final x86Register rsi = new x86Register(4, "rsi", true, false, false);
    public static final x86Register rdi = new x86Register(5, "rdi", true, false, false);
    public static final x86Register rbp = new x86Register(6, "rbp", false, true, false);
    public static final x86Register rsp = new x86Register(7, "rsp", false, true, false);
    public static final x86Register r8 = new x86Register(8, "r8", true, false, false);
    public static final x86Register r9 = new x86Register(9, "r9", true, false, false);
    public static final x86Register r10 = new x86Register(10, "r10", true, false, true);
    public static final x86Register r11 = new x86Register(11, "r11", true, false, true);
    public static final x86Register r12 = new x86Register(12, "r12", false, true, true);
    public static final x86Register r13 = new x86Register(13, "r13", false, true, true);
    public static final x86Register r14 = new x86Register(14, "r14", false, true, false);
    public static final x86Register r15 = new x86Register(15, "r15", false, true, false);

    public static Set<physicRegister> CallerSavedRegs = new HashSet<>();
    public static Set<physicRegister> CalleeSavedRegs = new HashSet<>();
    public static List<physicRegister> allRegs = new LinkedList<>();
    public static List<physicRegister> GeneralRegs = new LinkedList<>();
    public static List<physicRegister> FuncParamRegs = new LinkedList<>();

    static {
        allRegs.add(rax);
        allRegs.add(rbx);
        allRegs.add(rcx);
        allRegs.add(rdx);
        allRegs.add(rsi);
        allRegs.add(rdi);
        allRegs.add(rbp);
        allRegs.add(rsp);
        allRegs.add(r8);
        allRegs.add(r9);
        allRegs.add(r10);
        allRegs.add(r11);
        allRegs.add(r12);
        allRegs.add(r13);
        allRegs.add(r14);
        allRegs.add(r15);

        FuncParamRegs.add(rdi);
        FuncParamRegs.add(rsi);
        FuncParamRegs.add(rdx);
        FuncParamRegs.add(rcx);
        FuncParamRegs.add(r8);
        FuncParamRegs.add(r9);

        allRegs.stream().filter(physicRegister::isGeneral).forEach(GeneralRegs::add);
        allRegs.stream().filter(physicRegister::isCallerSave).forEach(CallerSavedRegs::add);
        allRegs.stream().filter(physicRegister::isCalleeSave).forEach(CalleeSavedRegs::add);

    }
}
