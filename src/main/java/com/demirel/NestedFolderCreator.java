package com.demirel;

import java.io.*;

public class NestedFolderCreator {

    public static void main(String[] args) {
        String rootFolder = ConfigReader.getFolderPath();

        // Create nested folders
        createNestedFolders(rootFolder, args);
    }

    public static void createNestedFolders(String rootFolder, String... folders) {
        File baseDirectory = new File(rootFolder);

        if (!baseDirectory.exists()) {
            if (baseDirectory.mkdirs()) {
                System.out.println("Root folder created: " + baseDirectory.getAbsolutePath());
            } else {
                System.out.println("Failed to create root folder.");
                return;
            }
        } else {
            System.out.println("Root folder already exists: " + baseDirectory.getAbsolutePath());
        }

        File dynamicBaseDirectory = new File(rootFolder);
        for (String folder : folders) {
            File subFolder = new File(dynamicBaseDirectory, folder);

            if (!subFolder.exists()) {
                if (subFolder.mkdirs()) {
                    System.out.println("Subfolder created: " + subFolder.getAbsolutePath());
                } else {
                    System.out.println("Failed to create subfolder.");
                }
            } else {
                System.out.println("Subfolder already exists: " + subFolder.getAbsolutePath());
            }
            dynamicBaseDirectory = new File(dynamicBaseDirectory + "/" + folder);
        }
    }
}
