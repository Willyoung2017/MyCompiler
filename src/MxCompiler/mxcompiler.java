package MxCompiler;

import MxCompiler.Ast.BuildAST.ASTBuilder;
import MxCompiler.Ast.abstractSyntaxTree;
import MxCompiler.BackEnd.registerAllocator;
import MxCompiler.IR.IRVisitor;
import MxCompiler.IR.IRbuilder;
import MxCompiler.IR.IRnodes.func;
import MxCompiler.IR.IRprinter;
import MxCompiler.Parser.MxLexer;
import MxCompiler.Parser.MxParser;
import MxCompiler.Parser.verboseListener;
import MxCompiler.SemanticChecker.dereferenceChecker;
import MxCompiler.SemanticChecker.localResolver;
import MxCompiler.SemanticChecker.typeResolver;
import MxCompiler.X86Related.x86RegisterSet;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

public class mxcompiler {
    private abstractSyntaxTree rootNode;
    private Map<String, func> funcMap;
    private InputStream in;
    private PrintStream out;

    public mxcompiler(InputStream in, PrintStream out){
        this.in = in;
        this.out = out;
    }

    private void buildAST() throws Exception{
        InputStream is = in;
        ANTLRInputStream input = new ANTLRInputStream(is);
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        parser.removeErrorListeners();
        verboseListener verboseListener = new verboseListener();
        parser.addErrorListener(verboseListener);
        ParseTree tree = parser.program();

        ParseTreeWalker walker = new ParseTreeWalker();
        ASTBuilder astBuilder = new ASTBuilder(parser);
        walker.walk(astBuilder, tree);

        rootNode = astBuilder.getRootNode();

        localResolver Localresolver = new localResolver();
        rootNode.accept(Localresolver);

        typeResolver Typeresolver = new typeResolver();
        rootNode.accept(Typeresolver);

        dereferenceChecker DereferenceChecker = new dereferenceChecker();
        rootNode.accept(DereferenceChecker);

        if(!verboseListener.error.exceptionList.isEmpty()){
            verboseListener.error.printExceptions();
            throw new Exception();
        }

        if (!Localresolver.error.exceptionList.isEmpty()) {
            Localresolver.error.printExceptions();
            throw new Exception();
        }
        if (!Typeresolver.error.exceptionList.isEmpty()) {
            Typeresolver.error.printExceptions();
            throw new Exception();
        }
        if (!DereferenceChecker.error.exceptionList.isEmpty()){
            DereferenceChecker.error.printExceptions();;
            throw new Exception();
        }
    }

    private void buildIR(){
        IRbuilder iRBuilder = new IRbuilder();
        rootNode.accept(iRBuilder);
        funcMap = iRBuilder.getFuncMap();
    }

    private void printIR(){
        IRprinter iRprinter = new IRprinter(funcMap, out);
        iRprinter.runPrinter();
    }

    private void allocateReg(){
        registerAllocator allocator = new registerAllocator(funcMap, x86RegisterSet.GeneralRegs);
        allocator.runAllocator();
    }

    private void runMain() throws Exception{
        buildAST();
        //throw new Exception();
        buildIR();
        //throw new Exception();
        //printIR();
        //throw new Exception();
        allocateReg();

        printIR();

    }

    public static void main(String[] argv) throws Exception {
        // check options
        String inFile = null;
        String outFile = null;
        for(int i = 0; i <= 0; ++i) {
            String num = i + ".";
            inFile = "E:\\compiler\\codgentest\\" + num + "mx";
            outFile = "E:\\compiler\\MyCode\\phy\\" + num + "ir";
            // run compiler
            InputStream in = new FileInputStream(inFile);
            PrintStream out = new PrintStream(new FileOutputStream(outFile));
            //throw new Exception();
            new mxcompiler(in, out).runMain();
        }

    }
}
