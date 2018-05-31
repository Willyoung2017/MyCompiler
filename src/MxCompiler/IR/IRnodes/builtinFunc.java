package MxCompiler.IR.IRnodes;

import MxCompiler.Ast.TypeSpecifier.boolType;
import MxCompiler.Ast.TypeSpecifier.intType;
import MxCompiler.Ast.TypeSpecifier.stringType;
import MxCompiler.Ast.TypeSpecifier.voidType;

import java.util.LinkedList;
import java.util.List;

public class builtinFunc {
    public func builtinOrd = new func("string.ord", new intType());
    public func builtinPrintString = new func("print", new voidType());
    public func builtinPrintlnString = new func("println", new voidType());
    public func builtinGetString = new func("getString", new stringType());
    public func builtinGetInt = new func("getInt", new intType());
    public func builtintoString = new func("toString", new stringType());
    public func builtinSubstring = new func("string.substring", new stringType());
    public func builtinParseInt = new func("string.parseInt", new intType());
    public func builtinStringAdd = new func("string.add", new stringType());
    public func builtinStringEqual = new func("string.eq", new boolType());
    public func builtinStringLess = new func("string.s", new boolType());
    public func builtinStingLessEqual = new func("string.le", new boolType());
    public func builtinStringGreater = new func("string.g", new boolType());
    public func builtinStringGreaterEqual = new func("string.ge", new boolType());
    public List<func> builtinFunctions = new LinkedList<>();

}
