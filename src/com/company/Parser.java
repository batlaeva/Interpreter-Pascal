package com.company;

import java.util.Arrays;
import java.util.List;

public class Parser {
    private Lexer lexer;
    private Token currentToken;
    private Variable currentVariable;

    public Parser(Lexer lexer) throws Exception {
        this.lexer = lexer;
        currentToken = this.lexer.nextToken();
    }

    private void checkTokenType(TokenType type) throws Exception {
        if (currentToken.getType() == type) {
            currentToken = lexer.nextToken();
        } else {
            throw new Exception("Parser error");
        }
    }

    private Node factor() throws Exception {
        Token token = currentToken;
        if (token.getType().equals(TokenType.EOL)) {
            checkTokenType(TokenType.EOL);
            token = currentToken;
        }
        if (token.getType().equals(TokenType.PLUS)) {
            checkTokenType(TokenType.PLUS);
            return new UnaryOp(token, factor());
        } else if (token.getType().equals(TokenType.MINUS)) {
            checkTokenType(TokenType.MINUS);
            return new UnaryOp(token, factor());
        } else if (token.getType().equals(TokenType.INTEGER)) {
            checkTokenType(TokenType.INTEGER);
            return new Number(token);
        } else if (token.getType().equals(TokenType.LPAREN)) {
            checkTokenType(TokenType.LPAREN);
            Node node = expr(factor());
            checkTokenType(TokenType.RPAREN);
            return node;
        } else if (token.getType().equals(TokenType.BEGIN)) {
            checkTokenType(TokenType.BEGIN);
            Node node = expr(assignment());
            checkTokenType(TokenType.END);
            return node;
        } else if (token.getType().equals(TokenType.END)) {
            return new End(token);
        } else if (token.getType().equals(TokenType.VARIABLE)) {
            checkTokenType(TokenType.VARIABLE);
            currentVariable = new Variable(token);
            return currentVariable;
        } else if (token.getType().equals(TokenType.ASSIGNMENT)) {
            checkTokenType(TokenType.ASSIGNMENT);
            Node node = expr(factor());
            return new Match(currentVariable, node);
        }
        throw new Exception("Factor error");
    }

    private Node term(Node node) throws Exception {
        Node result = node;
        List<TokenType> ops = Arrays.asList(TokenType.DIV, TokenType.MUL);
        while (ops.contains(currentToken.getType())) {
            Token token = currentToken;
            if (token.getType() == TokenType.MUL) {
                checkTokenType(TokenType.MUL);
            } else if (token.getType() == TokenType.DIV) {
                checkTokenType(TokenType.DIV);
            }
            result = new BinOp(result, token, factor());
        }
        return result;
    }

    public Node expr(Node node) throws Exception {
        List<TokenType> ops = Arrays.asList(TokenType.PLUS, TokenType.MINUS);
        Node result = term(node);
        while (ops.contains(currentToken.getType())) {
            Token token = currentToken;
            if (token.getType() == TokenType.PLUS) {
                checkTokenType(TokenType.PLUS);
            } else if (token.getType() == TokenType.MINUS) {
                checkTokenType(TokenType.MINUS);
            }
            result = new BinOp(result, token, term(factor()));
        }
        return result;
    }

    public Node equating() throws Exception {
        List<TokenType> ops = Arrays.asList(TokenType.ASSIGNMENT, TokenType.VARIABLE);
        Node result = factor();
        while (ops.contains(currentToken.getType())) {
            Token token = currentToken;
            if (token.getType() == TokenType.VARIABLE) {
                checkTokenType(TokenType.VARIABLE);
            }
            if (token.getType() == TokenType.ASSIGNMENT) {
                checkTokenType(TokenType.ASSIGNMENT);
                result = new Match(currentVariable, expr(factor()));
                return result;
            }
        }
        return expr(result);
    }

    public Node assignment() throws Exception {
        Block body = new Block();
        do {
            Node result = equating();
            body.addExpression(result);
        }
        while (currentToken.getType() == TokenType.EOL);
        return body;
    }

    public Node parse() throws Exception {
        return assignment();
    }
}
