package eternal.fire;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Lexer {
    private final char EOF = '!';
    private String src;
    private int index;
    private int lineNum;
    private StringBuilder tokenString = new StringBuilder();
    //    private Set<Token> tokenTable = new HashSet<>();
    private Map<String, Token> tokenTable = new HashMap<>();

    public Lexer() {
        tokenTable.put("PI", new Token(TokenType.CONST_ID, "PI", 3.1415926));
        tokenTable.put("E", new Token(TokenType.CONST_ID, "E", 2.71828));
        tokenTable.put("T", new Token(TokenType.T, "T", 0));
        tokenTable.put("SIN", new Token(TokenType.FUNC, "SIN", 0));
        tokenTable.put("COS", new Token(TokenType.FUNC, "COS", 0));
        tokenTable.put("TAN", new Token(TokenType.FUNC, "TAN", 0));
        tokenTable.put("LN", new Token(TokenType.FUNC, "LN", 0));
        tokenTable.put("EXP", new Token(TokenType.FUNC, "EXP", 0));
        tokenTable.put("SQRT", new Token(TokenType.FUNC, "SQRT", 0));
        tokenTable.put("ORIGIN", new Token(TokenType.ORIGIN, "ORIGIN", 0));
        tokenTable.put("SCALE", new Token(TokenType.SCALE, "SCALE", 0));
        tokenTable.put("ROT", new Token(TokenType.ROT, "ROT", 0));
        tokenTable.put("IS", new Token(TokenType.IS, "IS", 0));
        tokenTable.put("FOR", new Token(TokenType.FOR, "FOR", 0));
        tokenTable.put("FROM", new Token(TokenType.FROM, "FROM", 0));
        tokenTable.put("TO", new Token(TokenType.TO, "TO", 0));
        tokenTable.put("STEP", new Token(TokenType.STEP, "STEP", 0));
        tokenTable.put("DRAW", new Token(TokenType.DRAW, "DRAW", 0));

        try {
            src = Read.readSrc();
        } catch (IOException e) {
            System.out.println("Src not found");
            e.printStackTrace();
        }
    }

    // 每次调用获得一个Token
    private Token getToken() {
        Token token = new Token();
        char ch = getChar();
        if (ch == '!') {
            token.setTokenType(TokenType.END);
            return token;
        }
        tokenString.append(ch);
        // 识别ID:字母开头，和数字混合
        if (Character.isAlphabetic(ch)) {
            while (true) {
                ch = getChar();
                if (Character.isAlphabetic(ch) || Character.isDigit(ch)) {
                    tokenString.append(ch);
                } else {
                    break;
                }
            }
            index--;
            token = judgeKeyToken(tokenString.toString());
        } else if (Character.isDigit(ch)) { // 识别数字常量
            while (true) {
                ch = getChar();
                if (Character.isDigit(ch)) {
                    tokenString.append(ch);
                } else {
                    break;
                }
            }
            if (ch == '.') {
                tokenString.append(ch);
                while (true) {
                    ch = getChar();
                    if (Character.isDigit(ch)) {
                        tokenString.append(ch);
                    } else {
                        break;
                    }
                }
            }
            index--;
            return new Token(TokenType.CONST_ID, tokenString.toString(), Double.parseDouble(tokenString.toString()));
        } else { // 不是字母数字，就是运算符或者分隔符
            switch (ch) {
                case ';' -> {
                    token.setTokenType(TokenType.SEMICOLON);
                    return token;
                }
                case '(' -> {
                    return new Token(TokenType.L_BRACKET, "(", 0);
                }
                case ')'->{
                    return new Token(TokenType.R_BRACKET, ")", 0);
                }
                case ','->{
                    return new Token(TokenType.COMMA, ",", 0);
                }
                case '+'->{
                    return new Token(TokenType.PLUS, "+", 0);
                }
                case '-'->{
                    return new Token(TokenType.MINUS, "-", 0);
                }

                default -> {
                    return new Token(TokenType.ERROR, "ERROR", 0);
                }
            }
        }
        return token;
    }

    // 查表：查不到就返回ERROR类型的TOKEN
    private Token judgeKeyToken(String tokenString) {
        if (tokenTable.containsKey(tokenString)) {
            return tokenTable.get(tokenString);
        } else {
            return new Token(TokenType.ERROR, "ERROR", 0);
        }
    }

    // 读取源代码中的一个字符，跳过空白字符
    private char getChar() {
        while (true) {
            char ch = src.charAt(index);
            if (Character.isSpaceChar(ch) || ch == '\n') {
                index++;
            } else {
                index++;
                return ch;
            }
        }
    }

    private int scanAndMove() {

    }

    private void backChar() {
        index--;
    }
}
