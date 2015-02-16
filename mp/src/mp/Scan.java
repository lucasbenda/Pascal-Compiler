/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp;

import java.util.*;

/**
 *
 * @author Erigon
 */
public class Scan{
    int x,y;
    String letter = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ_";
    String symbol = ".,;()=><:+-*:";
    String num = "0123456789";
    String[][] doc;
    private static final HashMap<String, Type> reservedWords = initReservedWords();
    
    
    public Token getToken(String n[][]){
        doc = n;
        //System.out.println(n[0][0].);
        for(int y=0; y <n.length; y++){
            for(int x = 0; x < n[y].length; x++){
                String t = (n[y][x]);

                if (containsChar(letter, t.charAt(0))){
                     identifier(t);
                }

                else if (containsChar(symbol, t.charAt(0))){
                     symbol(t);
                }
                else if (containsChar(num, t.charAt(0))){
                     num(t);
                }

                /*if(t[0].contains()){
                    aStore = aStore + "_" + t[1];
                }
                if((store[a]).contains("B-"){
                    String t[] = (store[a]).split("-");
                    bStore = bStore + "_" + t[1];
                }

                if (byte[] == char){
                    identifier();
                }
                else if(byte[] == num){

                }
                else if(byte[] == ){

                }*/
            }
        }
        return null;
    }  
    
    
    
    private boolean containsChar(String s, char search) {
    if (s.length() == 0)
        return false;
    else
        return s.charAt(0) == search || containsChar(s.substring(1), search);
    }
    
    private Token checkForReservedWordAndEmit(Token t) {
		Token current = t;
		String word = current.lexeme.toLowerCase();
		Type possible = reservedWords.get(word);
		if (possible != null)
			current.type = possible;
		return current;
    }

    
    
    
    
    private Token identifier(String t){
        
        Token tok = new Token();
        int state = 1;
		for (int i=0; i< t.length(); i++){
			switch (state){ 
			case 1:				 
				if (t.charAt(i) == '_' ) {
					state = 2;
					tok.type=  Type.MP_IDENTIFIER;
				}else if (containsChar(letter, t.charAt(i)) || containsChar(num, t.charAt(i))){ 
					state = 1;
					tok.type = Type.MP_IDENTIFIER;
				} else { 
                                        tok.lexeme = t.substring(0, i);
					return checkForReservedWordAndEmit(tok);
				}
				break;
			case 2: 
				if(containsChar(letter, t.charAt(i)) || containsChar(num, t.charAt(i))) {
					state = 1;
					tok.type = Type.MP_IDENTIFIER;
				} else {
                                        tok.lexeme = t.substring(0, i);
                                        return checkForReservedWordAndEmit(tok);
				}
				break;
			}

		}
                tok.lexeme = t;
		return checkForReservedWordAndEmit(tok);
                
    }
    
    
    
    
    private Token symbol(String s){
        System.out.println("This is a symbol - " + s);
        return null;
    }
    
    
    
    
    private Token num(String n){
        System.out.println("This is an integer - " + n);
        return null;
    }       
    
    
    private static HashMap<String, Type> initReservedWords() {
		HashMap<String, Type> reserved = new HashMap<String, Type>();
		reserved.put("and", Type.MP_AND);
		reserved.put("begin", Type.MP_BEGIN);
		reserved.put("boolean", Type.MP_BOOLEAN);
		reserved.put("div", Type.MP_DIV);
		reserved.put("do", Type.MP_DO);
		reserved.put("downto", Type.MP_DOWNTO);
		reserved.put("else", Type.MP_ELSE);
		reserved.put("end", Type.MP_END);
		reserved.put("false", Type.MP_FALSE);
		reserved.put("fixed", Type.MP_FIXED);
		reserved.put("float", Type.MP_FLOAT);
		reserved.put("for", Type.MP_FOR);
		reserved.put("function", Type.MP_FUNCTION);
		reserved.put("if", Type.MP_IF);
		reserved.put("integer", Type.MP_INTEGER);
		reserved.put("mod", Type.MP_MOD);
		reserved.put("not", Type.MP_NOT);
		reserved.put("or", Type.MP_OR);
		reserved.put("procedure", Type.MP_PROCEDURE);
		reserved.put("program", Type.MP_PROGRAM);
		reserved.put("read", Type.MP_READ);
		reserved.put("repeat", Type.MP_REPEAT);
		reserved.put("string", Type.MP_STRING);
		reserved.put("then", Type.MP_THEN);
		reserved.put("true", Type.MP_TRUE);
		reserved.put("to", Type.MP_TO);
		reserved.put("type", Type.MP_TYPE);
		reserved.put("until", Type.MP_UNTIL);
		reserved.put("var", Type.MP_VAR);
		reserved.put("while", Type.MP_WHILE);
		reserved.put("write", Type.MP_WRITE);
		reserved.put("writeln", Type.MP_WRITELN);
		return reserved;
	}
    

}