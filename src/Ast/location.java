package Ast;

public class location {
    public int numofLine;
    public int numofCol;

    public location(){
        numofCol = -1;
        numofLine = -1;
    }
    public location(int line, int col){
        this.numofLine = line;
        this.numofCol = col;
    }

    public String locString(){
        return "(" + numofLine + "," + numofCol + ")";
    }
}
