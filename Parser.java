import java.util.*;
public class Parser {
    private static final Map<String, String> TOKEN_CLASSES = new HashMap<>();
    static {
        TOKEN_CLASSES.put("if", "KEYWORD");
        TOKEN_CLASSES.put("else", "KEYWORD");
        TOKEN_CLASSES.put("while", "KEYWORD");
        TOKEN_CLASSES.put("int", "KEYWORD");
        TOKEN_CLASSES.put("float", "KEYWORD");
        TOKEN_CLASSES.put("+", "OP");
        TOKEN_CLASSES.put("-", "OP");
        TOKEN_CLASSES.put("*", "OP");
        TOKEN_CLASSES.put("/", "OP");
        TOKEN_CLASSES.put("(", "LPAR");
        TOKEN_CLASSES.put(")", "RPAR");
        TOKEN_CLASSES.put("{", "LBRACE");
        TOKEN_CLASSES.put("}", "RBRACE");
        TOKEN_CLASSES.put(";", "SEMICOLON");
        TOKEN_CLASSES.put(">", "RELOP");
        TOKEN_CLASSES.put("<", "RELOP");
        TOKEN_CLASSES.put(">=", "RELOP");
        TOKEN_CLASSES.put("<=", "RELOP");
        TOKEN_CLASSES.put("==", "RELOP");
        TOKEN_CLASSES.put("=", "RELOP");
    }

    public static void tokenize(String input) {

        boolean inQuotes = false;
        StringBuilder modifiedInput = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == '\"') inQuotes = !inQuotes;
            if (inQuotes && c == ' ') modifiedInput.append("FUCKYOU!");
            else modifiedInput.append(c);
        }
    
        input = modifiedInput.toString()
                .replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", "")
                .replaceAll("\"(.*?)\"", " \"$1\" ")
                .replace("+", " + ")
                .replace("-", " - ")
                .replace("*", " * ")
                .replace("/", " / ")
                .replace("(", " ( ")
                .replace(")", " ) ")
                .replace("{", " { ")
                .replace("}", " } ")
                .replace(";", " ; ");
    
        String[] parts = input.split("\\s+");
        List<String> tokens = new ArrayList<>();
    
        for (String part : parts) {
            if(part==" ") continue;
            if (part.startsWith("\"") && part.endsWith("\"")) {
                part=part.replace("FUCKYOU!"," ");
                System.out.println("<STRING, " + part + ">");
            } else if (TOKEN_CLASSES.containsKey(part)) {
                System.out.println("<" + TOKEN_CLASSES.get(part) + ", " + part + ">");
            } else if (part.startsWith("//")) {
                System.out.println("<COMMENT, " + part + ">");
            }else if (part.matches("[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")) {
                tokens.add("<NUMBER, " + part + ">");
            } else {
                System.out.println("<UNKNOWN, " + part + ">");
            }
        }
    }



    public static void main(String[] args) {
        String input = " if ( x >= 0 ) { print ( \"Hello World\" ) ; } else { int sum = 0 ; for ( int i = 0 ; i < 10 ; i = i + 1 ) { sum = i + 12.34 + 21E-2 + .21 ; } } ;";
        tokenize(input);  
    }
}