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
import java.util.List;

public class HidingVariableInsertionOperator implements Operator {

    @Override
    public void mutate() throws NotFoundException, CannotCompileException, IOException {
        System.out.println("Executing hiding variable deletion");

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath("D:\\git\\instrumentated_app_hw2\\out\\production\\classes");

        File f = new File("D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\Inheritance");
        File f2 = new File(PropertiesUtils.getProperties().getProperty("testClassPath"));
        URL[] classpath = { f.toURI().toURL(),f2.toURI().toURL() };
        URLClassLoader urlClassLoader = new URLClassLoader(classpath);

        List<String> classList = LauncherUtils.getClassNamesFromFileSystem("D:\\git\\instrumentated_app_hw2\\out\\production\\classes","");
        Collections.sort(classList,new ListOrderingComparator());

        for (String className : classList) {
            try{
                if(!className.contains("$")) {
                    CtClass clazz = pool.get(className);
                    CtClass superClass = clazz.getSuperclass();
                    if (superClass != null && !superClass.getName().equals("java.lang.Object")) {
                        CtField[] fields = superClass.getDeclaredFields();
                        for (CtField fd : fields) {
                            CtField[] ctFields = clazz.getDeclaredFields();

                            if (fd.getType().getSimpleName() == "int" || fd.getType().getSimpleName() == "long" || fd.getType().getSimpleName() == "double" ||
                                    fd.getType().getSimpleName() == "float" || fd.getType().getSimpleName() == "char" || fd.getType().getSimpleName() == "byte" ||
                                    fd.getType().getSimpleName() == "short" || fd.getType().getSimpleName() == "java.lang.String" || fd.getType().getSimpleName() == "boolean") {
                                boolean fieldFound = false;
                                for (CtField subField : ctFields) {
                                    //** this is (IHI)
                                    String superClassField = fd.getName();
                                    String subClassField = subField.getName();

                                    if (superClassField.equals(subClassField)) {
                                        fieldFound = true;
                                        break;
                                    }
                                }
                                if(!fieldFound) {
                                    fd.setType(clazz);
                                    //CtField ctField = CtField.make();

                                    clazz.addField(fd);
                                    System.out.println("Adding variable " + fd.getName() + " for class " +
                                    clazz.getName());
                                }
                            }
                        }
                    }
                    //clazz.writeFile(targetFolderInheritance);
                }
                else{
                    String classLocation = className.replace(".","\\");
                    File sourceFile = new File("D:\\git\\instrumentated_app_hw2\\out\\production\\classes\\" + classLocation + ".class");

                    File destinationFile =  new File("D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\Inheritance" + "\\" + classLocation + ".class");
                    org.apache.commons.io.FileUtils.copyFile(sourceFile,destinationFile);
                }
            }catch (Exception e){
                e.getStackTrace();
            }
        }
        for(String className : classList){
            try{
                urlClassLoader.loadClass(className);

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
