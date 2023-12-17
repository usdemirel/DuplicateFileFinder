package com.demirel;

import java.io.*;

public class TempFolderCreator {

    public static void main(String[] args) {
        String folderPath = ConfigReader.getFolderPath();
        createTempFolder(folderPath);
    }

    public static void createTempFolder(String folderPath) {
        File tempFolder = new File(folderPath +"/temp/a/b/c/d");

        if (!tempFolder.exists()) {
            boolean created = tempFolder.mkdir();
            if (created) {
                System.out.println("Temporary folder created successfully.");
            } else {
                System.out.println("Failed to create temporary folder.");
            }
        } else {
            System.out.println("Temporary folder already exists.");
        }
    }
}
