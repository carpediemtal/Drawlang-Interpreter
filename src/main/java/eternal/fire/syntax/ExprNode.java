package eternal.fire.syntax;

import eternal.fire.token.TokenType;

/**
 * 当节点有两个孩子的时候，使用left和right
 * 当节点只有一个孩子的时候，使用child
 */
public class ExprNode {
    private TokenType tokenType;

    private ExprNode left;

    private ExprNode right;

    private ExprNode child;

    // 针对CONST_VAL
    private double val;

    // 针对FUNC
    private String funcName;

    // 针对FUNC类型的节点
    public ExprNode(TokenType tokenType, String funcName) {
        this.tokenType = tokenType;
        this.funcName = funcName;
    }

    // CONST_VAL类型的节点
    public ExprNode(TokenType tokenType, double val) {
        this.tokenType = tokenType;
        this.val = val;
    }

    // 针对二元运算类型的节点（已经构造好了左子树和右子树）
    public ExprNode(TokenType tokenType, ExprNode left, ExprNode right) {
        this.tokenType = tokenType;
        this.left = left;
        this.right = right;
    }

    public ExprNode() {

    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    public ExprNode(TokenType tokenType) {
        this.tokenType = tokenType;
    }


    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public ExprNode getLeft() {
        return left;
    }

    public void setLeft(ExprNode left) {
        this.left = left;
    }

    public ExprNode getRight() {
        return right;
    }

    public void setRight(ExprNode right) {
        this.right = right;
    }

    public ExprNode getChild() {
        return child;
    }

    public void setChild(ExprNode child) {
        this.child = child;
    }

    @Override
    public String toString() {
//        return String.format("tokenType:%s, val:%f, funcName:%s, left:%s, right:%s, child:%s", tokenType.toString(), val, funcName, left.toString(), right.toString(), child.toString());
        return tokenType.toString() + val;
    }
}
