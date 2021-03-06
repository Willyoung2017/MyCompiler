package MxCompiler;

import MxCompiler.Ast.BuildAST.ASTBuilder;
import MxCompiler.Ast.BuildAST.ASTViewer;
import MxCompiler.Ast.abstractSyntaxTree;
import MxCompiler.Parser.verboseListener;
import MxCompiler.SemanticChecker.dereferenceChecker;
import MxCompiler.SemanticChecker.localResolver;
import MxCompiler.SemanticChecker.typeResolver;
import MxCompiler.Parser.MxLexer;
import MxCompiler.Parser.MxParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.InputStream;

public class build {
    public static void main(String[] args) throws Exception{
            try {
                    String inputFilePath = "E:\\compiler\\codgentest\\" + "1." + "mx";
                    InputStream is = new FileInputStream(inputFilePath);
                    //InputStream is = System.in;
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

                    abstractSyntaxTree rootNode = astBuilder.getRootNode();

                    ASTViewer viewer = new ASTViewer(System.out);
                    rootNode.accept(viewer);

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
            catch(Exception e){
                    System.exit(1);
           }
    }

    }

