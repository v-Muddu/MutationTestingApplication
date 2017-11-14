package com.oole.hw3.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

/**
 * CSV Utils
 * For operations on CSV files
 */
public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ',';

    public static final String csvPath = PropertiesUtils.getProperties().getProperty("csvFilePath");

    public static FileWriter writer;

    static {
        try {
            writer = new FileWriter(CSVUtils.csvPath);

            writeLine(writer, Arrays.asList("Operator Applied", "Class Name", "Mutation Applied", "Mutation Detected"));

        } catch (IOException e) {
            e.printStackTrace();
        }
   }

    /**
     * writes the input list items as comma separated values into the csv file
     * @param w
     * @param values
     * @throws IOException
     */
    public static void writeLine(Writer w, List<String> values) throws IOException {
        writeLine(w, values, DEFAULT_SEPARATOR, ' ');
    }

    public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
        writeLine(w, values, separators, ' ');
    }

    //https://tools.ietf.org/html/rfc4180
    private static String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

        boolean first = true;

        //default customQuote is empty

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());


    }

    public static void shutdown(){
        try {
            writer.flush();
            //writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}