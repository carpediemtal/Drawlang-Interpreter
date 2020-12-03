package eternal.fire.syntax;

import eternal.fire.token.Lexer;
import eternal.fire.token.Token;
import eternal.fire.token.TokenType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Parser {
    private final static Logger logger = LoggerFactory.getLogger(Parser.class);

    /**
     * 像素点的大小
     */
    private double dotSize = 5;

    public void setDotSize(double dotSize) {
        this.dotSize = dotSize;
    }

    /**
     * 原点偏移量
     */
    private double originX = 0;
    private double originY = 0;

    /**
     * 缩放比例
     */
    private double scaleX = 1;
    private double scaleY = 1;

    /**
     * 旋转角度
     */
    private double rotAngle = 0;

    /**
     * 绘图语句定义的循环的起始量
     */
    private double start;
    private double end;
    private double step;

    /**
     * 要绘制的点的坐标（从语法树中计算得到的初始坐标）
     */
    private ExprNode horizontalT;
    private ExprNode verticalT;

    private final GraphicsContext context;

    /**
     * 语法分析器内置的词法分析器
     */
    private final Lexer lexer;

    public Lexer getLexer() {
        return lexer;
    }

    /**
     * 当前的Token
     */
    private Token token;

    /**
     * 语法分析器的构造函数
     *
     * @param context 绘图面板
     * @throws IOException 找不到函数绘图语言的源代码
     */
    public Parser(GraphicsContext context) throws IOException {
        lexer = new Lexer();
        this.context = context;
        logger.info("Parser init done");
    }

    /**
     * 语法分析器的构造函数（指定源代码文件名）
     *
     * @param context  绘图面板
     * @param fileName 源代码文件名
     * @throws IOException 找不到对应的文件
     */
    public Parser(GraphicsContext context, String fileName) throws IOException {
        lexer = new Lexer(fileName);
        this.context = context;
        logger.info("Parser init done");
    }

    private void drawDot(double x, double y) {
        context.fillOval(x, y, dotSize, dotSize);
        logger.info("Draw dot:({},{}), color:{}", x, y, context.getFill());
    }

    private void drawDots() {
        for (double i = start; i < end; i += step) {
            var xVal = getExprVal(horizontalT, i);
            var yVal = getExprVal(verticalT, i);
            var xy = calculateXY(xVal, yVal);
            drawDot(xy.get(0), xy.get(1));
        }
    }

    /**
     * 将原始坐标进行比例变换、旋转变换再加上原点偏移量得到新的坐标
     *
     * @param x 原始横坐标
     * @param y 原始纵坐标
     * @return 变换后的坐标
     */
    private List<Double> calculateXY(double x, double y) {
        // 比例变换
        x *= scaleX;
        y *= scaleY;
        // 旋转变换
        double tmp = x * Math.cos(rotAngle) + y * Math.sin(rotAngle);
        y = y * Math.cos(rotAngle) - x * Math.sin(rotAngle);
        x = tmp;
        // 加上原点偏移量
        return List.of(x + originX, y + originY);
    }

    public void parse() {
        fetchToken();
        program();
    }

    private void program() {
        while (token.getTokenType() != TokenType.END) {
            statement();
            matchToken(TokenType.SEMICOLON);
        }
    }

    /**
     * 解释statement
     */
    private void statement() {
        switch (token.getTokenType()) {
            case ORIGIN -> originStatement();
            case SCALE -> scaleStatement();
            case ROT -> rotStatement();
            case FOR -> forStatement();
            default -> throw new RuntimeException("syntax error");
        }
    }

    /**
     * 解释originStatement
     */
    private void originStatement() {
        matchToken(TokenType.ORIGIN);
        matchToken(TokenType.IS);
        matchToken(TokenType.L_BRACKET);
        var syntaxTree = expression();
        originX = getExprVal(syntaxTree);
        logger.info("Set originX:{}", originX);

        matchToken(TokenType.COMMA);
        syntaxTree = expression();
        originY = getExprVal(syntaxTree);
        logger.info("Set originY:{}", originY);
        matchToken(TokenType.R_BRACKET);
    }

    private void rotStatement() {
        matchToken(TokenType.ROT);
        matchToken(TokenType.IS);
        var syntaxTree = expression();
        rotAngle = getExprVal(syntaxTree);
    }

    private void scaleStatement() {
        matchToken(TokenType.SCALE);
        matchToken(TokenType.IS);
        matchToken(TokenType.L_BRACKET);
        var syntaxTree = expression();
        scaleX = getExprVal(syntaxTree);
        logger.info("Set scaleX:{}", scaleX);

        matchToken(TokenType.COMMA);
        syntaxTree = expression();
        scaleY = getExprVal(syntaxTree);
        logger.info("Set scaleY:{}", scaleY);
        matchToken(TokenType.R_BRACKET);
    }

    private void forStatement() {
        matchToken(TokenType.FOR);
        matchToken(TokenType.T);
        matchToken(TokenType.FROM);
        var syntaxTree = expression();
        start = getExprVal(syntaxTree);
        logger.info("Set start:{}", start);

        matchToken(TokenType.TO);
        syntaxTree = expression();
        end = getExprVal(syntaxTree);
        logger.info("Set end:{}", end);

        matchToken(TokenType.STEP);
        syntaxTree = expression();
        step = getExprVal(syntaxTree);
        logger.info("Set step:{}", step);

        matchToken(TokenType.DRAW);
        matchToken(TokenType.L_BRACKET);
        horizontalT = expression();
        matchToken(TokenType.COMMA);
        verticalT = expression();
        matchToken(TokenType.R_BRACKET);
        drawDots();
    }

    /**
     * 匹配一个表达式expression，为表达式expression构造语法树ExprNode
     *
     * @return 构造出的语法树
     */
    private ExprNode expression() {
        var left = term();
        while (token.getTokenType() == TokenType.PLUS || token.getTokenType() == TokenType.MINUS) {
            var tmp = token.getTokenType();
            matchToken(token.getTokenType());
            var right = term();
            // 不断更新左节点
            left = new ExprNode(tmp, left, right);
        }
        return left;
    }

    private ExprNode term() {
        ExprNode left = factor();
        while (token.getTokenType() == TokenType.MUL || token.getTokenType() == TokenType.DIV) {
            var tmp = token.getTokenType();
            matchToken(token.getTokenType());
            var right = factor();
            // 不断更新左节点
            left = new ExprNode(tmp, left, right);
        }
        return left;
    }

    private ExprNode factor() {
        switch (token.getTokenType()) {
            // ‘一元加’运算
            case PLUS -> {
                matchToken(TokenType.PLUS);
                ExprNode left = new ExprNode(TokenType.CONST_VAL, 0);
                ExprNode right = factor();
                return new ExprNode(TokenType.PLUS, left, right);
            }
            // ‘一元减’运算
            case MINUS -> {
                matchToken(TokenType.MINUS);
                ExprNode left = new ExprNode(TokenType.CONST_VAL, 0);
                ExprNode right = factor();
                return new ExprNode(TokenType.MINUS, left, right);
            }
            default -> {
                return component();
            }
        }
    }

    private ExprNode component() {
        ExprNode left = atom();
        if (token.getTokenType() == TokenType.POWER) {
            matchToken(TokenType.POWER);
            var right = component();
            return new ExprNode(TokenType.POWER, left, right);
        }
        return left;
    }

    private ExprNode atom() {
        switch (token.getTokenType()) {
            case CONST_VAL -> {
                var tmp = token.getTokenVal();
                matchToken(TokenType.CONST_VAL);
                return new ExprNode(TokenType.CONST_VAL, tmp);
            }
            case T -> {
                matchToken(TokenType.T);
                return new ExprNode(TokenType.T);
            }
            case FUNC -> {
                ExprNode funcNode = new ExprNode(TokenType.FUNC, token.getTokenString());
                matchToken(TokenType.FUNC);
                matchToken(TokenType.L_BRACKET);
                ExprNode child = expression();
                funcNode.setChild(child);
                matchToken(TokenType.R_BRACKET);
                return funcNode;
            }
            case L_BRACKET -> {
                matchToken(TokenType.L_BRACKET);
                ExprNode node = expression();
                matchToken(TokenType.R_BRACKET);
                return node;
            }
            default -> throw new RuntimeException("Syntax Error");
        }
    }

    /**
     * 根据语法树，计算表达式的值（不考虑包含参数T的情况）
     *
     * @param exprNode 待计算的语法树
     * @return 语法树所代表的表达式的值
     */
    private double getExprVal(ExprNode exprNode) {
        switch (exprNode.getTokenType()) {
            case PLUS -> {
                return getExprVal(exprNode.getLeft()) + getExprVal(exprNode.getRight());
            }
            case MINUS -> {
                return getExprVal(exprNode.getLeft()) - getExprVal(exprNode.getRight());
            }
            case MUL -> {
                return getExprVal(exprNode.getLeft()) * getExprVal(exprNode.getRight());
            }
            case DIV -> {
                return getExprVal(exprNode.getLeft()) / getExprVal(exprNode.getRight());
            }
            case POWER -> {
                return Math.pow(getExprVal(exprNode.getLeft()), getExprVal(exprNode.getRight()));
            }
            case FUNC -> {
                return function(exprNode.getFuncName(), getExprVal(exprNode.getChild()));
            }
            case CONST_VAL -> {
                return exprNode.getVal();
            }
            default -> {
                logger.info(exprNode.toString());
                throw new RuntimeException("Syntax Error");
            }
        }
    }

    /**
     * 方法重载：根据语法树，计算表达式的值（考虑包含参数T的情况）
     *
     * @param exprNode 待计算的语法树
     * @param t        参数T的值
     * @return 语法树所代表的表达式的值
     */
    private double getExprVal(ExprNode exprNode, double t) {
        switch (exprNode.getTokenType()) {
            case PLUS -> {
                return getExprVal(exprNode.getLeft(), t) + getExprVal(exprNode.getRight(), t);
            }
            case MINUS -> {
                return getExprVal(exprNode.getLeft(), t) - getExprVal(exprNode.getRight(), t);
            }
            case MUL -> {
                return getExprVal(exprNode.getLeft(), t) * getExprVal(exprNode.getRight(), t);
            }
            case DIV -> {
                return getExprVal(exprNode.getLeft(), t) / getExprVal(exprNode.getRight(), t);
            }
            case POWER -> {
                return Math.pow(getExprVal(exprNode.getLeft(), t), getExprVal(exprNode.getRight(), t));
            }
            case FUNC -> {
                return function(exprNode.getFuncName(), getExprVal(exprNode.getChild(), t));
            }
            case CONST_VAL -> {
                return exprNode.getVal();
            }
            case T -> {
                return t;
            }
            default -> throw new RuntimeException("Syntax Error");
        }
    }

    /**
     * 计算带有函数的表达式的值
     *
     * @param funcName 函数名
     * @param val      函数
     * @return 函数值
     */
    private double function(String funcName, double val) {
        switch (funcName) {
            case "COS" -> {
                return Math.cos(val);
            }
            case "SIN" -> {
                return Math.sin(val);
            }
            case "TAN" -> {
                return Math.tan(val);
            }
            case "SQRT" -> {
                return Math.sqrt(val);
            }
            case "EXP" -> {
                return Math.exp(val);
            }
            case "LN" -> {
                return Math.log(val);
            }
            default -> throw new RuntimeException("Syntax Error");
        }
    }

    /**
     * 调用词法分析器的getToken方法获得源代码中下一个Token
     */
    private void fetchToken() {
        this.token = lexer.getToken();
        logger.info("Fetch Token:{}", token.getTokenType());
        if (token.getTokenType() == TokenType.ERROR) {
            throw new RuntimeException("Syntax Error");
        }
    }

    /**
     * 匹配一个Token，接着fetch一个Token
     *
     * @param tokenType 要匹配的Token的类型
     */
    private void matchToken(TokenType tokenType) {
        if (token.getTokenType() != tokenType) {
            throw new RuntimeException("Syntax Error");
        }
        fetchToken();
    }
}
