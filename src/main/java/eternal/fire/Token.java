package eternal.fire;

public class Token {
    private TokenType tokenType;
    private String string;
    private double val;

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
