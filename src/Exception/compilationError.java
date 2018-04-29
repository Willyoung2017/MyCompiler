package Exception;

import java.util.LinkedList;
import java.util.List;

public class compilationError {
    public  List<Exception> exceptionList;

    public compilationError(){
        exceptionList = new LinkedList<>();
    }

    public void add(Exception exception){
        exceptionList.add(exception);
    }

    public  void printExceptions(){
        for(Exception exception : exceptionList){
            System.err.println("Error: " + exception.getMessage());
        }
    }

    public  Exception top(){
        return exceptionList.get(exceptionList.size()-1);
    }
}
