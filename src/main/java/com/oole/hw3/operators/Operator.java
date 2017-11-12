package com.oole.hw3.operators;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.io.IOException;

public interface Operator extends Runnable {
    String targetFolderEncapsulation = "mutatedFiles/AMC";
    String targetFolderIHD = "D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\IHD";
    String targetFolderIOD = "D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\IOD";
    String targetFolderJSI = "D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\JSI";
    String targetFolderJSD = "D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\JSD";
    String targetFolderPMD = "D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\PMD";
    String targetFolderOMD = "D:\\git\\vishwanath_muddu_adarsh_hegde_rohit_vibhu__hw3\\mutatedFiles\\OMD";
    void mutate() throws NotFoundException, CannotCompileException, IOException;
}
