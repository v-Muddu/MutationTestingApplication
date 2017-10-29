package com.oole.hw3.operators;

public interface Operator extends Runnable {
    String targetFolder = "mutatedFiles/AMC";
    void mutate();
}
