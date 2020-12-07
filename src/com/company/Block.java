package com.company;

import java.util.ArrayList;

public class Block extends Node {

    ArrayList <Node> expressions;
    int current;

    public Block(){
        expressions = new ArrayList<>();
        current = 0;
    }

    public void addExpression(Node node){
        expressions.add(node);
    }

    public Node getNext(){
        current +=1;
        if (current != expressions.size()+1)
             return expressions.get(current -1);
        return null;
    }
}
