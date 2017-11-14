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
 * Java Static Modifier Insertion Operator
 * This operator inserts static modifier to the fields
 */
public class JavaStaticModifierInsertionOperator implements Operator {
    @Override
    public void mutate() {

        try {
            System.out.println("Executing the Java static modifier insertion operator");
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(PropertiesUtils.getProperties().getProperty("sourceClassPath"));

            File f = new File(targetFolderJSI);
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
                if (!className.contains("$")) {
                    CtClass clazz = pool.get(className);
                    clazz.defrost();
                    for (CtField ctf : clazz.getDeclaredFields()) {

                        //JSD
                        if (Modifier.isStatic(ctf.getModifiers())) {
                            ctf.setModifiers(ctf.getModifiers() & ~Modifier.STATIC);
                            mutatedClassSet.add(className);
                        }
                    }
                    clazz.writeFile(targetFolderJSI);
                } else {
                    String classLocation = className.replace(".", "\\");
                    File sourceFile = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath") + "\\" + classLocation + ".class");

                    File destinationFile = new File(targetFolderJSI + "\\" + classLocation + ".class");
                    org.apache.commons.io.FileUtils.copyFile(sourceFile, destinationFile);
                }
            }

            LauncherUtils.prepareClassesForExecution("Static Modifier Insertion Operator", classList, mutatedClassSet, orgUrlClassLoader, mutatedUrlClassLoader);
        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        mutate();

    }
}

