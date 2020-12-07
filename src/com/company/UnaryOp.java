package com.company;

public class UnaryOp extends Node {
    private Token token;
    private Node expr;

    public UnaryOp(Token token, Node expr){
        this.token = token;
        this.expr = expr;
    }

    public Token getToken(){
        return token;
    }

    public Node getExpr(){
        return expr;
    }

    public String toString(){
        return String.format("UnaryOp%s (%s)",this.token.getValue(), expr);
    }

}
