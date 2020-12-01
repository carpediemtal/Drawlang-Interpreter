package eternal.fire;

import java.util.Objects;

public class Token {
    private TokenType tokenType;
    private String tokenString;
    private double tokenVal;

    public Token() {

    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenType, tokenString, tokenVal);
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
        return token.getTokenString().equals(this.tokenString) && token.getTokenType() == this.tokenType && token.tokenVal == this.tokenVal;
    }

    public Token(TokenType tokenType, String tokenString, double tokenVal) {
        this.tokenType = tokenType;
        this.tokenString = tokenString;
        this.tokenVal = tokenVal;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public double getTokenVal() {
        return tokenVal;
    }

    public void setTokenVal(double tokenVal) {
        this.tokenVal = tokenVal;
    }

    /**
     * 格式化输出
     * @return 格式化输出内容
     */
    @Override
    public String toString() {
        return String.format("TokenType:%s;\tTokenString:%s;\tTokenVal:%.2f\t\n", tokenType, tokenString, tokenVal);
    }

}
