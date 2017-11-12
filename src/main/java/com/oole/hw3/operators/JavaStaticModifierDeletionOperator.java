package com.oole.hw3.operators;

import com.oole.hw3.utility.FileUtils;
import com.oole.hw3.utility.ListOrderingComparator;
import javassist.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;

public class JavaStaticModifierDeletionOperator implements Operator {
    @Override
    public void mutate() throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath("D:\\git\\instrumentated_app_hw2\\out\\production\\classes");

        File f = new File(targetFolderJSD);
        File f2 = new File("D:\\git\\instrumentated_app_hw2\\build\\libs\\commons-lang3-3.7-SNAPSHOT-tests");
        URL[] classpath = { f.toURI().toURL(),f2.toURI().toURL() };
        URLClassLoader urlClassLoader = new URLClassLoader(classpath);

        List<String> classList = FileUtils.getClassNamesFromFileSystem("D:\\git\\instrumentated_app_hw2\\out\\production\\classes","");
        Collections.sort(classList,new ListOrderingComparator());

        for (String className : classList) {
            if(!className.contains("$")) {
                CtClass clazz = pool.get(className);
                clazz.defrost();
                for (CtField ctf : clazz.getDeclaredFields()) {
                    //JSI
                    if (!Modifier.isStatic(ctf.getModifiers())) {
                        ctf.setModifiers(Modifier.STATIC);
                    }

                }
                clazz.writeFile(targetFolderJSD);
            }  else{
                String classLocation = className.replace(".","\\");
                File sourceFile = new File("D:\\git\\instrumentated_app_hw2\\out\\production\\classes\\" + classLocation + ".class");

                File destinationFile =  new File(targetFolderJSD + "\\" + classLocation + ".class");
                org.apache.commons.io.FileUtils.copyFile(sourceFile,destinationFile);
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
