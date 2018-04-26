package Exception;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class compilationError {
    public static List<Exception> exceptionList;

    public compilationError(){
        exceptionList = new LinkedList<>();
    }

    public void add(Exception exception){
        exceptionList.add(exception);
    }

    public static void printExceptions(){
        for(Exception exception : exceptionList){
            System.err.println("Error: " + exception.getMessage());
        }
    }

    public static Exception top(){
        return exceptionList.get(exceptionList.size()-1);
    }
}
