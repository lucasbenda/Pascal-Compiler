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
 * @author Erigon
 */
public class Mp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scan scanner = new Scan();
        try {
            //BufferedReader in = new BufferedReader(new FileReader("K:\\location\\inputfile.txt"));
            String text[][] = create2DIntMatrixFromFile("C:\\Users\\Erigon\\Desktop\\hi.txt");
            scanner.nextToken(text);
            /*
            try{
            Scanner input = new Scanner(new File("file.txt"));
            while(input.hasNextLine()){
            String message = input.nextLine();
            //message = message.replace(" ", "");
            String t[] = message.split(" ");
            
            
            }
            input.close();
            }
            catch(Exception e){}*/
            System.out.println(text[0][5]);
        } catch (Exception ex) {
            Logger.getLogger(Mp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static String[][] create2DIntMatrixFromFile(String filename) throws Exception {
    String[][] matrix = {{""}, {""}};

    File inFile = new File(filename);
    Scanner in = new Scanner(inFile);
    

    int row = 0;
    int colom =0;
    
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

    
    /*
    in = new Scanner(inFile);

    int lineCount = 0;
    while (in.hasNextLine()) {
      String[] currentLine = in.nextLine().trim().split("\\s+"); 
         for (int i = 0; i < currentLine.length; i++) {
            matrix[lineCount][i] = currentLine[i];    
                }
      lineCount++;
     }           */                      
     return matrix;
    }
}
