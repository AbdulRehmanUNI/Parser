import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Parser {
    private static final Map<String, String> TOKEN_CLASSES = new HashMap<>();
    static {
        TOKEN_CLASSES.put("if", "KEYWORD");
        TOKEN_CLASSES.put("else", "KEYWORD");
        TOKEN_CLASSES.put("while", "KEYWORD");
        TOKEN_CLASSES.put("int", "KEYWORD");
        TOKEN_CLASSES.put("float", "KEYWORD");
        TOKEN_CLASSES.put("print", "KEYWORD");
        TOKEN_CLASSES.put("do", "KEYWORD");
        TOKEN_CLASSES.put("break", "KEYWORD");
        TOKEN_CLASSES.put("continue", "KEYWORD");
        TOKEN_CLASSES.put("void", "KEYWORD");
        TOKEN_CLASSES.put("char", "KEYWORD");
        TOKEN_CLASSES.put("double", "KEYWORD");
        TOKEN_CLASSES.put("long", "KEYWORD");
        TOKEN_CLASSES.put("short", "KEYWORD");
        TOKEN_CLASSES.put("switch", "KEYWORD");
        TOKEN_CLASSES.put("case", "KEYWORD");
        TOKEN_CLASSES.put("default", "KEYWORD");
        TOKEN_CLASSES.put("for", "KEYWORD");    
        TOKEN_CLASSES.put("return", "KEYWORD");
        TOKEN_CLASSES.put("true", "KEYWORD");
        TOKEN_CLASSES.put("false", "KEYWORD");
        TOKEN_CLASSES.put("String", "KEYWORD");
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

    public static void tokenizeCode(String input) {
        input=input.trim();
        boolean inQuotes = false;
        StringBuilder modifiedInput = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\"') inQuotes = !inQuotes;
            //Replace spaces with OlWYA (Encoded form of space)
            if (inQuotes && c == ' ') modifiedInput.append("OLWYA!");
            else if ((c == '=' || c == '>' || c == '<' || c == '/') && !inQuotes) {
                if (i < input.length() - 1 && (input.charAt(i + 1) == '=' || input.charAt(i + 1) == c)) {
                    // If the current and next characters are both '=', '>', '<', '/' or the same, append them without spaces
                    modifiedInput.append(c).append(input.charAt(i + 1));
                    i++; // Skip the next character
                } else {
                    // If the current character is '=', '>', '<', '/' and it's not part of '==', '>=', '<=', '>>', '<<', '//' add spaces around it
                    modifiedInput.append(" ").append(c).append(" ");
                }
            } else {
                modifiedInput.append(c);
            }
        }//for
    
        input = modifiedInput.toString()
                .replaceAll("(\\+|-)", " $1 ")
                .replaceAll("(E|e) - ", "$1-")
                .replace("*", " * ")
                .replace("(", " ( ")
                .replace(")", " ) ")
                .replace("{", " { ")
                .replace("}", " } ")
                .replace(";", " ; ")
                .replace("==", " == ")
                .replace(">=", " >= ")
                .replace("<=", " <= ")
                .replace("=>", " => ");
    
        

        String[] newLines = input.split("\n");
        ArrayList<String> errors=new ArrayList<>();
        int lineNo=0, totalErrors=0;
        for (int j=0; j<newLines.length; j++) {
            String newLine = newLines[j];
            lineNo++;
            if (newLine.startsWith("//")) {
                System.out.println("<COMMENT, " + newLine.trim() + ">");
            } else {
                String[] parts = newLine.split("\\s+");
                for (String part : parts) {
                    
                    part=part.trim();
                    part=part.replace("\\t","").replace(   "\\r","");
                    if (part.isEmpty()) continue;
                    
                    if (part.startsWith("\"") && part.endsWith("\"")) {
                        part=part.replace("OLWYA!"," ");
                        if (part.matches("\"[a-zA-Z ]*\"") || part.matches("\"[0-9 ]*\"")) {
                            System.out.println("<STRING, " + part + ">");
                        }else{
                            totalErrors++;
                            newLine=newLine.replace("OLWYA!"," ");
                            part=part.replace("OLWYA!"," ");
                            System.out.println("<ERROR, INVALID STRING, " + part + ">");
                            errors.add("input.txt:"+lineNo+" invalid string");
                            errors.add("\t"+newLine);
                            // add ^ under the invalid string
                            int index = newLine.indexOf(part);
                            String temp="";
                            for (int i = 0; i < index; i++) temp+=" ";
                            temp+=("^");
                            errors.add("\t"+temp+"\n");
                    }        
                    } else if (TOKEN_CLASSES.containsKey(part)) {
                        String op=printOperators(part);
                        System.out.println("<" + TOKEN_CLASSES.get(part) + ", " + op+ ">");
                    } else if (part.startsWith("//")) {
                        System.out.println("<COMMENT, " + part + ">");
                    } else if(part.matches("[0-9]+\\.?[0-9]*([eE][-+]?[0-9]+)?")){
                        System.out.println("<NUMBER, "+ part+">");
                    } else if(part.equals("COMMENT")){
                        System.out.println("<COMMENT>");
                    } else if(part.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")){
                        System.out.println("<ID, " + part + ">");                       
                    } else if (part.matches("[0-9]*\\.?[0-9]*\\.?[0-9]*[a-zA-Z_]*")) {
                        totalErrors++;
                        System.out.println("<ERROR, invalid number format: " + part + ">");
                        errors.add("input.txt:"+lineNo+" invalid number format");
                        errors.add("\t"+newLine);
                        // add ^ under the invalid number
                        int index = newLine.indexOf(part);
                        String temp="";
                        for (int i = 0; i < index; i++) temp+=" ";
                        temp+=("^");
                        errors.add("\t"+temp+"\n");
                            
                     } 

            }//inner-for
        }//else
    }//outer-for

    //Error Displaying

        System.out.println("\n\n\n");
        System.out.println("Errors: "+ totalErrors);
        for(String error: errors) System.out.println(error);

}//tokenize()
    

    public static String printOperators(String str){
        str=str.trim();  
        if(str.equals("=")) return "EQ";
        if(str.equals(">=")) return "GEQ";
        if(str.equals("<=")) return "LEQ";
        if(str.equals(">")) return "GT";
        if(str.equals("<")) return "LT";
        else return str;
    }//printOperators()

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }//while
            reader.close();
            String input = sb.toString().replaceAll("\t","")
                .replaceAll("\r","");
            tokenizeCode(input);
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch

    }//main
}//class