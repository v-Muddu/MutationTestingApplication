package com.oole.hw3.operators;

import com.oole.hw3.utility.FileUtils;
import com.oole.hw3.utility.ListOrderingComparator;
import com.oole.hw3.utility.PropertiesUtils;
import javassist.*;
import javassist.bytecode.AccessFlag;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;

public class AccessModifierOperator implements Operator {

    @Override
    public void mutate() throws NotFoundException, CannotCompileException, IOException{
        System.out.println("Executing the access modifier operator");

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(PropertiesUtils.getProperties().getProperty("sourceClassPath"));

        File f = new File(targetFolderEncapsulation);
        File f2 = new File(PropertiesUtils.getProperties().getProperty("testClassPath"));
        URL[] classpath = { f.toURI().toURL(),f2.toURI().toURL() };
        URLClassLoader urlClassLoader = new URLClassLoader(classpath);

        List<String> classList = FileUtils.getClassNamesFromFileSystem(PropertiesUtils.getProperties().getProperty("sourceClassPath"),"");
        Collections.sort(classList,new ListOrderingComparator());

        for(String className : classList){
            try {
                if(!className.contains("$")) {
                CtClass ctClass = pool.get(className);
                CtMethod[] methods = ctClass.getDeclaredMethods();
                for(CtMethod ctm : methods){
                    if(ctm.getModifiers() == 1 || ctm.getModifiers() == 4)
                    ctm.setModifiers(AccessFlag.PRIVATE);
                }
                ctClass.writeFile(targetFolderEncapsulation);

                }
                else{
                    String classLocation = className.replace(".","\\");
                    File sourceFile = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath") + "\\" + classLocation + ".class");

                    File destinationFile =  new File(targetFolderEncapsulation + "\\" + classLocation + ".class");
                    org.apache.commons.io.FileUtils.copyFile(sourceFile,destinationFile);
                }
            } catch (NotFoundException | CannotCompileException | IOException e) {
                e.printStackTrace();
            }
        }

        for(String className : classList){
            try{
                Class c = urlClassLoader.loadClass(className);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(String className : classList){
            try{

                String testClassName = className + "Test";
                System.out.println("Executing Test class >>" + testClassName);

                Class testClass = urlClassLoader.loadClass(testClassName);
                Method[] methods = testClass.getMethods();
                Object obj = testClass.newInstance();
                System.out.println(methods.length);
                for(Method method : methods){
                    System.out.println("Invoking method >>" + method.getName());
                    method.invoke(obj,null);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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
