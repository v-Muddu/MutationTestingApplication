package com.oole.hw3;

import com.oole.hw3.utility.LauncherUtils;
import com.oole.hw3.utility.PropertiesUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class LauncherUtilsTest {


    @Test
    public void testGetClassNamesFromFileSystem(){
        List<String> classes = LauncherUtils.getClassNamesFromFileSystem(PropertiesUtils.getProperties().getProperty("sourceClassPath"), "");
        Assert.assertNotEquals(classes,null);
        Assert.assertNotEquals(classes.size(),0);
    }

    @Test
    public void testExecuteClassFromTestClass(){
        File f2 = new File(PropertiesUtils.getProperties().getProperty("testClassPath"));
        File file = new File(PropertiesUtils.getProperties().getProperty("sourceClassPath"));
        URL[] filePath;
        try {
            filePath = new URL[]{file.toURI().toURL(), f2.toURI().toURL()};
            URLClassLoader orgUrlClassLoader = new URLClassLoader(filePath);

            List<String> lines = LauncherUtils.executeClassFromTestClass("org.apache.commons.lang3.AnnotationUtils",orgUrlClassLoader);
            Assert.assertNotEquals(lines,null);
            Assert.assertNotEquals(lines.size(),0);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCompareTracesEqual(){

        List<String> firstList = new ArrayList<>();
        List<String> secondList = new ArrayList<>();

        firstList.add("abc");
        firstList.add("xyz");
        firstList.add("opq");

        secondList.add("abc");
        secondList.add("xyz");
        secondList.add("opq");

        Assert.assertTrue(LauncherUtils.compareTraces(firstList,secondList));

    }

    @Test
    public void testCompareTracesUnEqual(){

        List<String> firstList = new ArrayList<>();
        List<String> secondList = new ArrayList<>();

        firstList.add("123");
        firstList.add("456");

        secondList.add("abc");
        secondList.add("xyz");

        Assert.assertFalse(LauncherUtils.compareTraces(firstList,secondList));

    }

    @Test
    public void testCompareTracesNull(){

        List<String> firstList = null;
        List<String> secondList = new ArrayList<>();

        secondList.add("abc");
        secondList.add("xyz");
        Assert.assertNull(firstList);
        Assert.assertFalse(LauncherUtils.compareTraces(firstList,secondList));

    }


}
