package eternal.fire;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Parser extends Application {
    private Lexer lexer;
    private Token token;
    private double originX = 0;
    private double originY = 0;
    private double scaleX = 1;
    private double scaleY = 1;
    private double rotAngle = 0;
    double start;
    double end;
    double step;
    ExprNode horizontalT;
    ExprNode verticalT;
    GraphicsContext context;

    public Parser() throws IOException {
        lexer = new Lexer();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Canvas canvas = new Canvas(500, 500);
        context = canvas.getGraphicsContext2D();
//        drawDot(context, 250, 250);
        drawDots();
        root.getChildren().add(canvas);
        primaryStage.setTitle("Draw");
        primaryStage.getIcons().add(new Image(Draw.class.getResourceAsStream("/draw.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void drawDot(double x, double y) {
        context.setFill(Color.GREEN);
        context.setLineWidth(5);
        context.fillOval(x, y, 5, 5);
    }

    private void drawDots() {
        for (int i = (int) start; i < end; i += step) {
            var xVal = getExprVal(horizontalT);
            var yVal = getExprVal(verticalT);
            var xy = calculateXY(xVal, yVal);
            drawDot(xy.get(0), xy.get(1));
        }
    }

    private List<Double> calculateXY(double x, double y) {
        // 比例变换
        x *= scaleX;
        y *= scaleY;
        // 旋转变换
        double tmp = x * Math.cos(rotAngle) + y * Math.sin(rotAngle);
        y = y * Math.cos(rotAngle) - x * Math.sin(rotAngle);
        x = tmp;
        return List.of(x, y);
    }

    private void parse() {
        fetchToken();
        program();
    }

    private void program() {
        while (token.getTokenType() != TokenType.END) {
            statement();
            matchToken(TokenType.SEMICOLON);
        }
    }

    private void statement() {
        switch (token.getTokenType()) {
            case ORIGIN -> originStatement();
            case SCALE -> scaleStatement();
            case ROT -> rotStatement();
            case FOR -> forStatement();
            default -> throw new RuntimeException("syntax error");
        }
    }

    private void originStatement() {
        matchToken(TokenType.ORIGIN);
        matchToken(TokenType.IS);
        matchToken(TokenType.L_BRACKET);
        var syntaxTree = expression();
        originX = getExprVal(syntaxTree);
        matchToken(TokenType.COMMA);
        syntaxTree = expression();
        originY = getExprVal(syntaxTree);
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
        matchToken(TokenType.COMMA);
        syntaxTree = expression();
        scaleY = getExprVal(syntaxTree);
        matchToken(TokenType.R_BRACKET);
    }

    private void forStatement() {
        matchToken(TokenType.FOR);
        matchToken(TokenType.T);
        matchToken(TokenType.FROM);
        var syntaxTree = expression();
        start = getExprVal(syntaxTree);
        matchToken(TokenType.TO);
        syntaxTree = expression();
        end = getExprVal(syntaxTree);
        matchToken(TokenType.STEP);
        syntaxTree = expression();
        step = getExprVal(syntaxTree);
        matchToken(TokenType.DRAW);
        matchToken(TokenType.L_BRACKET);
        horizontalT = expression();
        matchToken(TokenType.COMMA);
        verticalT = expression();
        matchToken(TokenType.R_BRACKET);
        drawDots();
    }

    private ExprNode expression() {
        var left = term();
        while (token.getTokenType() == TokenType.PLUS || token.getTokenType() == TokenType.MINUS) {
            matchToken(token.getTokenType());
            var right = term();
            left = makeExprNode(token.getTokenType());

        }
        return left;
    }

    private ExprNode term() {
        ExprNode left = factor();

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
                matchToken(TokenType.CONST_VAL);
                return new ExprNode(TokenType.CONST_VAL);
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

//    public ExprNode makeExprNode(TokenType tokenType) {
//        switch (tokenType) {
//            case CONST_VAL -> {
//                return new ExprNode(tokenType, this.token.getTokenVal());
//            }
//            case T -> {
//                return new ExprNode(tokenType);
//            }
//            case FUNC -> {
//                return new ExprNode(tokenType, token.getTokenString());
//            }
//            case PLUS, MINUS, MUL, DIV ->{
//                // Todo:
//            }
//            default -> throw new RuntimeException("Syntax Error");
//        }
//    }
//
//    public ExprNode makeExprNode(TokenType tokenType, ExprNode left, ExprNode right) {
//
//    }

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
            default -> throw new RuntimeException("Syntax Error");
        }
    }

    // 只有draw后面的表达式包含T
    private double getExprVal(ExprNode exprNode, double t) {
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
            case T -> {
                return t;
            }
            default -> throw new RuntimeException("Syntax Error");
        }
    }

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

    private void fetchToken() {
        this.token = lexer.getToken();
        if (token.getTokenType() == TokenType.ERROR) {
            throw new RuntimeException("Syntax Error");
        }
    }

    private void matchToken(TokenType tokenType) {
        if (token.getTokenType() != tokenType) {
            throw new RuntimeException("Syntax Error");
        }
        fetchToken();
    }
}
