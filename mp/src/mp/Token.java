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
    private String token;
    private int row,colom;
    private String type;
    
    public Token(String token, int r, int c, String t) {
        this.token = token;
        this.row = r;
        this.colom = c;
        this.type = t;
    }
    
    public String getToken(){
        return token;
    }
    
    public String getType() {
        return type;
    }
    
    public int getRow() {
        return row;
    }
    public int getColom(){
        return colom;
    }
    
}
