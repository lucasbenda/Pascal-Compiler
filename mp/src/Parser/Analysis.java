package Parser;

import SymbolTable.*;
import Tokenizer.Token;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Analysis {

    PrintStream output;
    int stackSize;
    
    public Analysis() throws FileNotFoundException {     
        this.output = new PrintStream(new File("machinecode1.txt"));
        stackSize = 0;

    }

    public void close() {
        this.output.close();

    }
    /*
     * generates wrts
     */

    public void genWriteStmt() {
        output.append("wrts\n");
    }

    /**
     * Pushes machine code for newline
     */
    public void genWriteLnStmt() {
        output.append("wrtln #\"\"\n");
    }

    /*
     * generates a read statement
     */
    public void genReadStmt(Token paramRec) {
        if (paramRec != null) {
            Row transfer = NonTerminals.symTab.findVariable(paramRec.lexeme);
            Token var = new Token();
            rowToToken(transfer, var);

            if (var != null) {
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
                    case "boolean":
                        rdOp = "rd";
                        break;
                    default:

                        break;
                }
                output.append(rdOp + " " + var.offset + "(D" + var.nestingLevel + ")\n");
            }
        }

    }

    public void genArithmetic(Token leftRec, Token opRec, Token rightRec, Token resultRec) {
        String operation = null;
        boolean integerDiv = false,
                floatDiv = false,
                booleanOp = false,
                relOp = false;		
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
                booleanOp = true;	
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
                booleanOp = true;	
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
            output.append(operation + "\n");
            resultRec.setType(leftRec.getType());
        } 
        else if (!leftRec.getType().equals("boolean") && !leftRec.getType().equals("string")) {
            if (leftRec.getType().equals(rightRec.getType())) {

                if (relOp) {
                    resultRec.setType("boolean");
                } else {
                    resultRec.setType(leftRec.getType());
                }
                if (leftRec.getType().equals("float")) {
                    if (integerDiv) {
                        output.append("castsi\n");	
                        genCastLeftHandSide("integer");	
                        resultRec.setType("integer");	
                    } else // not integer div
                    {
                        operation = operation + "f";	// use float operation if LHS and RHS both float
                    }
                }
                else if (leftRec.getType().equals("integer") && floatDiv) {
                    output.append("castsf\n");	
                    genCastLeftHandSide("float");
                    resultRec.setType("float");
                    operation = operation + "f";
                }
                output.append(operation + "\n");
            } 
            else if (leftRec.getType().equals("float") && rightRec.getType().equals("integer")) {
                if (relOp) {
                    resultRec.setType("boolean");
                } else {
                    resultRec.setType("float");
                }

                if (integerDiv) {
                    genCastLeftHandSide("integer");
                    resultRec.setType("integer");	
                } else { 
                    output.append("castsf\n");	
                    operation = operation + "f";
                }
                output.append(operation + "\n");
            } 
            else if (leftRec.getType().equals("integer") && rightRec.getType().equals("float")) {
                if (relOp) {
                    resultRec.setType("boolean");
                } else {
                    resultRec.setType("float");
                }

                if (integerDiv) {
                    output.append("castsi\n");	
                    resultRec.setType("integer");
                } else { 
                    genCastLeftHandSide("float");
                    operation = operation + "f";
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
            output.append("castsf\n");	
        } else if (id.getType().equals("integet") && expr.getType().equals("float")) {
            output.append("castsi\n");	
        } else if (!id.getType().equals(expr.getType())) {
            this.semanticError("Incompatible types encountered for assignement statement: " + id.getType() + " := " + expr.getType());
        }
        Row var = NonTerminals.symTab.findVariable(id.getLexeme());
        output.append("pop " + " " + var.getOffset() + "(D" + var.getNestingLevel() + ")\n");
    }

    private void genCastLeftHandSide(String type) {
        String castOp;
        if (type.equals("float")) {
            castOp = "castsf\n";
        } else {
            castOp = "castsi\n";
        }

        output.append("push -2(SP)\n");	
        output.append(castOp);				
        output.append("pop -2(SP)\n");		
    }

    public void genPushLiteral(Token literalRec, Token signRec) {
        String literal;
        if (literalRec.getType().equals("string")) {
            literal = "\"" + literalRec.lexeme + "\"";
        } else {
            literal = literalRec.lexeme;
        }
        if (signRec.negative) {
            literal = "-" + literal;
        }
        output.append("push #" + literal + "\n");	
    }

    public void genPushId(Token idRec, Token signRec) {
        Row var = NonTerminals.symTab.findVariable(idRec.getLexeme());

        output.append("push " + " " + var.getOffset() + "(D" + var.getNestingLevel() + ")\n");
        if (signRec.negative) {
            this.genNegOp(idRec);
            
        }
    }

    public void genPushBoolLit(Token boolLit) {
        String bool;
        if (boolLit.lexeme.equalsIgnoreCase("true")) {
            bool = "1";
        } else {
            bool = "0";
        }
        output.append("push #" + bool + "\n");

    }

    public void genNotOp(Token factorRec) {
        if (factorRec.getType().equals("boolean")) {
            output.append("NOTS\n");
        } else {
            this.semanticError("'not' used for non-boolean expression type");
        }
    }

    public void genNegOp(Token factorRec) {
        if (factorRec.getType().equals("float") || factorRec.getType().equals("integet")) {
            String negOp;
            if (factorRec.getType().equals("float")) {
                negOp = "negsf\n";
            } else {
                negOp = "negs\n";
            }
            output.append(negOp);	
        } else {
            this.semanticError("'-' used for non-numeric expression type");
        }
    }


    public void copy(Token existing, Token copy) {
        copy.setType(existing.getType());
        copy.lexeme = existing.lexeme;
    }

    /*
     * not sure if this will be needed yet
     */
    public void rowToToken(Row in, Token out) {
        out.setType(in.getType());
        out.setKind(in.getKind());
        out.size = in.getSize();
        out.lexeme = in.getID();
        out.offset = in.getOffset();
        out.nestingLevel = in.getNestingLevel();


    }

    public void beginProgDec(){
        output.append("MOV SP D" + NonTerminals.symTab.getNestingLevel() + "\n");
        output.append("ADD SP #" + NonTerminals.symTab.getSize() + " SP\n");
        
    }
    public void endProgDec(){
        output.append("HLT\n");
    }
    

    private void semanticError(String errorMsg) {
        output.append(errorMsg);
    }
}
