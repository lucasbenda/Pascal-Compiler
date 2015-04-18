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
public class SymbolTable {
//insert
//lookup
//return attributes
//create symbol table
//destroy symbol table
    private SymbolTable thisSymbol;
	
	public SymbolTable(){
		thisSymbol = new SymbolTable("_program", Kind.Program, null);
	}
	
	
}
