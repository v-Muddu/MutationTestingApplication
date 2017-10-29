package com.oole.hw3.operators;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.io.IOException;

public interface Operator extends Runnable {
    String targetFolderEncapsulation = "mutatedFiles/AMC";
    String targetFolderInheritance = "mutatedFiles/Inheritance";
    String targetFolderPolymorphism = "mutatedFiles/Polymorphism";
    String targetFolderJavaSpec = "mutatedFiles/JavaSpec";
    void mutate() throws NotFoundException, CannotCompileException, IOException;
}
