/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author why
 */
public class NewClass {

    //指定文件路径及文件名
    //private static  String fileName = "c:/aa.ini";
    private static Properties props = new Properties();
    private static InputStream inputFile;
    private static FileOutputStream outputFile;

    public static String readKey(String key) {
        String fileName = "./newproperties.properties";
        //System.out.println(fileName);
        String value = "";
        try {
            inputFile = new BufferedInputStream(new FileInputStream(fileName));
            props.load(inputFile);
            inputFile.close();
            value = props.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void setKey(String key, String value) {
        props.setProperty(key, value);
    }

    public static void saveKey(String comment) {
        String fileName = "./newproperties.properties";

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            outputFile = new FileOutputStream(fileName);
            props.store(outputFile, comment);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputFile.close();
            } catch (IOException e) {
            }
        }
    }
}