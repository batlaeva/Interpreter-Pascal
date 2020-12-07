package com.company;

public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value){
        this.type = type;
        this.value = value;
    }

    public TokenType getType(){
        return this.type;
    }

    public String getValue(){
        return this.value;
    }

    public String toString(){
        return String.format("Token(%s, %s)", type, value);
    }

    public static void main(String[] args){
        Token token = new Token(TokenType.INTEGER, "10");
        System.out.println(token);
        System.out.println(token.getType().equals(TokenType.INTEGER));
    }

}
