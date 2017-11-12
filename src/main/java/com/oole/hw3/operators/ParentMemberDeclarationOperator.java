package com.oole.hw3.operators;

import com.oole.hw3.utility.FileUtils;
import com.oole.hw3.utility.ListOrderingComparator;
import javassist.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;

public class ParentMemberDeclarationOperator implements Operator {

    @Override
    public void mutate() throws NotFoundException, CannotCompileException, IOException {
        System.out.println("Executing the parent member declaration operator");

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath("D:\\git\\instrumentated_app_hw2\\out\\production\\classes");

        File f = new File("D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\Polymorphism");
        File f2 = new File("D:\\git\\instrumentated_app_hw2\\build\\libs\\commons-lang3-3.7-SNAPSHOT-tests");
        URL[] classpath = { f.toURI().toURL(),f2.toURI().toURL() };
        URLClassLoader urlClassLoader = new URLClassLoader(classpath);

        List<String> classList = FileUtils.getClassNamesFromFileSystem("D:\\git\\instrumentated_app_hw2\\out\\production\\classes","");
        Collections.sort(classList,new ListOrderingComparator());
        for (String className : classList) {

            try {

                if(!className.contains("$")) {
                    CtClass ctClass = pool.get(className);
                    CtField[] ctFields = ctClass.getDeclaredFields();
                    CtClass superClass = ctClass.getSuperclass();

                    /*for (CtField ctField : ctFields) {
                        if (ctField.getType().getName().equals(ctClass.getName()) &&
                                !superClass.getName().equals("java.lang.Object"))
                            System.out.println("Changing type of " + ctField.getName() + " from " + ctField.getType().getName() + " to"
                                    + " super class >" + superClass.getName());
                        ctField.setType(superClass);
                    }*/

                    ctClass.writeFile(targetFolderPolymorphism);
                }
                else{
                    String classLocation = className.replace(".","\\");
                    File sourceFile = new File("D:\\git\\instrumentated_app_hw2\\out\\production\\classes\\" + classLocation + ".class");

                    File destinationFile =  new File("D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\Polymorphism" + "\\" + classLocation + ".class");
                    //org.apache.commons.io.FileUtils.copyDirectory();
                    org.apache.commons.io.FileUtils.copyFile(sourceFile,destinationFile);
                }
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        for(String className : classList){
            try{
                //CtClass ctClass = pool.get(className);

                Class c = urlClassLoader.loadClass(className);
                System.out.println(c.getCanonicalName());
                System.out.println(c.getMethods().length);

                String testClassName = className + "Test";
                System.out.println("Test class name >>" + testClassName);
                System.out.println("Class name >>" + className);

                /*Class testClass = urlClassLoader.loadClass(testClassName);
                Method[] methods = testClass.getMethods();
                Object obj = testClass.newInstance();
                System.out.println(methods.length);
                for(Method method : methods){
                    System.out.println("Invoking method >>" + method.getName());
                    method.invoke(obj,null);
                }*/

            } catch (Exception e) {
                System.out.println("Error");
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
