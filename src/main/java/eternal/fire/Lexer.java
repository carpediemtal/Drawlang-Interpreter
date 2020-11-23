package eternal.fire;

import java.io.IOException;

public class Lexer {
    private final char EOF = '!';
    private String src;
    private int index;

    public Lexer() {
        try {
            src = Read.readSrc();
        } catch (IOException e) {
            System.out.println("Src not found");
            e.printStackTrace();
        }
    }

    private Token getToken() {
        Token token = new Token();
        char ch = getChar();

        return token;
    }

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
}
