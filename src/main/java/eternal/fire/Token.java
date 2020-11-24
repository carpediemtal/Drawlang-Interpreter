package eternal.fire;

import java.util.Objects;

public class Token {
    private TokenType tokenType;
    private String string;
    private double val;

    public Token() {

    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenType, string, val);
    }

    @Override
    public boolean equals(Object obj) {
        // 同一个引用
        if (obj == this) {
            return true;
        }
        // 有null或者不是一个类
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Token token = (Token) obj;
        return token.getString().equals(this.string) && token.getTokenType() == this.tokenType && token.val == this.val;
    }

    public Token(TokenType tokenType, String string, double val) {
        this.tokenType = tokenType;
        this.string = string;
        this.val = val;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }
}
