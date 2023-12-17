package com.demirel;


import java.io.*;
        import java.nio.file.*;
        import java.security.*;
        import java.util.*;

public class ConfigReader {

    public static String getFolderPath() {
        Properties properties = loadProperties();
        String folderPath = properties.getProperty("folderPath");
        if (folderPath != null) {
            return folderPath;
        } else {
            System.out.println("Folder path not specified in the properties file.");
            return null;
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("/Users/serkandemirel/Documents/testSPr/DuplicateFinderProj/src/main/java/com/demirel/config.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }
}
