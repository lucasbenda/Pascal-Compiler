/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp;

import java.util.*;

/**
 *
 * @author Eric, Andrew, Karl
 */
public class Scan{
    int x=-1,y=0;
    String r = null;//this is for leftorves of a string
    String lr = null; //this holds the last r value
    String letter = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ_";
    String symbol = ".,;()=><:+-*:";
    String num = "0123456789";
    String doc[][] = new String[0][0];
    private static final HashMap<String, Type> reservedWords = initReservedWords();
    
    
    public Token getToken(String n[][]){
        doc = n;
        x++;
        //System.out.println(n[0][0].);
        for(; y <n.length || r!=lr; y++, x=0){
            for(; x < n[y].length || r!=lr; x++){
                String t = null;
                if(r == lr)
                    t = (n[y][x]);
                else{
                    t = r; x--; lr = r;
                }
                
                
                if(t.length() == 0){
                    
                }
                else if (containsChar(letter, t.charAt(0))){
                    Token id = identifier(t);
                    id.colomn = x;
                    id.row = y;
                    return id;
                }

                else if (containsChar(symbol, t.charAt(0))){
                    Token sy = symbol(t);
                    sy.colomn = x;
                    sy.row = y;
                    return sy;
                }
                else if (containsChar(num, t.charAt(0))){
                    Token nu = num(t);
                    nu.colomn = x;
                    nu.row = y;
                    return nu;
                }
                else if (t.charAt(0) == '{'){
                    Token co= comment();
                    co.colomn = x;
                    co.row = y;
                    return co;
                }
                else if (t.charAt(0) == '\''){
                    Token st= string();
                    st.colomn = x;
                    st.row = y;
                    return st;
                }

            }
        }
        Token end = new Token();
        end.lexeme = null;
        end.type = Type.MP_EOF;
        end.row = n.length;
        end.colomn = x;
        return end;
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
                                        r = t.substring(i);
					return checkForReservedWordAndEmit(tok);
				}
				break;
			case 2: 
				if(containsChar(letter, t.charAt(i)) || containsChar(num, t.charAt(i))) {
					state = 1;
					tok.type = Type.MP_IDENTIFIER;
				} else {
                                        tok.lexeme = t.substring(0, i);
                                        r = t.substring(i);
                                        return checkForReservedWordAndEmit(tok);
				}
				break;
			}

		}
                if(tok.lexeme == null)
                    tok.lexeme = t;
		return checkForReservedWordAndEmit(tok);
                
    }
    
    
    
    
    private Token symbol(String s){
        Token tok = new Token();
        switch (s.charAt(0)) {
		case '<':
			tok.type = Type.MP_LTHAN;
                        if(s.length()>1)
			switch (s.charAt(1)) {
                            case '>': tok.type = Type.MP_NEQUAL; break;
                            case '=': tok.type = Type.MP_LEQUAL; break;
			} break;
		case '>':
			tok.type = Type.MP_GTHAN;
                        if(s.length()>1)
			switch (s.charAt(1)) {
                            case '=': tok.type = Type.MP_GEQUAL; break;
			} break;
		case ':':
			tok.type = Type.MP_COLON;
                        if(s.length()>1)
			switch (s.charAt(1)) {
                            case '=': tok.type = Type.MP_ASSIGN; break;
			} break;
		case '/': tok.type = Type.MP_FLOAT_DIVIDE; break;

		case ')': tok.type = Type.MP_RPAREN; break;
		case '(': tok.type = Type.MP_LPAREN; break;
		case '=': tok.type = Type.MP_EQUAL; break;
		case '+': tok.type = Type.MP_PLUS; break;
		case '-': tok.type = Type.MP_MINUS; break;
		case '*': tok.type = Type.MP_TIMES; break;
		case ';': tok.type = Type.MP_SCOLON; break;
		case '.': tok.type = Type.MP_PERIOD; break;
		case ',': tok.type = Type.MP_COMMA; break;
		default:  tok.type = Type.MP_ERROR; break;
		}
        
                if(s.length()>2 && (tok.type ==Type.MP_ASSIGN || tok.type == Type.MP_GEQUAL ||tok.type == Type.MP_NEQUAL || tok.type == Type.MP_LEQUAL)){
                    tok.lexeme = s.substring(0, 2);
                    r = s.substring(2);
                }
                else if(s.length()>1 ){
                    tok.lexeme = s.substring(0, 1);
                    r = s.substring(1);
                }
                else{
                    tok.lexeme = s;
                }
		return tok;
        
    }
    
    
    
    
    private Token num(String n){
        Token tok = new Token();
        int state = 1;
		for (int i=0; i <n.length();i++){
			switch (state){
			case 1: //state 1 in FSA
				
				if (containsChar(num, n.charAt(i))){
					state = 1;
					tok.type = Type.MP_INTEGER_LIT;
				}
				else if (n.charAt(i) == '.'){
					state = 2;
				}
				else if (n.charAt(i) == 'e' || n.charAt(i) == 'E'){
					state = 4;
				}
                                else{ 
                                    tok.lexeme = n.substring(0,i);
                                    r = n.substring(i);
                                    return tok;
                                }
				break;
			case 2: //state 2 in FSA
				if (containsChar(num, n.charAt(i))){
					state = 3;
					tok.type = Type.MP_FIXED_LIT;
				}
                                else {
                                    tok.lexeme = n.substring(0, i);
                                    r = n.substring(i);
                                    return tok;
                                }
				break;
			case 3: //state 3 in FSA accept state for fixed literal
				
				if (containsChar(num, n.charAt(i))){
					state = 3;
					tok.type = Type.MP_FIXED_LIT;
				}
				else if (n.charAt(i) == 'e' || n.charAt(i) == 'E'){
					state = 4;
				}
                                else{
                                    tok.lexeme = n.substring(0, i);
                                    r = n.substring(i);
                                    return tok;
                                }
				break;
			case 4://state 4
				if (n.charAt(i) == '+' || n.charAt(i) == '-'){
					state = 5;
				}
				else if (containsChar(num, n.charAt(i))){ 
					state = 6;
					tok.type = Type.MP_FLOAT_LIT;
				}
                                else{
                                    tok.lexeme = n.substring(0, i);
                                    r = n.substring(i);
                                    return tok;
                                }
				break;
			case 5: //state 5
				if (containsChar(num, n.charAt(i))){
					state = 6;
					tok.type = Type.MP_FLOAT_LIT;
				}
				else {
                                    tok.lexeme = n.substring(0, i);
                                    r = n.substring(i);
                                    return tok;
                                }
				break;
			case 6: // state 6 in FSA / accept state for Float
				
				if (containsChar(num, n.charAt(i))){
					state = 6;
					tok.type = Type.MP_FLOAT_LIT;
				}
                                else{
                                    tok.lexeme = n.substring(0, i);
                                    r = n.substring(i);
                                    return tok;
                                }
				break;
			}
		}
                tok.lexeme = n;
		return tok;
    }       
    
    private Token comment() {
        Token tok = new Token();
        int yMax =doc.length;
		while ( y< yMax ) {
                    while(x < doc[y].length){
				if (containsChar(doc[y][x], '}')) {
					tok.type = Type.MP_COMMENT;
                                        tok.lexeme = tok.lexeme +" "+ doc[y][x];
					return tok;
				}
                                if(tok.lexeme !=null)
                                    tok.lexeme = tok.lexeme+ " " +doc[y][x];
                                else tok.lexeme = doc[y][x];
                                x++;
			}
                    y++;
		}
		tok.type =Type.MP_RUN_COMMENT;
		return tok;
	}
    
    private Token string() {
        Token tok = new Token();
        int yMax =doc.length;
            while ( y< yMax ) {
                while(x < doc[y].length){
                    if (containsChar(doc[y][x], '\'')) {
                        tok.type = Type.MP_STRING_LIT;
                        tok.lexeme = tok.lexeme +" "+ doc[y][x];
                        tok.lexeme = alterStringContents(tok.lexeme);
                        return tok;
                    } 
                    if(tok.lexeme !=null)
                        tok.lexeme = tok.lexeme+ " " +doc[y][x];
                    else tok.lexeme = doc[y][x];
                    x++;
                }
                y++;
            }
            tok.type =Type.MP_RUN_STRING;
            return tok;
               
	}
    private String alterStringContents(String curLexemeContent){
		curLexemeContent = curLexemeContent.substring(1, curLexemeContent.length()-1);
		curLexemeContent=(curLexemeContent.replace("''", "'"));
		return curLexemeContent;
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