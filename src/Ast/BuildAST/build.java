package Ast.BuildAST;

import Ast.abstractSyntaxTree;
import Parser.MxLexer;
import Parser.MxParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.InputStream;

public class build {
    public static void main(String[] args) throws Exception{
        String inputFilePath = "E:\\compiler\\MyCompiler\\testcase" +
                "\\semantic\\compile_error\\test.mx";
        InputStream is = new FileInputStream(inputFilePath);
        ANTLRInputStream input = new ANTLRInputStream(is);
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        ParseTree tree = parser.program();

        ParseTreeWalker walker = new ParseTreeWalker();
        ASTBuilder astBuilder = new ASTBuilder(parser);
        walker.walk(astBuilder, tree);

        abstractSyntaxTree rootNode = astBuilder.getRootNode();

        ASTViewer viewer = new ASTViewer(System.out);
        rootNode.accept(viewer);
    }
}
