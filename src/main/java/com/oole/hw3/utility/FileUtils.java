package com.oole.hw3.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

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
                if(file.getName().endsWith(".class")){
                    String name = file.getName();
                    String className = name.replaceAll("/",".");

                    //if(!className.contains("$")) {
                        classList.add(packagePath + className.substring(0,className.length()-6));
                    //}
                }

            }
        }
        return classList;
    }
}
