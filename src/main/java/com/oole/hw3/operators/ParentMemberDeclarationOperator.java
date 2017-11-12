package com.oole.hw3.operators;

import com.oole.hw3.utility.FileUtils;
import javassist.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class ParentMemberDeclarationOperator implements Operator {

    @Override
    public void mutate() throws NotFoundException, CannotCompileException, IOException {
        System.out.println("Executing the parent member declaration operator");

        List<String> classList = FileUtils.getClassNamesFromFileSystem("D:\\git\\instrumentated_app_hw2\\out\\production\\classes","");
        for (String className : classList) {
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath("D:\\git\\instrumentated_app_hw2\\out\\production\\classes");
            //ClassLoader classLoader = pool.getClassLoader();


            try {

                //classLoader.loadClass(className);
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

                File f = new File("D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\Polymorphism");
                File f2 = new File("D:\\git\\instrumentated_app_hw2\\build\\libs\\commons-lang3-3.7-SNAPSHOT-tests");
                URL[] classpath = { f.toURI().toURL(),f2.toURI().toURL() };
                URLClassLoader urlClassLoader = new URLClassLoader(classpath);
                Class c = urlClassLoader.loadClass(ctClass.getName());
                System.out.println(c.getCanonicalName());
                System.out.println(c.getMethods().length);

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

            } catch (NotFoundException e) {
                e.printStackTrace();
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
