package com.oole.hw3.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Launcher Utils
 * Utility class with multiple utility methods
 */
public class LauncherUtils {

    /**
     * Given a directory path returns up all .class file names in it and all sub directories
     * @param dirPath Path to directory containing .class files
     * @param packagePath Used to construct package structure for class names
     * @return Returns list of class names
     */
    public static List<String> getClassNamesFromFileSystem(String dirPath, String packagePath) {

        List<String> classList = new ArrayList<>();
        File directory = new File(dirPath);
        File[] listOfFilesInDirectory = directory.listFiles();

        for(File file : listOfFilesInDirectory){
            if(file.isDirectory()){

                List<String> fileList = getClassNamesFromFileSystem(file.getPath(),packagePath + file.getName() + ".");
                if(null != fileList && !fileList.isEmpty())
                    classList.addAll(fileList);

            } else if(file.isFile()){

                if(file.getName().endsWith(".class") && !file.getName().contains("ApacheCommonsDriver")){
                    String name = file.getName();
                    String className = name.replaceAll("/",".");

                    classList.add(packagePath + className.substring(0,className.length()-6));
                }
            }
        }
        return classList;
    }

    /**
     * executes test class corresponding to passed class name
     * Classloader instance passed is used to load class and test class
     * @param className Fully qualified class name
     * @param urlClassLoader Classloader instance used to load classes
     * @return List of lines in the trace file generated as result of execution
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static synchronized List<String> executeClassFromTestClass(String className, URLClassLoader urlClassLoader) {

        List<String> lines = null;
        try {
            Class cls = urlClassLoader.loadClass(className);
            System.out.println("Loading class: " + cls.getCanonicalName());

            String testClassName = className + "Test";
            System.out.println("Executing Test class: " + testClassName);
            Class testCls = urlClassLoader.loadClass(testClassName);

            Method[] methods = testCls.getMethods();
            Object obj = testCls.newInstance();
            System.out.println("Creating instance for : " + testClassName);

            String path = PropertiesUtils.getProperties().getProperty("traceFilePath");

            // clean trace file before execution of test cases
            cleanTraceFile(path);

            long startTime = System.currentTimeMillis();

            for (Method method : methods) {
                long currentTime = System.currentTimeMillis();
                if((currentTime-startTime) > 30000){
                    break;
                }

                System.out.println("Invoking test method: " + method.getName());

                try {
                    method.invoke(obj, null);
                } catch (Exception | Error e) {
                    System.out.println("Error while invoking the method: " + method
                            + "from class: " + cls.getName());
                }
            }
            System.out.println("Done Executing Test class: " + testClassName);
            // read all lines in traces file into list
            lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
        } catch (Exception | Error e){
            System.out.println("Error while executing test class for class: " + className);
        }
        return lines;
    }

    /**
     * Compares list of lines in trace files
     * Used to compare trace files generated from execution of class and test class
     * @param lines trace generated from original execution of class file
     * @param mutatedLines trace generated from execution of mutated class file
     * @return return true or false depending on if the trace files match or not
     * @throws FileNotFoundException
     */
    public static boolean compareTraces(List<String> lines, List<String> mutatedLines) {
        try {
            if(null == lines || null == mutatedLines)
                return false;
            if(lines.size() != mutatedLines.size()){
                return  false;
            }
            Iterator<String> it1 = lines.iterator();
            Iterator<String> it2 = mutatedLines.iterator();

            while (it1.hasNext() && it2.hasNext()) {
                String line = it1.next();
                String mutatedLine = it2.next();

                if (null != line && null != mutatedLine && !line.equals(mutatedLine)) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  true;
    }

    /**
     * Clears the trace file in the given path
     * @param path path to the trace file
     * @throws FileNotFoundException
     */
    public static synchronized void cleanTraceFile(String path) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File(path));
        writer.print("");
        writer.close();
    }

    /**
     * Takes class list as input and calls the service for execution of test cases and classes
     * separated into a method only because all operators use this code fragment
     * @param classList
     * @param orgUrlClassLoader
     * @param mutatedUrlClassLoader
     * @param operatorName
     * @param mutatedClassSet
     */
    public static synchronized void prepareClassesForExecution(String operatorName, List<String> classList, Set<String> mutatedClassSet, URLClassLoader orgUrlClassLoader, URLClassLoader mutatedUrlClassLoader){
        int classCount = 0;
        for (String className : classList) {
            if(className.contains("$") || className.contains("Fraction"))
                continue;
            if(classCount > 10)
                break;

            try {
                List<String> csvLine = new ArrayList<>();
                csvLine.add(operatorName);
                csvLine.add(className);
                if(mutatedClassSet.contains(className))
                    csvLine.add("YES");
                else {
                    csvLine.add("NO");
                    csvLine.add("--");
                    CSVUtils.writeLine(CSVUtils.writer, csvLine);
                    CSVUtils.writer.flush();
                    continue;
                }
                classCount++;

                List<String> lines = LauncherUtils.executeClassFromTestClass(className, orgUrlClassLoader);
                List<String> mutatedLines = LauncherUtils.executeClassFromTestClass(className, mutatedUrlClassLoader);
                boolean result = LauncherUtils.compareTraces(lines, mutatedLines);

                if(result)
                    csvLine.add("NO");
                else
                    csvLine.add("YES");

                CSVUtils.writeLine(CSVUtils.writer, csvLine);

                CSVUtils.writer.flush();
                System.out.println("Comparing trace files for class name: " + className + " result: " + result);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
