package com.oole.hw3.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtils {
    public static List<String> classList = new ArrayList<>();

    public static List<String> getClassNamesInJar() {
        try {
            JarFile jarFile = new JarFile("lib/commons-lang3-3.7-SNAPSHOT.jar");
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                String className = name.replaceAll("/",".");

                if(className.endsWith(".class")) {
                    classList.add(className.substring(0,className.length()-6));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classList;
    }

}
