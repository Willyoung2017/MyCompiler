package Ast;

import Ast.BuildAST.ASTBuilder;
import Ast.BuildAST.ASTViewer;
import Parser.MxLexer;
import Parser.MxParser;
import SemanticChecker.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.InputStream;

public class build {
    public static void main(String[] args) throws Exception{
            try {
                   /* String inputFilePath = "E:\\compiler\\MyCompiler\\testcase" +
                            "\\semantic\\compile_error\\test.mx";
                    InputStream is = new FileInputStream(inputFilePath);*/
                   InputStream is = System.in;
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

                    localResolver Localresolver = new localResolver();
                    rootNode.accept(Localresolver);

                    typeResolver Typeresolver = new typeResolver();
                    rootNode.accept(Typeresolver);

                    if (!Localresolver.error.exceptionList.isEmpty()) {
                            Localresolver.error.printExceptions();
                            throw new Exception();
                    }
                    if (!Typeresolver.error.exceptionList.isEmpty()) {
                            Typeresolver.error.printExceptions();
                            throw new Exception();
                    }
            }
            catch(Exception e){
                    System.exit(1);
            }
    }

    }

