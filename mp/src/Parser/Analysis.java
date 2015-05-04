package Parser;

import SymbolTable.*;
import Tokenizer.Token;

/**
 *
 * @author All the groups
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

    public void genReadStmt(Row paramRec) {

        if (paramRec != null) {
            String rdOp = null;
            switch (paramRec.getType()) {
                case "integer":
                    rdOp = "rd ";
                    break;
                case "float":
                    rdOp = "rdf ";
                    break;
                case "string":
                    rdOp = "rds ";
                    break;
                default:
                    //this.semanticError("Unsupported parameter type supplied for read");
                    break;
            }
            //String dereference = var.mode == Symbol.ParameterMode.REFERENCE ? "@" : "";
            System.out.println(rdOp + " " + paramRec.getOffset() + "(D" + paramRec.getNestingLevel() + ")\n");
            System.out.println(paramRec.getID());
        }

    }

    public void genArithmetic(Token leftRec, Token opRec, Token rightRec, Token resultRec) {
        String operation = null;
        boolean integerDiv = false,
                floatDiv = false,
                booleanOp = false,
                relOp = false;		// if relational op, set resulting type to boolean
        switch (opRec.lexeme.toLowerCase()) {
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
                if (leftRec.getType().equals("float") || rightRec.getType().equals("float")) {
                    semanticError("Modulo operator cannot be applied to float type");
                }
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
                if (!leftRec.getType().equals("boolean") || !rightRec.getType().equals("boolean")) {
                    semanticError("Logical operations not allowed on non-boolean types");
                }
                operation = "ands";
                booleanOp = true;
                break;
            case "or":
                if (!leftRec.getType().equals("boolean") || !rightRec.getType().equals("boolean")) {
                    semanticError("Logical operations not allowed on non-boolean types");
                }
                operation = "ors";
                booleanOp = true;
                break;
        }
        if (booleanOp && leftRec.getType().equals("boolean") && rightRec.getType().equals("boolean")) {
            System.out.println(operation + "\n");
            resultRec.setType(leftRec.getType());
        } // disallow arithmetic/compare operations on boolean and string types
        else if (!leftRec.getType().equals("boolean") && !leftRec.getType().equals("string")) {
            if (leftRec.getType().equals(rightRec.getType())) {

                if (relOp) {
                    resultRec.setType("boolean");
                } else {
                    resultRec.setType(leftRec.getType());
                }
                // Check for integer division operator on floats
                if (leftRec.getType().equals("float")) {
                    if (integerDiv) {
                        // I'm not sure exactly what the semantics are for int division
                        // on floats, I assume you just truncate values, then divide
                        System.out.println("castsi\n");	// cast RHS to integer
                        genCastLeftHandSide("integer");	// cast LHS to integer
                        resultRec.setType("integer");	// set expression result to integer
                    } else // not integer div
                    {
                        operation = operation + "f";	// use float operation if LHS and RHS both float
                    }
                } // Check for float division operator on integers
                else if (leftRec.getType().equals("integer") && floatDiv) {
                    output.append("castsf\n");	// cast RHS to float
                    genCastLeftHandSide("float");	// cast LHS to float
                    resultRec.setType("float");	// set expression result to float
                    operation = operation + "f";	// use float operation
                }
                output.append(operation + "\n");
            } // Stack top needs to be casted to float:
            else if (leftRec.getType().equals("float") && rightRec.getType().equals("integer")) {
                if (relOp) {
                    resultRec.setType("boolean");
                } else {
                    resultRec.setType("float");
                }

                // unless integer div, then cast LHS to integer
                if (integerDiv) {
                    genCastLeftHandSide("integer"); // cast LHS to integer
                    resultRec.setType("integer");	// set expression result to integer
                } else { // not integer div
                    output.append("castsf\n");	// cast RHS to float
                    operation = operation + "f";// use float operation
                }
                output.append(operation + "\n");
            } // Second in from top of stack needs to be casted to float:
            else if (leftRec.getType().equals("integer") && rightRec.getType().equals("float")) {
                if (relOp) {
                    resultRec.setType("boolean");
                } else {
                    resultRec.setType("float");
                }

                // unless integer div, then cast RHS to integer
                if (integerDiv) {
                    output.append("castsi\n");	// cast RHS to integer
                    resultRec.setType("integer"); // set expression result to integer
                } else { // not integer div
                    genCastLeftHandSide("float");	// cast LHS to float
                    operation = operation + "f";	// use float operation
                }
                output.append(operation + "\n");
            } else if (!leftRec.getType().equals(rightRec.getType())) {
                this.semanticError("Incompatible types encountered for expression: " + leftRec.getType() + " " + opRec.getLexeme() + " " + rightRec.getType());
            }
        } else {
            this.semanticError("Incompatible types encountered for expression: " + leftRec.getType() + " " + opRec.getLexeme() + " " + rightRec.getType());
        }
    }

    /*
     * Genderates the code for the assignemnt statements
     */
    public void genAssignStmt(Token id, Token expr) {
        if (id.getType().equals("float") && expr.getType().equals("integer")) {
            output.append("castsf\n");	// cast expression result (stack top) to float to assign to id
        } else if (id.getType().equals("integet") && expr.getType().equals("float")) {
            output.append("castsi\n");	// cast expression result to integer to assign to id
        } else if (!id.getType().equals(expr.getType())) {
            this.semanticError("Incompatible types encountered for assignement statement: " + id.getType() + " := " + expr.getType());
        }
        Row var = NonTerminals.symTab.findVariable(id.getLexeme());
		//String dereference = var.mode == Symbol.ParameterMode.REFERENCE ? "@" : "";
        // assuming parser will catch undeclared id's so no need to null check
        output.append("pop " + " " + var.getOffset() + "(D" + var.getNestingLevel() + ")\n");
    }

    private void genCastLeftHandSide(String type) {
        String castOp;
        if (type.equals("float")) {
            castOp = "castsf\n";
        } else {
            castOp = "castsi\n";
        }

        output.append("push -2(SP)\n");		// Push value below value on top of stack
        output.append(castOp);				// cast value
        output.append("pop -2(SP)\n");		// then put it back where it was
    }

    public void genPushId(Token idRec, Token signRec) {
        Row var = NonTerminals.symTab.findVariable(idRec.getLexeme());
        if (mode == Symbol.ParameterMode.REFERENCE && var.mode == Symbol.ParameterMode.REFERENCE) {
			// if a reference mode formal parameter is used as an actual reference mode parameter 
            // in a function call inside of a function, we have already calculated its address 
            output.append("push " + var.offset + "(D" + var.nestLevel + ")\n");
        } else if (mode == Symbol.ParameterMode.REFERENCE) {
            output.append("push D" + var.getNestingLevel() + "\n");
            output.append("push #" + var.getOffset() + "\n");
            output.append("adds\n");	// calculate variable address
        } else {
            String dereference = var.mode == Symbol.ParameterMode.REFERENCE ? "@" : "";	// if ref mode, dereference
            output.append("push " + dereference + var.offset + "(D" + var.nestLevel + ")\n");
            if (signRec.negative) {
                this.genNegOp(idRec);
            }
        }
    }

    private void semanticError(String errorMsg) {
        parser.semanticError(errorMsg);
        error = true;
    }
}
