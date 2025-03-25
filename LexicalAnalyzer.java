import java.io.*;
import java.util.*;
import java.util.regex.*;

public class LexicalAnalyzer {
    // Enum for different error categories
    enum ErrorCategories { LEXICAL, SYNTAX, GENERAL_SEMANTIC, DUPLICATE_IDENTIFIER, UNDECLARED }

    // List to store error messages
    private static Queue<String> errorMessages = new LinkedList<>();
    private static int lexicalErrors = 0;
    private static int syntacticErrors = 0;
    private static int semanticErrors = 0;
    private static int totalErrors = 0;
    private static int lineNumber = 1;

    // Define regex patterns for various lexemes and tokens
    private static final String LINE = "\n";
    private static final String WHITESPACE = "[\\t\\r]+";
    private static final String COMMENT = "//.*";
    private static final String COMMENT2 = "--.*";
    private static final String RESERVED_WORDS = "(function|main|returns|integer|real|if|then|else|begin|end|case|switch)";
    private static final String OPERATORS = "(\\+\\+|--|\\+=|-=|==|!=|<=|>=|&&|\\|\\|[+\\-*/%^=<>!&|])";// Updated regex for multi-character operators
    private static final String IDENTIFIER = "[A-Za-z]([A-Za-z0-9_]{0,2}[A-Za-z0-9])*";
    private static final String NUMBER = "[0-9]";
    private static final String REAL_LITERAL = "([0-9]+\\.[0-9]*([eE][+-]?[0-9]+)?)|([0-9]*\\.[0-9]+([eE][+-]?[0-9]+)?)"; // Matches real numbers
    private static final String HEX_LITERAL = "#[0-9A-Fa-f]";
    private static final String CHAR_LITERAL = "(.|\\\\[btnrf])\\";
    private static final String PUNCTUATION = "[\\(\\),:;]";
    private static final String INVALID_REAL = "[0-9]*\\.[0-9]+\\.[0-9]*(\\.[0-9]+)*";
    
    
    // Function to process the input file
    public void analyzeFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                analyzeLine(line);
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to analyze each line
    private void analyzeLine(String line) {
        // First, remove any comments
        line = line.replaceAll(COMMENT, "");

        String tokenPattern = RESERVED_WORDS + "|" + OPERATORS + "|" + IDENTIFIER + "|" + NUMBER + "|" 
                + REAL_LITERAL + "|" + HEX_LITERAL + "|" + CHAR_LITERAL + "|" + PUNCTUATION;

        
        // Create a pattern matcher
        Pattern pattern = Pattern.compile(tokenPattern);
        Matcher matcher = pattern.matcher(line);

        // Loop through all matches
        while (matcher.find()) {
            String token = matcher.group();

            
            // Debug print to show every token found
            //System.out.println("||Token|| " + token);
            
            // Check the type of the token
            if (token.matches(RESERVED_WORDS)) {
                System.out.println("Line " + lineNumber + ": RESERVED WORD: " + token);
            } else if (token.matches(OPERATORS)) {
                System.out.println("Line " + lineNumber + ": OPERATOR: " + token);
            } else if (token.matches(IDENTIFIER)) {
                System.out.println("Line " + lineNumber + ": IDENTIFIER: " + token);
            } else if (token.matches(NUMBER)) {
                System.out.println("Line " + lineNumber + ": INTEGER: " + token);
            } else if (token.matches(REAL_LITERAL)) {
                System.out.println("Line " + lineNumber + ": REAL LITERAL: " + token);
            } else if (token.matches(HEX_LITERAL)) {
                System.out.println("Line " + lineNumber + ": HEX LITERAL: " + token);
            } else if (token.matches(CHAR_LITERAL)) {
                System.out.println("Line " + lineNumber + ": CHAR LITERAL: " + token);
            } else if (token.matches(PUNCTUATION)) {
                System.out.println("Line " + lineNumber + ": PUNCTUATION: " + token);
            } else {
                appendError(ErrorCategories.LEXICAL, "Invalid Token '" + token + "'");
                appendError(ErrorCategories.SYNTAX, "Invalid Token '" + token + "'");
                appendError(ErrorCategories.GENERAL_SEMANTIC, "Invalid Token '" + token + "'");
                appendError(ErrorCategories.DUPLICATE_IDENTIFIER, "Invalid Token '" + token + "'");
                appendError(ErrorCategories.UNDECLARED, "Invalid Token '" + token + "'");
            } 
            
        }
        
    }

    // Function to append errors
    private void appendError(ErrorCategories errorCategory, String message) {
        String errorMessage = "";
        
        
        switch (errorCategory) {
            case LEXICAL:
                errorMessage = "Lexical Error, Invalid Token: " + message;
                lexicalErrors++;
                break;
            case SYNTAX:
                errorMessage = "Syntax Error: " + message;
                syntacticErrors++;
                break;
            case GENERAL_SEMANTIC:
                errorMessage = "Semantic Error: " + message;
                semanticErrors++;
                break;
            case DUPLICATE_IDENTIFIER:
                errorMessage = "DUP Error: " + message;
                lexicalErrors++;
                break;
            case UNDECLARED:
                errorMessage = "UNDCL Error: " + message;
                lexicalErrors++;
                break;
            default:
                errorMessage = "Unknown Error: " + message;
                break;
        }
        errorMessages.add(errorMessage);
        totalErrors++;
    }

    // Function to print the errors or success message
    public void printErrors() {
        if (errorMessages.isEmpty()) {
            System.out.println("Compiled Successfully");
        } else {
            while (!errorMessages.isEmpty()) {
                System.out.println(errorMessages.poll());
            }
        }
        System.out.println("Lexical Errors: " + lexicalErrors);
        System.out.println("Syntactic Errors: " + syntacticErrors);
        System.out.println("Semantic Errors: " + semanticErrors);
        System.out.println("Total Errors: " + totalErrors);
    }
}