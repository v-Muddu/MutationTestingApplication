package com.oole.hw3.operators;

import com.oole.hw3.utility.FileUtils;
import javassist.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OverloadingMethodDeletionOperator implements Operator {

    @Override
    public void mutate() throws NotFoundException, CannotCompileException, IOException {
        System.out.println("Executing the overloading method deletion operator");

        List<String> classList = FileUtils.getClassNamesFromFileSystem("D:\\git\\instrumentated_app_hw2\\out\\production\\classes","");
        for(String className : classList){
            ClassPool pool = ClassPool.getDefault();
            ClassLoader classLoader = pool.getClassLoader();
            try {
                classLoader.loadClass(className);
                CtClass ctClass = pool.get(className);
                CtMethod[] methods = ctClass.getDeclaredMethods();
                Set<String> methodSet = new HashSet<>();
                for(CtMethod ctMethod : methods){
                    if(methodSet.add(ctMethod.getName()) == false){
                        System.out.println("Removing overloaded method " + ctMethod.getName()
                                + " from class " + ctClass.getName());
                        ctClass.removeMethod(ctMethod);

                    }
                }
                ctClass.writeFile(targetFolderPolymorphism);
            } catch (ClassNotFoundException | NotFoundException | CannotCompileException | IOException e) {
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
