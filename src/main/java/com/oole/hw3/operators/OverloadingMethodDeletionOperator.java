package com.oole.hw3.operators;

import com.oole.hw3.utility.LauncherUtils;
import com.oole.hw3.utility.ListOrderingComparator;
import com.oole.hw3.utility.PropertiesUtils;
import javassist.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Overloading Method Deletion Operator
 * This operator deletes the overloaded method in a class
 */
public class OverloadingMethodDeletionOperator implements Operator {

    @Override
    public void mutate() {
        try {
            System.out.println("Executing the overloading method deletion operator");

            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(PropertiesUtils.getProperties().getProperty("sourceClassPath"));

            File f = new File(targetFolderOMD);
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
                        CtMethod[] methods = ctClass.getDeclaredMethods();
                        Set<String> methodSet = new HashSet<>();
                        for (CtMethod ctMethod : methods) {
                            if (methodSet.add(ctMethod.getName()) == false) {
                                System.out.println("Removing overloaded method " + ctMethod.getName()
                                        + " from class " + ctClass.getName());
                                ctClass.removeMethod(ctMethod);
                                mutatedClassSet.add(className);

                            }
                        }
                        ctClass.writeFile(targetFolderOMD);
                    } else {
                        String classLocation = className.replace(".", "\\");
                        File sourceFile = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath") + "\\" + classLocation + ".class");

                        File destinationFile = new File(targetFolderOMD + "\\" + classLocation + ".class");
                        org.apache.commons.io.FileUtils.copyFile(sourceFile, destinationFile);
                    }
                } catch (NotFoundException | CannotCompileException | IOException e) {
                    e.printStackTrace();
                }
            }

            LauncherUtils.prepareClassesForExecution("Access Modifier Operator", classList, mutatedClassSet, orgUrlClassLoader, mutatedUrlClassLoader);
        } catch (NotFoundException | IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        mutate();
    }
}
