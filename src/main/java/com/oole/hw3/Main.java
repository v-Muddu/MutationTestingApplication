package com.oole.hw3;

import com.oole.hw3.concurrency.LauncherThreadExecutorService;
import com.oole.hw3.operators.*;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.io.IOException;

public class Main
    {
        public static void main(String[] args) throws IOException, CannotCompileException, NotFoundException, ClassNotFoundException {
            Operator operator = new AccessModifierOperator();
            Operator hideOperator = new HidingVariablesOperator();
            Operator JavaSpecOperators = new GlobalVariablesMutation();
            Operator omdOperator = new OverloadingMethodDeletionOperator();
            Operator pmdOperator = new ParentMemberDeclarationOperator();

            LauncherThreadExecutorService.executorService.submit(pmdOperator);
            LauncherThreadExecutorService.shutdown();
        }
    }
