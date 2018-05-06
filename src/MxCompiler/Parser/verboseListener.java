package MxCompiler.Parser;

import MxCompiler.Exception.compilationError;
import MxCompiler.Exception.semanticException;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.Collections;
import java.util.List;

public class verboseListener extends BaseErrorListener {
    public compilationError error;
    public verboseListener(){
        error = new compilationError();
    }
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        //error.add(new semanticException("rule stack: "+stack));
        error.add(new semanticException("line "+line+":"+charPositionInLine+" at "+ offendingSymbol+": "+msg));
    }
}

