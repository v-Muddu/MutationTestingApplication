package com.oole.hw3;


import javassist.*;
import javassist.tools.reflect.Reflection;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class Main
{
    final static String targetPackage = "org.apache.commons.lang3.math";
    final static String targetFolder = "mutatedFiles/AMC";

    public static void main(String[] args) throws IOException, CannotCompileException, NotFoundException, ClassNotFoundException {
        Main main = new Main();
        main.mutatedModifiers(targetPackage, targetFolder);
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
                ctm.setModifiers(2);
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
