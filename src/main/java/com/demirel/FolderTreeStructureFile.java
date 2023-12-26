package com.demirel;

import java.io.*;
import java.util.*;

public class FolderTreeStructureFile {

    static final String FILE_NAME = "/folder_tree.serkan";
    static final String TEMP_FOLDER = "/temp_serkan";

    public static void main(String[] args) {
        String folderPath = ConfigReader.getFolderPath();
        createRootFolder(folderPath + TEMP_FOLDER);

        List<String> subfoldersWithFiles = getSubfoldersWithFiles(folderPath);
        writeToFile(folderPath + TEMP_FOLDER + FILE_NAME, subfoldersWithFiles);
    }

    public static List<String> getSubfoldersWithFiles(String folderPath) {
        List<String> subfoldersWithFiles = new ArrayList<>();
        exploreFolder(new File(folderPath), subfoldersWithFiles);
        Collections.sort(subfoldersWithFiles);
        return subfoldersWithFiles;
    }

    private static void exploreFolder(File folder, List<String> subfoldersWithFiles) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                boolean containsFiles = false;

                for (File file : files) {
                    if (file.isFile()) {
                        containsFiles = true;
                        break;
                    }
                }

                if (containsFiles) {
                    subfoldersWithFiles.add(folder.getAbsolutePath());
                }

                for (File subfolder : files) {
                    if (subfolder.isDirectory()) {
                        exploreFolder(subfolder, subfoldersWithFiles);
                    }
                }
            }
        }
    }

    public static void createRootFolder(String rootFolder) {
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
    }

    private static void writeToFile(String filePath, List<String> subfoldersWithFiles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String subfolder : subfoldersWithFiles) {
                writer.write(subfolder);
                writer.newLine();
            }
            System.out.println("Data written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
