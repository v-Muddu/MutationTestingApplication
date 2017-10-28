package com.oole.hw3.operators;

public class AccessModifierOperator implements Operator {

    @Override
    public void mutate() {
        System.out.println("Executing the access modifier operator");
    }

    @Override
    public void run() {
        mutate();
    }
}
