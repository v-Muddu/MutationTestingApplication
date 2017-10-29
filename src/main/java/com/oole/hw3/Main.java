package com.oole.hw3;

import javassist.*;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.apache.commons.lang3.math.Fraction;
import sun.awt.geom.AreaOp;
import sun.jvm.hotspot.oops.AccessFlags;
import sun.net.www.protocol.file.FileURLConnection;

import javax.lang.model.type.PrimitiveType;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.rmi.AccessException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main
{
    final static String targetFolderAMC = "mutatedFiles/AMC";
    final static String targetFolderInheritance = "mutatedFiles/Inheritance";
    final static String targetFolderPoly = "mutatedFiles/Polymorphism";
    final static String taregetFolderJavaSpec = "mutatedFiles/JavaSpec";
    final static String targetPackageTime = "org.apache.commons.lang3.time";
    final static String targetPackageArch = "org.apache.commons.lang3.arch";
    final static String targetPackageBuilder = "org.apache.commons.lang3.builder";
    final static String targetPackageConcurrent = "org.apache.commons.lang3.concurrent";
    final static String targetPackageEvent = "org.apache.commons.lang3.event";
    final static String targetPackageException = "org.apache.commons.lang3.exception";
    final static String targetPackageMath = "org.apache.commons.lang3.math";
    final static String targetPackageMutable = "org.apache.commons.lang3.mutable";
    final static String targetPackageReflect = "org.apache.commons.lang3.reflect";
    final static String targetPackageText = "org.apache.commons.lang3.text";
    final static String targetPackageTuple = "org.apache.commons.lang3.tuple";
    final static String targetPackageMain = "org.apache.commons.lang3";


    public static void main(String[] args) throws IOException, CannotCompileException, NotFoundException, ClassNotFoundException {
        Main main = new Main();
       // main.mutatedModifiers(targetPackageTime, targetFolder);
        // main.mutatedModifiers(targetPackageArch, targetFolder);
       // main.mutatedModifiers(targetPackageBuilder, targetFolder);
      //  main.mutatedModifiers(targetPackageConcurrent, targetFolder);
      //  main.mutatedModifiers(targetPackageEvent, targetFolder);
     //   main.mutatedModifiers(targetPackageException, targetFolder);
      //  main.mutatedModifiers(targetPackageMath, targetFolder);
      //  main.mutatedModifiers(targetPackageMutable, targetFolder);
     //   main.mutatedModifiers(targetPackageReflect, targetFolder);
     //   main.mutatedModifiers(targetPackageText, targetFolder);
     //   main.mutatedModifiers(targetPackageTuple, targetFolder);
        //main.mutatedModifiers(targetPackageMain, targetFolder);
       // main.methodMutation(targetPackageMath, targetFolderJavaSpec);
        main.varMutation(targetPackageMath, targetFolderInheritance);
    }

    //for whether the subclass has a superclass, and if does it'll check whether subclass inherits an instance variable from the super, and deletes it (IHD)
    public void varMutation(String targetPackage, String targetFolder) throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException {
        List<?> classList = getClassesForPackage(targetPackage);
        for (Object classes : classList) {
            ClassPool pool = ClassPool.getDefault();
            ClassLoader classLoader = pool.getClassLoader();
            String className = ((Class) classes).getCanonicalName().toString();
            classLoader.loadClass(className);
            CtClass ctClass = pool.get(className);
            if (ctClass.getSuperclass().getName() != null) {
                CtField[] fields = ctClass.getSuperclass().getDeclaredFields();
                for (CtField fd : fields) {
                    CtField[] ctFields = ctClass.getDeclaredFields();
                    for (CtField subField : ctFields) {
                        if (subField.getName().equals(fd.getName())) {
                            ctClass.removeField(ctClass.getField(subField.getName()));
                        }
                        // this is throwing a duplication exception, I'll investigate and update, you guys are welcome to make changes
                        //** this is (IHI)
                        if (fd.getType().getSimpleName() == "int" || fd.getType().getSimpleName() == "long" || fd.getType().getSimpleName() == "double" ||
                                fd.getType().getSimpleName() == "float" || fd.getType().getSimpleName() == "char" || fd.getType().getSimpleName() == "byte" ||
                                fd.getType().getSimpleName() == "short" || fd.getType().getSimpleName() == "java.lang.String" || fd.getType().getSimpleName() == "boolean") {
                           // if (!fd.getName().equals(subField.getName())) {
                          //      ctClass.addField(fd);
                          //  }
                        }
                    }
                }
            }
            ctClass.writeFile(targetFolder);
        }
    }

    // this methods get a given declared method name, and then invokes the instrument object to replace the method body,
    // **for now this doesn't work, and is throwing a result not stored in $_ exception
    public void methodMutation(String targetPackage, String targetFolder) throws NotFoundException, CannotCompileException, ClassNotFoundException {
        List<?> classList = getClassesForPackage(targetPackage);
        for (Object classes : classList) {
            ClassPool pool = ClassPool.getDefault();
            ClassLoader classLoader = pool.getClassLoader();
            String className = ((Class) classes).getCanonicalName().toString();
            classLoader.loadClass(className);
            CtClass ctClass = pool.get(className);
            CtMethod[] methods = ctClass.getDeclaredMethods();
            for(CtMethod ctMethod: methods){
                if(ctMethod.getName() == "getReducedFraction"){
                    ctMethod.instrument(new ExprEditor() {
                        @Override
                        public void edit(MethodCall m) throws CannotCompileException {
                            StringBuilder sb = new StringBuilder();
                            sb.append("        if (denominator != 0) {\n");
                            sb.append("            TemplateClass.instrum(209, \"If\", new PairClass[]{new PairClass(\"denominator\", TemplateClass.valueOf(denominator))});\n");
                            sb.append("            throw new ArithmeticException(\"The denominator must not be zero\");\n");
                            sb.append("        } else if (numerator == false) {\n");
                            sb.append("            TemplateClass.instrum(212, \"If\", new PairClass[]{new PairClass(\"numerator\", TemplateClass.valueOf(Integer.valueOf((int)numerator)))});\n");
                            sb.append("            TemplateClass.instrum(213, \"Return\", new PairClass[0]);\n");
                            sb.append("            return ZERO;\n");
                            sb.append("        } else {\n");
                            sb.append("            if (denominator == -2147483648 && (numerator & true) == 0) {\n");
                            sb.append("                TemplateClass.instrum(216, \"If\", new PairClass[]{new PairClass(\"denominator\", TemplateClass.valueOf(denominator)), new PairClass(\"numerator\", TemplateClass.valueOf(Integer.valueOf((int)numerator)))});\n");
                            sb.append("                TemplateClass.instrum(216, \"If\", new PairClass[]{new PairClass(\"denominator\", TemplateClass.valueOf(denominator)), new PairClass(\"numerator\", TemplateClass.valueOf(Integer.valueOf((int)numerator)))});\n");
                            sb.append("                denominator /= 2;\n");
                            sb.append("            }\n");
                            sb.append("            if (denominator < 0) {\n");
                            sb.append("                TemplateClass.instrum(220, \"If\", new PairClass[]{new PairClass(\"denominator\", TemplateClass.valueOf(denominator))});\n");
                            sb.append("                if (numerator == -2147483648 || denominator == -2147483648) {\n");
                            sb.append("                    TemplateClass.instrum(221, \"If\", new PairClass[]{new PairClass(\"numerator\", TemplateClass.valueOf(Integer.valueOf((int)numerator))), new PairClass(\"denominator\", TemplateClass.valueOf(denominator))});\n");
                            sb.append("                    throw new ArithmeticException(\"overflow: can't negate\");\n");
                            sb.append("                }\n");
                            sb.append("                numerator = -numerator;\n");
                            sb.append("                denominator = -denominator;\n");
                            sb.append("            }\n");
                            sb.append("            gcd = greatestCommonDivisor((int)numerator, denominator);\n");
                            sb.append("            TemplateClass.instrum(228, \"Assign\", new PairClass[]{new PairClass(\"gcd\", TemplateClass.valueOf(gcd))});\n");
                            sb.append("            int numerator = numerator / gcd;\n");
                            sb.append("            denominator /= gcd;\n");
                            sb.append("            TemplateClass.instrum(231, \"Return\", new PairClass[]{new PairClass(\"numerator\", TemplateClass.valueOf(numerator)), new PairClass(\"denominator\", TemplateClass.valueOf(denominator))});\n");
                            sb.append("            return new Fraction(numerator, denominator);\n");
                            sb.append("        }\n");
                            sb.append("        }\n");
                            m.replace(sb.toString());
                            try {
                                ctClass.writeFile(targetPackageMath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }


    /*
    * this method mutates all the modifiers from private to public for all the classes in a given package
    */
    public void mutatedModifiers(String targetPackage, String targetFolder ) throws NotFoundException, CannotCompileException, IOException, ClassNotFoundException {
        List<?> classList = getClassesForPackage(targetPackage);
        for( Object classes : classList){
            ClassPool pool = ClassPool.getDefault();
            ClassLoader classLoader = pool.getClassLoader();
            String className = ((Class) classes).getCanonicalName().toString();
            classLoader.loadClass(className);
            CtClass ctClass = pool.get(className);
            CtMethod[] methods = ctClass.getDeclaredMethods();
            for(CtMethod ctm : methods){
                ctm.setModifiers(AccessFlag.PRIVATE);
            }
            ctClass.writeFile(targetFolder);
        }
    }

    private static void checkDirectory(File directory, String pckgname, ArrayList<Class<?>> classes) throws ClassNotFoundException {
        if (directory.exists() && directory.isDirectory()) {
            String[] files = directory.list();
            String[] var5 = files;
            int var6 = files.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String file = var5[var7];
                if (file.endsWith(".class")) {
                    try {
                        classes.add(Class.forName(pckgname + '.' + file.substring(0, file.length() - 6)));
                    } catch (NoClassDefFoundError var10) {
                        ;
                    }
                } else {
                    File tmpDirectory;
                    if ((tmpDirectory = new File(directory, file)).isDirectory()) {
                        checkDirectory(tmpDirectory, pckgname + "." + file, classes);
                    }
                }
            }
        }

    }

    private static void checkJarFile(JarURLConnection connection, String pckgname, ArrayList<Class<?>> classes) throws ClassNotFoundException, IOException {
        JarFile jarFile = connection.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        JarEntry jarEntry = null;

        while(entries.hasMoreElements() && (jarEntry = (JarEntry)entries.nextElement()) != null) {
            String name = jarEntry.getName();
            if (name.contains(".class")) {
                name = name.substring(0, name.length() - 6).replace('/', '.');
                if (name.contains(pckgname)) {
                    classes.add(Class.forName(name));
                }
            }
        }

    }

    public static ArrayList<Class<?>> getClassesForPackage(String pckgname) throws ClassNotFoundException {
        ArrayList classes = new ArrayList();

        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            } else {
                Enumeration<URL> resources = cld.getResources(pckgname.replace('.', '/'));
                URL url = null;

                while(resources.hasMoreElements() && (url = (URL)resources.nextElement()) != null) {
                    try {
                        URLConnection connection = url.openConnection();
                        if (connection instanceof JarURLConnection) {
                            checkJarFile((JarURLConnection)connection, pckgname, classes);
                        } else {
                            if (!(connection instanceof FileURLConnection)) {
                                throw new ClassNotFoundException(pckgname + " (" + url.getPath() + ") does not appear to be a valid package");
                            }

                            try {
                                checkDirectory(new File(URLDecoder.decode(url.getPath(), "UTF-8")), pckgname, classes);
                            } catch (UnsupportedEncodingException var7) {
                                throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Unsupported encoding)", var7);
                            }
                        }
                    } catch (IOException var8) {
                        throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname, var8);
                    }
                }

                return classes;
            }
        } catch (NullPointerException var9) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Null pointer exception)", var9);
        } catch (IOException var10) {
            throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname, var10);
        }
    }
}
