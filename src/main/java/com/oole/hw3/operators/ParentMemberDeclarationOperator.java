package com.oole.hw3.operators;

import com.oole.hw3.utility.FileUtils;
import javassist.*;

import java.io.IOException;
import java.util.List;

public class ParentMemberDeclarationOperator implements Operator {

    @Override
    public void mutate() throws NotFoundException, CannotCompileException, IOException {
        System.out.println("Executing the parent member declaration operator");

        List<String> classList = FileUtils.getClassNamesInJar();
        for (String className : classList) {
            ClassPool pool = ClassPool.getDefault();
            ClassLoader classLoader = pool.getClassLoader();

            try {

                classLoader.loadClass(className);
                CtClass ctClass = pool.get(className);
                CtField[] ctFields = ctClass.getDeclaredFields();
                CtClass superClass = ctClass.getSuperclass();

                for (CtField ctField : ctFields) {
                    if (ctField.getType().getName().equals(ctClass.getName()) &&
                            !superClass.getName().equals("java.lang.Object"))
                        System.out.println("Changing type of " + ctField.getName() + " from " + ctField.getType().getName() + " to"
                                + " super class >" + superClass.getName());
                    ctField.setType(superClass);
                }
                ctClass.writeFile(targetFolderPolymorphism);
            } catch (ClassNotFoundException | NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {
            mutate();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
