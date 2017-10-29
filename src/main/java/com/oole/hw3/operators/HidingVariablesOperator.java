package com.oole.hw3.operators;

import com.oole.hw3.utility.FileUtils;
import javassist.*;
import java.io.IOException;
import java.util.List;

public class HidingVariablesOperator implements Operator {
    @Override
    public void mutate() throws NotFoundException, CannotCompileException, IOException {
        System.out.println("Executing hiding variable deletion");

        List<String> classList = FileUtils.getClassNamesInJar();
        for (String className : classList) {
            ClassPool pool = ClassPool.getDefault();
            ClassLoader classLoader = pool.getClassLoader();
            try{
                classLoader.loadClass(className);
                CtClass clazz = pool.get(className);
                clazz.defrost();
                if (clazz.getSuperclass().getName() != null) {
                    CtField[] fields = clazz.getSuperclass().getDeclaredFields();
                    for (CtField fd : fields) {
                        CtField[] ctFields = clazz.getDeclaredFields();
                        for (CtField subField : ctFields) {
                            if (subField.getName().equals(fd.getName())) {
                                //IHD
                                clazz.removeField(clazz.getField(subField.getName()));
                            }
                            // this is throwing a duplication exception, I'll investigate and update, you guys are welcome to make changes
                            //** this is (IHI)
                            if (fd.getType().getSimpleName() == "int" || fd.getType().getSimpleName() == "long" || fd.getType().getSimpleName() == "double" ||
                                    fd.getType().getSimpleName() == "float" || fd.getType().getSimpleName() == "char" || fd.getType().getSimpleName() == "byte" ||
                                    fd.getType().getSimpleName() == "short" || fd.getType().getSimpleName() == "java.lang.String" || fd.getType().getSimpleName() == "boolean") {
                                 if (!fd.getName().equals(subField.getName())) {
                                     clazz.addField(fd);
                                  }
                            }
                        }
                    }

                    //IOD
                    CtMethod[] methods = clazz.getDeclaredMethods();
                    CtMethod[] superMethods = clazz.getSuperclass().getDeclaredMethods();
                    for(CtMethod ctMethod : methods){
                        for(CtMethod superMethod : superMethods){
                            if(ctMethod.getName().equals(superMethod.getName())){
                                clazz.removeMethod(ctMethod);
                            }
                        }
                    }
                }
                clazz.writeFile(targetFolderInheritance);
            }catch (Exception e){
                e.getStackTrace();
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
