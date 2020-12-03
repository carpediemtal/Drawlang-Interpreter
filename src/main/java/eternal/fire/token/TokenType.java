package eternal.fire.token;

public enum TokenType {
    /**
     * 保留关键字
     */
    ORIGIN, SCALE, ROT, IS, TO, STEP, DRAW, FOR, FROM,

    /**
     * 参数和标点符号
     */
    T, SEMICOLON, L_BRACKET, R_BRACKET, COMMA,

    /**
     * 运算符号
     */
    PLUS, MINUS, MUL, DIV, POWER,

    /**
     * 函数
     */
    FUNC,

    /**
     * 常数
     */
    CONST_VAL,

    /**
     * 文件末尾和错误
     */
    END, ERROR;
}
