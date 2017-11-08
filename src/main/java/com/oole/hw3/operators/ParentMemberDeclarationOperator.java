package com.oole.hw3.operators;

import com.oole.hw3.utility.FileUtils;
import javassist.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class ParentMemberDeclarationOperator implements Operator {

    @Override
    public void mutate() throws NotFoundException, CannotCompileException, IOException {
        System.out.println("Executing the parent member declaration operator");

        List<String> classList = FileUtils.getClassNamesInJar();
        for (String className : classList) {
            ClassPool pool = ClassPool.getDefault();
            ClassLoader classLoader = pool.getClassLoader();


            try {

                classLoader.loadClass(className);
                CtClass ctClass = pool.get(className);
                CtField[] ctFields = ctClass.getDeclaredFields();
                CtClass superClass = ctClass.getSuperclass();

                for (CtField ctField : ctFields) {
                    if (ctField.getType().getName().equals(ctClass.getName()) &&
                            !superClass.getName().equals("java.lang.Object"))
                        System.out.println("Changing type of " + ctField.getName() + " from " + ctField.getType().getName() + " to"
                                + " super class >" + superClass.getName());
                    ctField.setType(superClass);
                }
                ctClass.writeFile(targetFolderPolymorphism);

                String classPath = targetFolderPolymorphism + "/" + ctClass.getName().replace(".","/");
                //classLoader.loadClass(classPath);
                File targetsLocation = new File(classPath);
                URL url = null;
                try {
                    url = targetsLocation.toURI().toURL();
                } catch (MalformedURLException e) {
                    System.out.println("Error while generating URL object");
                }
                URL[] urls = new URL[]{ url };
                URLClassLoader urlClassLoader = new URLClassLoader(urls, classLoader);
                urlClassLoader.loadClass(ctClass.getName());
                String testClassName = ctClass.getName() + "Test";
                System.out.println("Test class name >>" + testClassName);
                System.out.println("Class name >>" + ctClass.getName());
                //System.out.println("New class path>> "+ classPath);

                Class testClass = urlClassLoader.loadClass(testClassName);
                Method[] methods = testClass.getMethods();
                Object obj = testClass.newInstance();
                System.out.println(methods.length);
                for(Method method : methods){
                    System.out.println("Invoking method >>" + method.getName());
                    method.invoke(obj,null);
                }

            } catch (ClassNotFoundException | NotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
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
