package com.oole.hw3.operators;

import com.oole.hw3.utility.PropertiesUtils;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.io.IOException;

/**
 * The Operator interface
 * This is implemented by various mutation operator classes and run as a thread
 */
public interface Operator extends Runnable {

    // destination folder for storing mutation code for access modifier operator
    String targetFolderEncapsulation = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\AMC";

    // destination folder for storing mutation code for hiding variable insertion operator
    String targetFolderIHD = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\IHD";

    // destination folder for storing mutation code for overloading method deletion operator
    String targetFolderIOD = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\IOD";

    // destination folder for storing mutation code for java static modifier insertion operator
    String targetFolderJSI = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\JSI";

    // destination folder for storing mutation code for static modifier deletion operator
    String targetFolderJSD = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\JSD";

    // destination folder for storing mutation code for parent member declaration operator
    String targetFolderPMD = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\PMD";

    // destination folder for storing mutation code for overriding method deletion operator
    String targetFolderOMD = PropertiesUtils.getProperties().getProperty("mutationClassPath") + "\\OMD";

    /**
     * Performs mutation operation on bytecode
     * @throws NotFoundException
     * @throws CannotCompileException
     * @throws IOException
     */
    void mutate() throws NotFoundException, CannotCompileException, IOException;
}
