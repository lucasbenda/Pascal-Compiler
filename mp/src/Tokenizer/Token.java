package Tokenizer;

/**
 *
 * @author Jon Koenes
 */
public class Token {
    
    public String lexeme;
    protected String token;
    protected int lineNum;
    protected int colNum;
    protected String type;
    protected String kind;
    public boolean negative = false;
    public int size, offset, variableOffset, nestingLevel;
    
    public Token(String in_lex, String in_token, int lnum, int colnum){
        lexeme = in_lex;
        token = in_token;
        lineNum = lnum;
        colNum = colnum;
    }
    
    public Token(){
        lexeme = "";
        token = "EMPTY_TOKEN";
        lineNum = 0;
        colNum = 0;
        type = "";
    }
    public void setType(String in){
        type = in;
    }
    
    public String getType(){
        return type;
    }
    
    public void setKind(String in){
        kind = in;
    }
    
    public String getKind(){
        return kind;
    }
    
    public String getLexeme(){
        return lexeme;
    }
    
    public String getToken(){
        return token;
    }
    
    public int getLineNumber(){
        return lineNum;
    }
    
    public int getColumnNumber(){
        return colNum;
    }
}
