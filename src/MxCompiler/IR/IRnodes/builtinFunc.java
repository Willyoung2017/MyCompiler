package MxCompiler.IR.IRnodes;

import MxCompiler.Ast.TypeSpecifier.boolType;
import MxCompiler.Ast.TypeSpecifier.intType;
import MxCompiler.Ast.TypeSpecifier.stringType;
import MxCompiler.Ast.TypeSpecifier.voidType;

import java.util.LinkedList;
import java.util.List;

public class builtinFunc {
    public func builtinPrintString = new func("print_string", new voidType());
    public func builtinPrintlnString = new func("println_string", new voidType());
    public func builtinGetString = new func("get_string", new stringType());
    public func builtinGetInt = new func("get_int", new intType());
    public func builtintoString = new func("to_string", new stringType());
    public func builtinSubstring = new func("sub_string", new stringType());
    public func builtinParseInt = new func("parse_int", new intType());
    public func builtinStringAdd = new func("add_string", new stringType());
    public func builtinStringEqual = new func("equal_string", new boolType());
    public func builtinStringLess = new func("less_string", new boolType());
    public func builtinStingLessEqual = new func("lq_string", new boolType());
    public func builtinStringGreater = new func("greater_string", new boolType());
    public func builtinStringGreaterEqual = new func("gq_string", new boolType());
    public List<func> builtinFunctions = new LinkedList<>();

}
