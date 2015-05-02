/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

/**
 *
 * @author Erigon
 */
public class Analysis {
    
    public Analysis() {
    }
    
    public void genWriteStmt() {
		System.out.print("wrts\n");
	}
	
	/**
	 * Generates code to write newline
	 */
	public void genWriteLnStmt() {
		System.out.print("wrtln #\'\'\n");
	}
        
        /*
 public void genArithmetic(Symbol leftRec, Symbol opRec, Symbol rightRec, Symbol resultRec) {
		String operation = null;
		boolean integerDiv = false,
				floatDiv = false,
				booleanOp = false, 
				relOp = false;		// if relational op, set resulting type to boolean
		switch(opRec.lexeme.toLowerCase()) {
			case "+":
				operation = "adds";
				break;
			case "-":
				operation = "subs";
				break;
			case "*":
				operation = "muls";
				break;
			case "div":
				integerDiv = true;
				operation = "divs";
				break;
			case "/":
				floatDiv = true;
				operation = "divs";	// 'f' added below
				break;
			case "mod":
				if(leftRec.type == Symbol.Type.FLOAT || rightRec.type == Symbol.Type.FLOAT)
					semanticError("Modulo operator cannot be applied to float type");
				operation = "mods";
				break;
			case "=":
				operation = "cmpeqs";	
				booleanOp = true;	// = allowed for booleans
				relOp = true;
				break;
			case ">=":
				operation = "cmpges";
				relOp = true;
				break;
			case ">":
				operation = "cmpgts";
				relOp = true;
				break;
			case "<=":
				operation = "cmples";
				relOp = true;
				break;
			case "<":
				operation = "cmplts";
				relOp = true;
				break;
			case "<>":
				operation = "cmpnes";
				booleanOp = true;	// <> allowed for booleans
				relOp = true;
				break;
			case "and":
				if(leftRec.type != Symbol.Type.BOOLEAN || rightRec.type != Symbol.Type.BOOLEAN)
					semanticError("Logical operations not allowed on non-boolean types");
				operation = "ands";
				booleanOp = true;
				break;
			case "or":
				if(leftRec.type != Symbol.Type.BOOLEAN || rightRec.type != Symbol.Type.BOOLEAN)
					semanticError("Logical operations not allowed on non-boolean types");
				operation = "ors";
				booleanOp = true;
				break;
		}
		if(booleanOp && leftRec.type == Symbol.Type.BOOLEAN && rightRec.type == Symbol.Type.BOOLEAN) {
			output.append(operation+"\n");
			resultRec.type = leftRec.type;
		}
		// disallow arithmetic/compare operations on boolean and string types
		else if(leftRec.type != Symbol.Type.BOOLEAN && leftRec.type != Symbol.Type.STRING) {
			if(leftRec.type == rightRec.type) {
				resultRec.type = relOp ? Symbol.Type.BOOLEAN : leftRec.type;
				// Check for integer division operator on floats
				if(leftRec.type == Symbol.Type.FLOAT) {
					if(integerDiv) {
						// I'm not sure exactly what the semantics are for int division
						// on floats, I assume you just truncate values, then divide
						output.append("castsi\n");	// cast RHS to integer
						genCastLeftHandSide(Symbol.Type.INTEGER);	// cast LHS to integer
						resultRec.type = Symbol.Type.INTEGER;	// set expression result to integer
					}
					else // not integer div
						operation = operation + "f";	// use float operation if LHS and RHS both float
				}
				// Check for float division operator on integers
				else if(leftRec.type == Symbol.Type.INTEGER && floatDiv) {
					output.append("castsf\n");	// cast RHS to float
					genCastLeftHandSide(Symbol.Type.FLOAT);	// cast LHS to float
					resultRec.type = Symbol.Type.FLOAT;	// set expression result to float
					operation = operation + "f";	// use float operation
				}
				output.append(operation + "\n");
			}
			// Stack top needs to be casted to float:
			else if(leftRec.type == Symbol.Type.FLOAT && rightRec.type == Symbol.Type.INTEGER) {
				resultRec.type = relOp ? Symbol.Type.BOOLEAN : Symbol.Type.FLOAT;
				// unless integer div, then cast LHS to integer
				if(integerDiv) {
					genCastLeftHandSide(Symbol.Type.INTEGER); // cast LHS to integer
					resultRec.type = Symbol.Type.INTEGER;	// set expression result to integer
				}
				else { // not integer div
					output.append("castsf\n");	// cast RHS to float
					operation = operation + "f";// use float operation
				}
				output.append(operation + "\n");
			}
			// Second in from top of stack needs to be casted to float:
			else if(leftRec.type == Symbol.Type.INTEGER && rightRec.type == Symbol.Type.FLOAT) {
				resultRec.type = relOp ? Symbol.Type.BOOLEAN : Symbol.Type.FLOAT;
				// unless integer div, then cast RHS to integer
				if(integerDiv) {
					output.append("castsi\n");	// cast RHS to integer
					resultRec.type = Symbol.Type.INTEGER; // set expression result to integer
				}
				else { // not integer div
					genCastLeftHandSide(Symbol.Type.FLOAT);	// cast LHS to float
					operation = operation + "f";	// use float operation
				}
				output.append(operation + "\n");
			}
			else if(leftRec.type != rightRec.type) 
				this.semanticError("Incompatible types encountered for expression: " + leftRec.type + " " + opRec.lexeme + " " + rightRec.type);
		}
		else 
			this.semanticError("Incompatible types encountered for expression: " + leftRec.type + " " + opRec.lexeme + " " + rightRec.type);
	}


private void semanticError(String errorMsg) {
		parser.semanticError(errorMsg);
		error = true;
	}
        */
}