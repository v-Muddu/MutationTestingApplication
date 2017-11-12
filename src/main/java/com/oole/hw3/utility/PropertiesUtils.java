package com.oole.hw3.utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    static Properties prop = new Properties();
    static InputStream input = null;

    static{
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Properties getProperties(){
        return prop;
    }
}
