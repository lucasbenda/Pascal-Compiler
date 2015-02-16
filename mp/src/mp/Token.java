package mp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Erigon
 */
public class Token {
    public String lexeme;
    public int row,colomn;
    public Type type;
    
    
    public String getLexeme(){
        return lexeme;
    }
    
    public Type getType() {
        return type;
    }
    
    public int getLineNumber() {
        return row;
    }
    public int getColomnNumber(){
        return colomn;
    }
    
}
