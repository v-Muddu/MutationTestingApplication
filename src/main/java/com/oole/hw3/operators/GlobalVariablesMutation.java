package com.oole.hw3.operators;

import com.oole.hw3.utility.FileUtils;
import javassist.*;

import java.io.IOException;
import java.util.List;

public class GlobalVariablesMutation implements Operator{
    @Override
    public void mutate() throws NotFoundException, CannotCompileException, IOException {
        List<String> classList = FileUtils.getClassNamesInJar();
        for (String className : classList) {
            ClassPool pool = ClassPool.getDefault();
            ClassLoader classLoader = pool.getClassLoader();
            try{
                classLoader.loadClass(className);
                CtClass clazz = pool.get(className);
                clazz.defrost();
                for (CtField ctf : clazz.getDeclaredFields()) {
                    //JSI
                    if (!Modifier.isStatic(ctf.getModifiers())) {
                        ctf.setModifiers(Modifier.STATIC);
                    }
                    
                    //JSD
                    if (Modifier.isStatic(ctf.getModifiers())){
                        ctf.setModifiers(ctf.getModifiers() & ~Modifier.STATIC);
                    }
                }
                clazz.writeFile(targetFolderJavaSpec);
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
