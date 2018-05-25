package MxCompiler.IR.IRnodes;

import java.util.LinkedList;
import java.util.List;

public class builtinFunc {
    public func buildinNewArray;
    public func builtinPrintString;
    public func builtinPrintlnString;
    public func builtinGetString;
    public func builtinGetInt;
    public func builtintoString;
    public func builtinSubstring;
    public func builtinParseInt;
    public func builtinStringEqual;
    public func builtinStringLess;
    public List<func> builtinFunctions = new LinkedList<>();

}
