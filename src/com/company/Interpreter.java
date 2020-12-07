package com.company;

import java.util.HashMap;
import java.util.Map;

public class Interpreter implements NodeVisitor {
    private Parser parser;
    private static Map<String, Float> variables = new HashMap<>();

    public Interpreter(Parser parser){
        this.parser = parser;
    }

    @Override
    public void visit(Node node) throws Exception {
        Block block = (Block) node;
        boolean next = true;
        Node statement = block.getNext();
        while(next!=false) {
            if (statement.getClass() == Match.class){
                visitWriter(statement);
            }
            else if(statement.getClass() == Block.class){
                visit(statement);
            }
            else if(statement.getClass() == End.class){
                return;
            }
            else {
                visitState(statement);
            }
            statement = block.getNext();
            next = statement != null;
        }
    }

    public void visitWriter(Node node)throws Exception {
        Match writer = (Match) node;
        if (node.getClass().equals(Match.class)){
            float val = visitState(writer.getExpr());
            variables.put(writer.getID().toString(), val);
        }
    }

    public float visitState(Node node) throws Exception {
//        System.out.println("visit Statement");
        if (node.getClass().equals(BinOp.class)){
            return visitBinOp(node);
        }
        else if (node.getClass().equals(Number.class)){
            return visitNumber(node);
        }
        else if(node.getClass().equals(Variable.class)) {
            if (variables.containsKey(((Variable)node).getVariable().getValue()))
            {
                return variables.get(((Variable)node).getVariable().getValue());
            }
        }
        else if (node.getClass().equals(UnaryOp.class)){
            return visitUnaryOp(node);
        }
        throw new Exception("Interpreter error!");
    }

    public float visitBinOp(Node node) throws Exception {
//        System.out.println("visit BinOp");
        BinOp binop = (BinOp) node;
        if (binop.getOp().getType().equals(TokenType.PLUS)){
            return visitState(binop.getLeft()) + visitState(binop.getRight());
        }
        else if (binop.getOp().getType().equals(TokenType.MINUS)){
            return visitState(binop.getLeft()) - visitState(binop.getRight());
        }
        else if (binop.getOp().getType().equals(TokenType.DIV)){
            return visitState(binop.getLeft()) / visitState(binop.getRight());
        }
        else if (binop.getOp().getType().equals(TokenType.MUL)){
            return visitState(binop.getLeft()) * visitState(binop.getRight());
        }
        throw new Exception("BinOp error");
    }

    public float visitNumber(Node node){
//        System.out.println("visit Number");
        Number number = (Number) node;
        return Float.parseFloat(number.getToken().getValue());
    }

    public float visitUnaryOp(Node node) throws Exception {
//        System.out.println("visit UnaryOp");
        UnaryOp op = (UnaryOp) node;
        if (op.getToken().getType().equals(TokenType.PLUS)){
            return +visitState(op.getExpr());
        }
        else if (op.getToken().getType().equals(TokenType.MINUS)){
            return -visitState(op.getExpr());
        }
        throw new Exception("UnaryOp error!");
    }

    public void interpret() throws Exception {
        Node tree = parser.parse();
        visit(tree);
    }

    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer("BEGIN\ny:= 2;\nBEGIN\na := 3;\na := a;\n b := 10 + a + 10 * y / 4;\nc := +a - - b\nEND;\nx := 11;\nEND.");
        Parser parser = new Parser(lexer);
        Interpreter interpreter = new Interpreter(parser);
        interpreter.interpret();

        for (Map.Entry<String, Float> variable : variables.entrySet()){
            System.out.println(variable.getKey() + " = " + variable.getValue());
        }
    }
}
