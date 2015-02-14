/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp;

/**
 *
 * @author Erigon
 */
public class Scan{
    int x,y;
    String letter = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ_";
    String symbol = ".,;()=><:+-*:";
    String num = "0123456789";
    public Token nextToken(String n[][]){
        
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
    
    
    
    public boolean containsChar(String s, char search) {
    if (s.length() == 0)
        return false;
    else
        return s.charAt(0) == search || containsChar(s.substring(1), search);
}
    
    
    
    
    
    private Token identifier(String t){
        
        System.out.println("This is an identifier - " + t);
        Token n = new Token(t,x,y,"Identifier"); 
        return n;
    }
    
    
    
    
    private Token symbol(String s){
        System.out.println("This is a symbol - " + s);
        return null;
    }
    
    
    
    
    private Token num(String n){
        System.out.println("This is an integer - " + n);
        return null;
    }       
    

}