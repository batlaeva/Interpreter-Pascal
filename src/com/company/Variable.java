package com.company;

public class Variable extends Node {
    private Token token;

    public Variable(Token token){
        this.token = token;
    }

    public Token getVariable(){
        return token;
    }

    public String toString(){
        return token.getValue();
    }
}
