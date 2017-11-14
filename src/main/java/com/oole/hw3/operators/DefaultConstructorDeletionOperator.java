package com.oole.hw3.operators;

import com.oole.hw3.utility.LauncherUtils;
import com.oole.hw3.utility.ListOrderingComparator;
import com.oole.hw3.utility.PropertiesUtils;
import javassist.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Java-Specified Default Constructor Deletion
 * This operator performs mutation by deletin the default constructor in a class
 */
public class DefaultConstructorDeletionOperator implements Operator {
    /**
     *
     * Performs mutation
     */
    @Override
    public void mutate() {
        try{
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(PropertiesUtils.getProperties().getProperty("sourceClassPath"));

            File f = new File(targetFolderJDC);
            File f2 = new File(PropertiesUtils.getProperties().getProperty("testClassPath"));
            URL[] classpath = {f.toURI().toURL(), f2.toURI().toURL()};
            URLClassLoader mutatedUrlClassLoader = new URLClassLoader(classpath);

            File file = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath"));
            URL[] filePath = {file.toURI().toURL(), f2.toURI().toURL()};
            URLClassLoader orgUrlClassLoader = new URLClassLoader(filePath);

            Set<String> mutatedClassSet = new HashSet<>();
            List<String> classList = LauncherUtils.getClassNamesFromFileSystem(PropertiesUtils.getProperties().getProperty("sourceClassPath"), "");
            Collections.sort(classList, new ListOrderingComparator());

            // the below code takes one class at a time and applies the mutation
            for(String className : classList){
                try{
                    if(!className.contains("$")){
                        CtClass ctClass = pool.get(className);
                        CtConstructor[] classConstructors = ctClass.getConstructors();
                        System.out.println("the total no of constructors before mutation: " + classConstructors.length);
                        for(CtConstructor constructor : classConstructors){
                            if(constructor.isEmpty()){
                                ctClass.removeConstructor(constructor);
                                System.out.println("Constructor removed: " + constructor.getName());
                                mutatedClassSet.add(className);
                            }
                        }
                        ctClass.writeFile(targetFolderJDC);
                    } else{
                        String classLocation = className.replace(".","/");
                        File sourceFile = new File(PropertiesUtils.getProperties().getProperty("source") + "/" + classLocation + ".class");

                        File destinationFile = new File(targetFolderJDC + "/" + classLocation + ".class");
                        org.apache.commons.io.FileUtils.copyFile(sourceFile, destinationFile);
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            LauncherUtils.prepareClassesForExecution("Default Constructor Deletion Operator", classList, mutatedClassSet, orgUrlClassLoader, mutatedUrlClassLoader);
        }catch(NotFoundException | IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        mutate();
    }
}
