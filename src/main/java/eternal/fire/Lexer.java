package eternal.fire;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    /**
     * 源代码文件名，如{@code src.txt}
     */
    private final String fileName;
    /**
     * 用来标识源代码已经读到了末尾
     */
    private static final char EOF = '!';
    /**
     * 从src.txt文件读取的源代码
     */
    private String src;
    /**
     * 读取源代码过程中的位置
     */
    private int index = 0;
    /**
     * TokenString到Token的Map
     */
    private final Map<String, Token> tokenTable = new HashMap<>();

    /**
     * Lexer的构造函数，初始化{@code TokenTable}，使用默认的“src.txt”初始化源代码{@code src}
     *
     * @throws IOException src初始化错误
     */
    public Lexer() throws IOException {
        fileName = "src.txt";
        initTable();
        initSrc();
        System.out.println("Lexer build complete");
    }

    /**
     * Lexer的构造函数，初始化TokenTable，使用{@code fileName}初始化源代码{@code src}
     *
     * @param fileName 源代码文件名，例如“src.txt”
     * @throws IOException 找不到文件
     */
    public Lexer(String fileName) throws IOException {
        this.fileName = fileName;
        initTable();
        initSrc();
    }

    /**
     * 每次调用获得一个Token
     *
     * @return 返回下一个Token
     */
    public Token getToken() {
        // 跳过最初的空白字符
        jumpSpace();

        // 读到了源代码末尾
        if (index >= src.length()) {
            return new Token(TokenType.END, "END", 0);
        }

        // 存储Token字符
        StringBuilder tokenString = new StringBuilder();

        // 第一个非空字符
        char ch = getChar();
        tokenString.append(ch);
        // 识别保留字、常量名和参数名:纯字母
        if (Character.isAlphabetic(ch)) {
            while (true) {
                ch = getChar();
                if (Character.isAlphabetic(ch)) {
                    tokenString.append(ch);
                } else {
                    break;
                }
            }
            index--;
            return lookUpTable(tokenString.toString());
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
            return new Token(TokenType.CONST_VAL, tokenString.toString(), Double.parseDouble(tokenString.toString()));
        } else {
            // 识别运算符或者分隔符，跳过注释
            switch (ch) {
                case ';' -> {
                    return new Token(TokenType.SEMICOLON, ";", 0);
                }
                case '(' -> {
                    return new Token(TokenType.L_BRACKET, "(", 0);
                }
                case ')' -> {
                    return new Token(TokenType.R_BRACKET, ")", 0);
                }
                case ',' -> {
                    return new Token(TokenType.COMMA, ",", 0);
                }
                case '+' -> {
                    return new Token(TokenType.PLUS, "+", 0);
                }
                // comment or minus
                case '-' -> {
                    ch = getChar();
                    // 跳过注释
                    if (ch == '-') {
                        while (ch != '\n' && ch != '!') {
                            ch = getChar();
                        }
                        index--;
                        return getToken();
                    } else {
                        index--;
                        return new Token(TokenType.MINUS, "-", 0);
                    }
                }
                case '*' -> {
                    ch = getChar();
                    if (ch == '*') {
                        return new Token(TokenType.POWER, "**", 0);
                    } else {
                        index--;
                        return new Token(TokenType.MUL, "*", 0);
                    }
                }
                // comment or div
                case '/' -> {
                    ch = getChar();
                    if (ch == '/') {
                        while (ch != '\n' && ch != '!') {
                            ch = getChar();
                        }
                        index--;
                        return getToken();
                    } else {
                        index--;
                        return new Token(TokenType.DIV, "/", 0);
                    }
                }
                // 不能识别的字符返回ERROR类型的Token
                default -> {
                    return new Token(TokenType.ERROR, "ERROR", 0);
                }
            }
        }
    }

    /**
     * 跳过空白字符，index指向index后第一个不为空的字符或者最后一个字符
     */
    private void jumpSpace() {
        while (index < src.length()) {
            if (src.charAt(index) == '\n' || src.charAt(index) == ' ') {
                index++;
            } else {
                break;
            }
        }
    }

    /**
     * 查TokenTable表返回Token
     *
     * @param tokenString token对应的字符串
     * @return 返回tokenString对应的Token
     */
    private Token lookUpTable(String tokenString) {
        if (tokenTable.containsKey(tokenString)) {
            return tokenTable.get(tokenString);
        } else {
            return new Token(TokenType.ERROR, "ERROR", 0);
        }
    }

    /**
     * 读取源代码中的一个字符
     *
     * @return 返回源代码中的一个字符（包含空白字符），返回EOF表示已经读到了源代码末尾
     */
    private char getChar() {
        if (index >= src.length()) {
            return EOF;
        }
        return src.charAt(index++);
    }

    /**
     * 初始化TokenTable
     */
    private void initTable() {
        tokenTable.put("PI", new Token(TokenType.CONST_VAL, "PI", 3.1415926));
        tokenTable.put("E", new Token(TokenType.CONST_VAL, "E", 2.71828));
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
    }

    /**
     * 初始化源代码{@code src}
     * @throws IOException 找不到文件
     */
    private void initSrc() throws IOException {
        Reader reader = new Reader(fileName);
        src = reader.readSrc().toUpperCase();
    }

    /**
     * 测试lexer
     */
    public void lexerTest() throws IOException {
        while (true) {
            Token token = getToken();
            System.out.println(token);
            if (token.getTokenType() == TokenType.END) {
                break;
            }
        }
    }
}
