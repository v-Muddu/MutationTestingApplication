package com.oole.hw3;


import com.oole.hw3.utility.CSVUtils;
import com.oole.hw3.utility.PropertiesUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVUtilsTest {


    @Test
    public void testWriteLine(){
        List<String> csvLine = new ArrayList<>();
        csvLine.add("Access Modifier Test");
        csvLine.add("org.apache.commons.lang3.AnnotationUtils");
        csvLine.add("NO");
        csvLine.add("--");

        try {
            CSVUtils.writeLine(CSVUtils.writer, csvLine);
            CSVUtils.writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String csvFile = PropertiesUtils.getProperties().getProperty("csvFilePath");
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            if ((line = br.readLine()) != null && (line = br.readLine()) != null) {

                // use comma as separator
                String[] items = line.split(cvsSplitBy);

                Assert.assertEquals(items[0],"Access Modifier Test");
                Assert.assertEquals(items[1],"org.apache.commons.lang3.AnnotationUtils");
                Assert.assertEquals(items[2],"NO");
                Assert.assertEquals(items[3],"--");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
