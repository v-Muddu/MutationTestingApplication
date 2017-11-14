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
 * Java Static Modifier Deletion Operator
 * This operator deletes the static modifier from fields
 */
public class JavaStaticModifierDeletionOperator implements Operator {
    @Override
    public void mutate() {
        try {
            System.out.println("Executing the Java static Modifier Deletion Operator");
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(PropertiesUtils.getProperties().getProperty("sourceClassPath"));

            File f = new File(targetFolderJSD);
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
                        //JSI
                        if (!Modifier.isStatic(ctf.getModifiers())) {
                            ctf.setModifiers(Modifier.STATIC);
                            mutatedClassSet.add(className);
                        }

                    }
                    clazz.writeFile(targetFolderJSD);
                } else {
                    String classLocation = className.replace(".", "\\");
                    File sourceFile = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath") + "\\" + classLocation + ".class");

                    File destinationFile = new File(targetFolderJSD + "\\" + classLocation + ".class");
                    org.apache.commons.io.FileUtils.copyFile(sourceFile, destinationFile);
                }
            }

            LauncherUtils.prepareClassesForExecution("Static Modifier Deletion Operator", classList, mutatedClassSet, orgUrlClassLoader, mutatedUrlClassLoader);

        }catch (NotFoundException | CannotCompileException | IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        mutate();
    }
}
