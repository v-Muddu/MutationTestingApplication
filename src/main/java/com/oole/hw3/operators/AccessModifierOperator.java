package com.oole.hw3.operators;

import com.oole.hw3.utility.FileUtils;
import javassist.*;
import javassist.bytecode.AccessFlag;

import java.io.IOException;
import java.util.List;

public class AccessModifierOperator implements Operator {

    @Override
    public void mutate() {
        System.out.println("Executing the access modifier operator");

        List<String> classList = FileUtils.getClassNamesInJar();
        for(String className : classList){
            ClassPool pool = ClassPool.getDefault();
            ClassLoader classLoader = pool.getClassLoader();
            try {
                classLoader.loadClass(className);
                CtClass ctClass = pool.get(className);
                CtMethod[] methods = ctClass.getDeclaredMethods();
                for(CtMethod ctm : methods){
                    ctm.setModifiers(AccessFlag.PRIVATE);
                }
                ctClass.writeFile(targetFolderEncapsulation);
            } catch (ClassNotFoundException | NotFoundException | CannotCompileException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        mutate();
    }
}
