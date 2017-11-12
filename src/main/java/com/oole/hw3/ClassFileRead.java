package com.oole.hw3;

import com.oole.hw3.utility.CustomClassLoader;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class ClassFileRead {
    public static void main(String[] args){

        //ClassPool pool = new ClassPool();
        try {
            File targetsLocation = new File("D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\Polymorphism\\org\\apache\\commons\\lang3\\math");
            URL url = null;
            try {
                url = targetsLocation.toURI().toURL();
            } catch (MalformedURLException e) {
                System.out.println("Error while generating URL object");
            }
            URL[] urls = new URL[]{ url };
            //CustomClassLoader urlClassLoader = new CustomClassLoader(Arrays.asList(urls));
            URLClassLoader urlClassLoader = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());
            //urlClassLoader.loadClass(ctClass.getName());
            //pool.insertClassPath("D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\Polymorphism\\org\\apache\\commons\\lang3");
            //ClassLoader classLoader = pool.getClassLoader();
            Class ctClass = urlClassLoader.loadClass("org.apache.commons.lang3.math.Fraction");
            System.out.println(ctClass.getMethods()[0].getName());
            Field[] ctFields = ctClass.getDeclaredFields();

            for (Field ctField : ctFields) {
                //if (ctField.getType().getName().equals(superClass.getName()))
                    System.out.println("Field Name " + ctField.getType() + " " +  ctField.getName());
                //ctField.setType(superClass);
            }
        } /*catch (NotFoundException e) {
            e.printStackTrace();
        }*/ catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
