package com.demirel;

import java.io.*;
import java.util.*;

public class SubfoldersWithFiles {

    public static void main(String[] args) {
        String folderPath = ConfigReader.getFolderPath();
        List<String> subfoldersWithFiles = getSubfoldersWithFiles(folderPath);

        Collections.sort(subfoldersWithFiles);

        writeToFile(folderPath + "/folder_tree.serkan", subfoldersWithFiles);
    }

    public static List<String> getSubfoldersWithFiles(String folderPath) {
        List<String> subfoldersWithFiles = new ArrayList<>();
        exploreFolder(new File(folderPath), subfoldersWithFiles);
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
