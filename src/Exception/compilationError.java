package Exception;

import java.util.LinkedList;
import java.util.List;

public class compilationError {
    public static List<Exception> exceptionList;

    public void compilationError(){
        exceptionList = new LinkedList<>();
    }

    public void add(Exception exception){
        exceptionList.add(exception);
    }

    public void printExceptions(){
        for(Exception exception : exceptionList){
            System.err.println(exception.getMessage());
        }
    }
}
