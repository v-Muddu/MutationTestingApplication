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
 * Hiding Variable Deletion operator
 * This operator performs mutation by deleting hiding variables in the sub class so that
 * superclass fields are accessed
 */
public class HidingVariableDeletionOperator implements Operator {

    /**
     * performs the mutation
     * @throws NotFoundException
     * @throws CannotCompileException
     * @throws IOException
     */
    @Override
    public void mutate() {
        try {
            System.out.println("Executing hiding variable deletion");

            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(PropertiesUtils.getProperties().getProperty("sourceClassPath"));

            File f = new File(targetFolderIHD);
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
                            CtField[] fields = superClass.getDeclaredFields();
                            for (CtField fd : fields) {
                                CtField[] ctFields = clazz.getDeclaredFields();
                                for (CtField subField : ctFields) {
                                    if (subField.getName().equals(fd.getName())) {
                                        //IHD
                                        clazz.removeField(subField);
                                        mutatedClassSet.add(clazz.getName());
                                    }
                                }
                            }
                        }
                        clazz.writeFile(targetFolderIHD);
                    } else {
                        String classLocation = className.replace(".", "\\");
                        File sourceFile = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath") + "\\" + classLocation + ".class");

                        File destinationFile = new File(targetFolderIHD + "\\" + classLocation + ".class");
                        org.apache.commons.io.FileUtils.copyFile(sourceFile, destinationFile);
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }

            LauncherUtils.prepareClassesForExecution("Hiding Variable Deletion Operator", classList, mutatedClassSet, orgUrlClassLoader, mutatedUrlClassLoader);
        }catch(NotFoundException | IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        mutate();
    }
}
