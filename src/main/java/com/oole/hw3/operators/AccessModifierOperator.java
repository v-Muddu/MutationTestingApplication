package com.oole.hw3.operators;

import com.oole.hw3.utility.LauncherUtils;
import com.oole.hw3.utility.ListOrderingComparator;
import com.oole.hw3.utility.PropertiesUtils;
import javassist.*;
import javassist.bytecode.AccessFlag;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;

/**
 * Access Modifier Mutation operator
 * This operator performs mutation by changing the access modifier of the fields in the class
 */
public class AccessModifierOperator implements Operator {

    /**
     * performs the mutation
     */
    @Override
    public void mutate() {
        try {
            System.out.println("Executing the access modifier operator");

            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(PropertiesUtils.getProperties().getProperty("sourceClassPath"));

            File f = new File(targetFolderEncapsulation);
            File f2 = new File(PropertiesUtils.getProperties().getProperty("testClassPath"));
            URL[] classpath = {f.toURI().toURL(), f2.toURI().toURL()};
            URLClassLoader mutatedUrlClassLoader = new URLClassLoader(classpath);

            File file = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath"));
            URL[] filePath = {file.toURI().toURL(), f2.toURI().toURL()};
            URLClassLoader orgUrlClassLoader = new URLClassLoader(filePath);

            List<String> classList = LauncherUtils.getClassNamesFromFileSystem(PropertiesUtils.getProperties().getProperty("sourceClassPath"), "");
            Collections.sort(classList, new ListOrderingComparator());

            // the below code takes one class at a time and applies the mutation
            for (String className : classList) {
                try {
                    if (!className.contains("$")) {
                        CtClass ctClass = pool.get(className);
                        CtMethod[] methods = ctClass.getDeclaredMethods();
                        for (CtMethod ctm : methods) {
                            if (ctm.getModifiers() == 1 || ctm.getModifiers() == 4)
                                ctm.setModifiers(AccessFlag.PRIVATE);
                        }
                        ctClass.writeFile(targetFolderEncapsulation);

                    } else {
                        String classLocation = className.replace(".", "\\");
                        File sourceFile = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath") + "\\" + classLocation + ".class");

                        File destinationFile = new File(targetFolderEncapsulation + "\\" + classLocation + ".class");
                        org.apache.commons.io.FileUtils.copyFile(sourceFile, destinationFile);
                    }
                } catch (NotFoundException | CannotCompileException | IOException e) {
                    e.printStackTrace();
                }
            }

            LauncherUtils.prepareClassesForExecution(classList, orgUrlClassLoader, mutatedUrlClassLoader);
        } catch (NotFoundException | IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        mutate();
    }
}
