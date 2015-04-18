/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eric, Anderw, Karl
 */
public class Mp {

    /**
     * @param args the command line arguments
     */
    
    static int row = 0;
    static int colom =0;
    static String[][] matrix = {{""}, {""}};
    
    public static void main(String[] args) {
        String[][] text = new String[0][0];

        try {
            text = create2DIntMatrixFromFile("C:\\Users\\Erigon\\Desktop\\hi.txt");
        } 
        catch (Exception ex) {
            Logger.getLogger(Mp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Scan scanner = new Scan();
        Token word = scanner.getToken(text);
        while (word.getLexeme()!= null){
                System.out.println(word.type.toString()+"  "+ word.row+"  "+ word.colomn+"  "+ word.lexeme);
                word = scanner.getToken(text);
        }
        
        System.out.println("\n End of Scanner output \n");
        
        
        Parser par = new Parser(text);
    }
    
    
    
    
    public static String[][] create2DIntMatrixFromFile(String filename) throws Exception {
 

        File inFile = new File(filename);
        Scanner in = new Scanner(inFile);

        while(in.hasNextLine()){
            String temp = in.nextLine();
            colom++;
        }

        matrix = new String[colom][1];

        in.close();    

        in = new Scanner(inFile);

        for(int i=0; i < colom; i++){
          matrix[i] = in.nextLine().trim().split("\\s+");
        }
        in.close();
                      
     return matrix;
    }
}
