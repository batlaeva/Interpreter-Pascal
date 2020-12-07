package com.company;

public class Match extends Node{
    Variable var;
    Node expression;

    public Match(Variable var, Node expression) {
        this.var = var;
        this.expression = expression;
    }

    public Variable getID() {
        return var;
    }

    public Node getExpr() {
        return expression;
    }
}
