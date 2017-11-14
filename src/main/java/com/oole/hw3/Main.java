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
            Operator omdOperator = new OverloadingMethodDeletionOperator();
            Operator pmdOperator = new ParentMemberDeclarationOperator();
            Operator jsiOperator = new JavaStaticModifierInsertionOperator();
            Operator jsdOperator = new JavaStaticModifierDeletionOperator();
            Operator ihdOperator = new HidingVariableDeletionOperator();
           // Operator ihiOperator = new HidingVariableInsertionOperator();
            Operator iodOperator = new OverridingMethodDeletionOperator();
            Operator jcdOperator = new DefaultConstructorDeletion();

            /*LauncherThreadExecutorService.executorService.submit(operator);
            LauncherThreadExecutorService.executorService.submit(omdOperator);
            LauncherThreadExecutorService.executorService.submit(pmdOperator);
            LauncherThreadExecutorService.executorService.submit(jsiOperator);
            LauncherThreadExecutorService.executorService.submit(jsdOperator);
            LauncherThreadExecutorService.executorService.submit(ihdOperator);
           // LauncherThreadExecutorService.executorService.submit(ihiOperator);
            LauncherThreadExecutorService.executorService.submit(iodOperator);*/
            LauncherThreadExecutorService.executorService.submit(jcdOperator);
            LauncherThreadExecutorService.shutdown();

        }
    }
