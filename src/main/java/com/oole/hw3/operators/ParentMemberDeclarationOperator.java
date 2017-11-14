package com.oole.hw3.operators;

import com.oole.hw3.utility.LauncherUtils;
import com.oole.hw3.utility.ListOrderingComparator;
import com.oole.hw3.utility.PropertiesUtils;
import javassist.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Parent Member Declaration Operator
 * This operator changes the type of a subclass field to a super type
 */
public class ParentMemberDeclarationOperator implements Operator {

    @Override
    public void mutate() {
        try {
            System.out.println("Executing the parent member declaration operator");

            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(PropertiesUtils.getProperties().getProperty("sourceClassPath"));

            File f = new File(targetFolderPMD);
            File f2 = new File(PropertiesUtils.getProperties().getProperty("testClassPath"));
            URL[] classpath = {f.toURI().toURL(), f2.toURI().toURL()};
            URLClassLoader mutatedUrlClassLoader = new URLClassLoader(classpath);

            File file = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath"));
            URL[] filePath = {file.toURI().toURL(), f2.toURI().toURL()};
            URLClassLoader orgUrlClassLoader = new URLClassLoader(filePath);

            List<String> classList = LauncherUtils.getClassNamesFromFileSystem(PropertiesUtils.getProperties().getProperty("sourceClassPath"), "");
            Set<String> mutatedClassSet = new HashSet<>();
            Collections.sort(classList, new ListOrderingComparator());

            for (String className : classList) {

                try {

                    if (!className.contains("$")) {
                        CtClass ctClass = pool.get(className);
                        CtField[] ctFields = ctClass.getDeclaredFields();
                        CtClass superClass = ctClass.getSuperclass();

                        for (CtField ctField : ctFields) {
                            if (ctField.getType().getName().equals(ctClass.getName()) &&
                                    !superClass.getName().equals("java.lang.Object")) {
                                System.out.println("Changing type of " + ctField.getName() + " from " + ctField.getType().getName() + " to"
                                        + " super class >" + superClass.getName());
                                ctField.setType(superClass);
                                mutatedClassSet.add(className);
                            }
                        }

                        ctClass.writeFile(targetFolderPMD);
                    } else {
                        String classLocation = className.replace(".", "\\");
                        File sourceFile = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath") + "\\" + classLocation + ".class");

                        File destinationFile = new File(targetFolderPMD + "\\" + classLocation + ".class");
                        //org.apache.commons.io.LauncherUtils.copyDirectory();
                        org.apache.commons.io.FileUtils.copyFile(sourceFile, destinationFile);
                    }

                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }

            LauncherUtils.prepareClassesForExecution("Parent Member Declaration Operator", classList, mutatedClassSet, orgUrlClassLoader, mutatedUrlClassLoader);

        }catch (NotFoundException | CannotCompileException | IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        mutate();
    }
}
