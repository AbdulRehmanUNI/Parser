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
        input=input.trim();
        boolean inQuotes = false;
        StringBuilder modifiedInput = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\"') inQuotes = !inQuotes;
            if (inQuotes && c == ' ') modifiedInput.append("DUCKYOU!");
            else if (c == '=' && !inQuotes) {
                if (i < input.length() - 1 && input.charAt(i + 1) == '=') {
                    // If the current and next characters are both '=', append them without spaces
                    modifiedInput.append("==");
                    i++; // Skip the next character
                } else {
                    // If the current character is '=' and it's not part of '==', add spaces around it
                    modifiedInput.append(" = ");
                }
            } else {
                modifiedInput.append(c);
            }
        }
    
        input = modifiedInput.toString()
                // .replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", " COMMENT ")
                .replaceAll("\"(.*?)\"", " \"$1\" ")
                .replace("+", " + ")
                .replace("-", " - ")
                .replace("*", " * ")
                .replace("/", " / ")
                .replace("(", " ( ")
                .replace(")", " ) ")
                .replace("{", " { ")
                .replace("}", " } ")
                .replace(";", " ; ")
                .replace("==", " == ")
                .replace(">=", " >= ")
                .replace("<=", " <= ")
                .replace(">", " > ")
                .replace("<", " < ")
                .replace("=>", " => ");
    
        String[] parts = input.split("\\s+");
    
        for (String part : parts) {
            if (part.startsWith("\"") && part.endsWith("\"")) {
                part=part.replace("DUCKYOU!"," ");
                System.out.println("<STRING, " + part + ">");
            } else if (TOKEN_CLASSES.containsKey(part)) {
                String op=printOperators(part);
                System.out.println("<" + TOKEN_CLASSES.get(part) + ", " + op+ ">");
            } else if (part.startsWith("//")) {
                System.out.println("<COMMENT, " + part + ">");
            }else if(part.matches("[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")){
                System.out.println("<NUMBER, "+ part+">");
            }else if(part.equals("COMMENT")){
                System.out.println("<COMMENT>");
            } 
            else {
                System.out.println("<ID, " + part + ">");
            }
        }
    }

    public static String printOperators(String str){
        str=str.trim();  
        if(str.equals("=")) return "EQ";
        if(str.equals("=>")) return "GEQ";
        if(str.equals("<=")) return "LEQ";
        if(str.equals(">")) return "GT";
        if(str.equals("<")) return "LT";
        else return str;
    }


    public static void main(String[] args) {
        String input = "if(x >= 0 ) { print ( \"Hello World\" + \"1.001\" ) ; } else { int sum=0 ; for ( int i = 0 ; i < 10 ; i = i + 1 ) { sum = i + 12.34+21E-2 + .21 ; } a==b } ;";
        tokenize(input);  
    }
}