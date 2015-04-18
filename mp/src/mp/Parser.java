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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Parser {

    private Scan initScanner;
    private String text[][];
    private Token lookaheadToken;
    private Type lookahead;
    private Token matched;

    //private final SymbolTable table;
    public Parser(String init[][]) {
        initScanner = new Scan();
        text = init;
        prime();
        systemGoal();
        //table = new SymbolTable();
    }

    private void match(Type matched) {
        if (matched == lookahead) {
            prime();
        } else {
            error("Expecting " + matched.toString() + " got ");
        }
    }

    // gets next lookahead item
    private void prime() {
        matched = lookaheadToken;
        lookaheadToken = initScanner.getToken(text);
        lookahead = lookaheadToken.getType();
    }

    // Error with a custom message
    private void error(String message) {
        System.out.println(message + lookaheadToken.getLexeme() + "line = " + lookaheadToken.getLineNumber());
    }

////////////////////////////
    private void systemGoal() {
        // rule 1
        program();
        match(Type.MP_EOF);
    }

    private void program() {
        // rule 2
        programHeading();
        match(Type.MP_SCOLON);
        block();
        match(Type.MP_PERIOD);
    }

    private void programHeading() {
        // rule 3
        match(Type.MP_PROGRAM);
        programIdentifier();
    }

    private void block() {
        // rule 4
        variableDeclarationPart();
        procedureAndFunctionDeclarationPart();
        //sets the D(n) pointer for each scope, and moves the stack above the symbol table
        //semantic.startCalled(start);
        statementPart();
        //semantic.endCalled();
        //table.destroyScope();
    }

    private void variableDeclarationPart() {
        switch (lookahead) {
            // rule 5
            case MP_VAR:
                prime();
                variableDeclaration();
                match(Type.MP_SCOLON);
                variableDeclarationTail();
                return;
            // rule 6 (empty string)
            default:
                return;
        }
    }

    private void variableDeclarationTail() {
        switch (lookahead) {
            // rule 7
            case MP_IDENTIFIER:
                variableDeclaration();
                match(Type.MP_SCOLON);
                variableDeclarationTail();
                break;
            // rule 8 (empty string)
            default:
                return;
        }
    }

    private void variableDeclaration() {
        // rule 9
        identifierList();
        match(Type.MP_COLON);
        type();

    }

    private Type type() {
        switch (lookahead) {
            // rule 10
            case MP_INTEGER:
                prime();
                return Type.Integer;
            // rule 11
            case MP_FLOAT:
                prime();
                return Type.Float;
            // rule 12
            case MP_STRING:
                prime();
                return Type.String;
            // rule 13
            case MP_BOOLEAN:
                prime();
                return Type.Boolean;
            // error call
            default:
                error("Needed to find a type declaration");
                return null;
        }
    }

    private void procedureAndFunctionDeclarationPart() {
        switch (lookahead) {
            // rule 14
            case MP_PROCEDURE:
                procedureDeclaration();
                procedureAndFunctionDeclarationPart();
                return;
            // rule 15
            case MP_FUNCTION:
                functionDeclaration();
                procedureAndFunctionDeclarationPart();
                return;
            // rule 16 (empty string)
            default:
                return;
        }
    }

    private void procedureDeclaration() {
        // rule 17
        procedureHeading();
        match(Type.MP_SCOLON);
        block();
        match(Type.MP_SCOLON);
        // return from procedure: 
    }

    private void functionDeclaration() {
        // rule 18
        functionHeading();
        match(Type.MP_SCOLON);
        block();
        match(Type.MP_SCOLON);
    }

    private void procedureHeading() {
        // rule 19
        match(Type.MP_PROCEDURE);
        procedureIdentifier();
        optionalFormalParameterList();

    }

    private void functionHeading() {
        // rule 20
        match(Type.MP_FUNCTION);
        functionIdentifier();
        optionalFormalParameterList();

        match(Type.MP_COLON);

        type();
    }

    private void optionalFormalParameterList() {

        switch (lookahead) {
            // rule 21
            case MP_LPAREN:
                match(Type.MP_LPAREN);
                formalParameterSection();
                formalParameterSectionTail();
                match(Type.MP_RPAREN);
                return;
            // rule 22 Lambda
            default:
                return;
        }
    }

    private void formalParameterSectionTail() {
        switch (lookahead) {
            // rule 23
            case MP_SCOLON:
                prime();
                formalParameterSection();
                formalParameterSectionTail();
                return;
            // rule 24 (empty String)
            default:
                return;
        }
    }

    private void formalParameterSection() {
        switch (lookahead) {
            // rule 25
            case MP_IDENTIFIER:
                valueParameterSection();
                return;
            // rule 26
            case MP_VAR:
                variableParameterSection();
                return;
            default:
                error("Expected an identifier or variable");
        }
    }

    private void valueParameterSection() {
        // rule 27

        identifierList();
        match(Type.MP_COLON);
        type();
    }

    private void variableParameterSection() {
        match(Type.MP_VAR);

        match(Type.MP_COLON);

    }

    private void statementPart() {
        compoundStatement();
    }

    private void compoundStatement() {
        // rule 30
        match(Type.MP_BEGIN);

        statementSequence();
        match(Type.MP_END);
    }

    private void statementSequence() {
        statement();
        statementTail();
    }

    private void statementTail() {
        switch (lookahead) {
            // rule 32
            case MP_SCOLON:
                prime();
                statement();
                statementTail();
                return;
            // rule 33 (empty string / epsilon)
            default:
                return;
        }
    }

    private void statement() {
        switch (lookahead) {
            // rule 35
            case MP_BEGIN:
                compoundStatement();
                break;
            // rule 36
            case MP_READ:
                readStatement();
                return;
            // rule 37 MP_write or MP_WRITELN
            case MP_WRITE:
            case MP_WRITELN:
                writeStatement();
                return;
            // rule 38/43
            // these two rules have the same lookahead token, but differ by
            // symbol table context
            case MP_IDENTIFIER:
                //the identifier is a procedure
                assignmentStatement();

                return;
            // rule 39
            case MP_IF:
                ifStatement();
                return;
            // rule 40
            case MP_WHILE:
                whileStatement();
                return;
            // rule 41
            case MP_REPEAT:
                repeatStatement();
                return;
            // rule 42
            case MP_FOR:
                forStatement();
                return;
            // rule 34 (empty statement)
            default:
                emptyStatement();
                return;
        }
    }

    private void emptyStatement() {
        return;
    }

    private void readStatement() {
        // rule 45
        match(Type.MP_READ);
        match(Type.MP_LPAREN);
        readParameter();
        readParameterTail();
        match(Type.MP_RPAREN);
    }

    private void readParameterTail() {
        switch (lookahead) {
            // rule 46
            case MP_COMMA:
                prime();
                readParameter();
                readParameterTail();
                return;
            // EmptyString Rule 47
            default:
                return;
        }
    }

    private void readParameter() {
        // rule 48
        variableIdentifier();
    }

    private void writeStatement() {
        switch (lookahead) {
            // rule 49
            case MP_WRITE:
                prime();
                match(Type.MP_LPAREN);
                writeParameter();
                writeParameterTail();
                prime(); //NOT SURE IF THIS IS SUPOSED TO BE HERE
                match(Type.MP_RPAREN);
                return;
            // rule 50
            case MP_WRITELN:
                prime();
                match(Type.MP_LPAREN);
                writeParameter();
                writeParameterTail();
                match(Type.MP_RPAREN);
                return;
            default:
                error("Expected write statement");
        }
    }

    private void writeParameterTail() {
        switch (lookahead) {
            // rule 51
            case MP_COMMA:
                prime();
                writeParameter();
                writeParameterTail();
                return;
            // Rule 52 empty string
            default:
                return;
        }
    }

    private void writeParameter() {
        // rule 53
        ordinalExpression();
    }

    private void assignmentStatement() {
        // rule 54 or 55
        variableIdentifier(); // or functionIdentifier
       
        match(Type.MP_ASSIGN);
        
        expression();
        
    }

    private void ifStatement() {
        // rule 56
        match(Type.MP_IF);
        booleanExpression();
        match(Type.MP_THEN);

        statement();
        
        optionalElsePart();
    }

    private void optionalElsePart() {
        switch (lookahead) {
            // rule 57
            case MP_ELSE:
                prime();
                statement();
                return;
            // rule 58
            default:
                return;
        }
    }

    private void repeatStatement() {
        // rule 59
        match(Type.MP_REPEAT);
        statementSequence();
        match(Type.MP_UNTIL);
        booleanExpression();

    }

    private void whileStatement() {
        // rule 60
        match(Type.MP_WHILE);
        booleanExpression();

        match(Type.MP_DO);
        statement();
    }

    private void forStatement() {
        // rule 61
        match(Type.MP_FOR);
        controlVariable();
        match(Type.MP_ASSIGN);
        initialValue();

    }

    private void controlVariable() {
        // rule 62
        variableIdentifier();
    }

    private void initialValue() {
        // Rule 63
    }

    private boolean stepValue() {
        switch (lookahead) {
            // Rule 64
            case MP_TO:
                prime();
                return true;
            // Rule 65
            case MP_DOWNTO:
                prime();
                return false;
            default:
                error("Expected step value (up to/ down to)");
                return true;
        }
    }

    private void finalValue() {
        // Rule 66

    }

    private void procedureStatement() {
        // Rule 67

        optionalActualParameterList();

    }

    private void optionalActualParameterList() {
        switch (lookahead) {
            // Rule 68
            case MP_LPAREN:
                prime();
                actualParameter();
                actualParameterTail();
                match(Type.MP_RPAREN);
                return;
            // Rule 69
            default:
                return;
        }
    }

    private void actualParameterTail() {
        switch (lookahead) {
            // Rule 70
            case MP_COMMA:
                prime();
                actualParameter();
                actualParameterTail();
                return;
            // Rule 71
            default:
                return;
        }
    }

    private void actualParameter() {
        // Rule 72
    }

    private void expression() {
        // Rule 73
        simpleExpression();
        optionalRelationalPart();
        
    }

    private void optionalRelationalPart() {

        switch (lookahead) {
            // Rule 74
            case MP_EQUAL:
            case MP_GEQUAL:
            case MP_GTHAN:
            case MP_LEQUAL:
            case MP_NEQUAL:
            case MP_LTHAN:
                System.out.println("lookahead is " + lookahead.toString());
                prime();
                System.out.println("lookahead is " + lookahead.toString());
                simpleExpression();
                break;
            // Rule 75
            default:
                break;
        }
    }

    private void simpleExpression() {
        // Rule 82
        optionalSign();
        term();
        termTail();
        System.out.println("lookahead is " + lookahead.toString());
    }

    private void termTail() {

        switch (lookahead) {
            // Rule 83
            case MP_OR:
            case MP_MINUS:
            case MP_PLUS:
                prime();
                term();
                termTail();
                break;
            default:
                break;
        }
    }

    private void optionalSign() {
        switch (lookahead) {

            case MP_PLUS:
            case MP_MINUS:
                prime();
                break;
            // Rule 85
            default:
                break;
        }
    }

    private Token addingOperator() {
        switch (lookahead) {
            // Rule 88, 89, 90
            case MP_PLUS:
            case MP_MINUS:
            case MP_OR:
                prime();
                return matched;
            default:
                error("Expected adding operator");
                return null;
        }
    }

    // 91 term
    private void term() {
        // Rule 91
        factor();
        factorTail();
    }

    private void factorTail() {
        switch (lookahead) {
            // Rule 94
            case MP_TIMES:
            case MP_FLOAT_DIVIDE:
            case MP_DIV:
            case MP_MOD:
            case MP_AND:
                prime();
                factor();
                factorTail();
                break;
            default:
               
                break;
                
        }

    }

    private void multiplyingOperator() {
        switch (lookahead) {
            // Rule 94
            case MP_TIMES:
            case MP_FLOAT_DIVIDE:
            case MP_DIV:
            case MP_MOD:
            case MP_AND:
                prime();
                break;
            default:
                error("Expected multiplying operator");
                break;
        }
    }

    private void factor() {

        switch (lookahead) {
            // Rule 99, 100, 101, 102, 103
            case MP_INTEGER_LIT:
            case MP_FLOAT_LIT:
            case MP_FIXED_LIT:
            case MP_STRING_LIT:
            case MP_TRUE:
            case MP_FALSE:
                prime();
                break;
            // Rule 104
            case MP_NOT:
                // Rule 105
                prime();
                factor();
                break;
            case MP_LPAREN:
                prime();
                expression();
                match(Type.MP_RPAREN);
                break;
            // Rule 106
            case MP_IDENTIFIER:
                functionIdentifier();
                optionalActualParameterList();
                break;
            default:
                error("Factor term not found");
        }
    }

    private void programIdentifier() {
        // Rule 107
        match(Type.MP_IDENTIFIER);
    }

    private Token variableIdentifier() {
        // Rule 108
        match(Type.MP_IDENTIFIER);
        return matched;
    }

    private Token procedureIdentifier() {
        // Rule 109
        match(Type.MP_IDENTIFIER);
        return matched;
    }

    private Token functionIdentifier() {
        // Rule 110
        match(Type.MP_IDENTIFIER);
        return matched;
    }

    private void booleanExpression() {
        // Rule 111
        expression();
    }

    private void ordinalExpression() {
        // Rule 112
        expression();

    }

    private List<String> identifierList() {
        // Rule 113
        List<String> idList = new ArrayList<String>();
        match(Type.MP_IDENTIFIER);
        System.out.println(matched.getLexeme());
        idList.add(matched.getLexeme());
        identifierTail(idList);
        return idList;
    }

    private void identifierTail(List<String> idList) {
        switch (lookahead) {
            // Rule 114
            case MP_COMMA:
                prime();
                match(Type.MP_IDENTIFIER);
                idList.add(matched.getLexeme());
                identifierTail(idList);
                return;
            // Rule 115
            default:
                return;
        }
    }

}
