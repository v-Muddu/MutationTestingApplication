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

public class OverridingMethodDeletionOperator implements Operator {

    @Override
    public void mutate() {
        try {
            System.out.println("Executing hiding variable deletion");

            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(PropertiesUtils.getProperties().getProperty("sourceClassPath"));

            File f = new File(targetFolderIOD);
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
                        CtClass clazz = pool.get(className);
                        CtClass superClass = clazz.getSuperclass();
                        if (superClass != null && !superClass.getName().equals("java.lang.Object")) {

                            //IOD
                            CtMethod[] methods = clazz.getDeclaredMethods();
                            CtMethod[] superMethods = clazz.getSuperclass().getDeclaredMethods();
                            for (CtMethod ctMethod : methods) {
                                for (CtMethod superMethod : superMethods) {
                                    if (ctMethod.getName().equals(superMethod.getName()) && ctMethod.getSignature().equals(superMethod.getSignature())) {
                                        System.out.println("Applying IOD Operator to remove method " + ctMethod.getName()
                                                + " from class " + clazz.getName());
                                        clazz.removeMethod(ctMethod);
                                        mutatedClassSet.add(className);

                                    }
                                }
                            }
                        }
                        clazz.writeFile(targetFolderIOD);
                    } else {
                        String classLocation = className.replace(".", "\\");
                        File sourceFile = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath") + "\\" + classLocation + ".class");

                        File destinationFile = new File(targetFolderIOD + "\\" + classLocation + ".class");
                        org.apache.commons.io.FileUtils.copyFile(sourceFile, destinationFile);
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }

            LauncherUtils.prepareClassesForExecution("Overriding Method Deletion Operator", classList, mutatedClassSet, orgUrlClassLoader, mutatedUrlClassLoader);

        } catch (NotFoundException | IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        mutate();
    }
}
