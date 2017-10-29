package com.oole.hw3;

import com.oole.hw3.concurrency.LauncherThreadExecutorService;
import com.oole.hw3.operators.AccessModifierOperator;
import com.oole.hw3.operators.GlobalVariablesMutation;
import com.oole.hw3.operators.HidingVariablesOperator;
import com.oole.hw3.operators.Operator;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.io.IOException;

public class Main
    {
        public static void main(String[] args) throws IOException, CannotCompileException, NotFoundException, ClassNotFoundException {
            Operator operator = new AccessModifierOperator();
            //LauncherThreadExecutorService.executorService.submit(operator);
           // LauncherThreadExecutorService.shutdown();

            Operator hideOperator = new HidingVariablesOperator();
            //LauncherThreadExecutorService.executorService.submit(hideOperator);
           // LauncherThreadExecutorService.shutdown();

            Operator JavaSpecOperators = new GlobalVariablesMutation();
            LauncherThreadExecutorService.executorService.submit(JavaSpecOperators);
            LauncherThreadExecutorService.shutdown();
        }
    }
