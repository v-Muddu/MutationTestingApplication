package com.oole.hw3.operators;

import com.oole.hw3.utility.PropertiesUtils;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.io.IOException;

public interface Operator extends Runnable {
    String targetFolderEncapsulation = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\AMC";
    String targetFolderIHD = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\IHD";
    String targetFolderIOD = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\IOD";
    String targetFolderJSI = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\JSI";
    String targetFolderJSD = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\JSD";
    String targetFolderPMD = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\PMD";
    String targetFolderOMD = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\OMD";
    void mutate() throws NotFoundException, CannotCompileException, IOException;
}
