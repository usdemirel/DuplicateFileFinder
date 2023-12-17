package com.demirel;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

/**
 * Not working well
 */
public class FolderDuplicateFileFinder {

    public static void main(String[] args) {
        String folderPath = ConfigReader.getFolderPath();
        findFolderDuplicateFiles(folderPath);
    }

    public static void findFolderDuplicateFiles(String folderPath) {
        Map<String, Set<String>> folderDuplicates = new HashMap<>();

        try {
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        File file = filePath.toFile();
                        String extension = getFileExtension(file.getName());
                        if (extension != null) {
                            String hash;
                            try {
                                hash = getFileChecksum(file);
                                String key = extension + "-" + hash;

                                if (!folderDuplicates.containsKey(key)) {
                                    folderDuplicates.put(key, new HashSet<>());
                                }
                                Set<String> folders = folderDuplicates.get(key);
                                folders.add(file.getParent());
                            } catch (IOException | NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Integer> folderDuplicateCounts = new HashMap<>();

        for (Set<String> folders : folderDuplicates.values()) {
            for (String folder : folders) {
                folderDuplicateCounts.put(folder, folderDuplicateCounts.getOrDefault(folder, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : folderDuplicateCounts.entrySet()) {
            String folder = entry.getKey();
            int duplicatesCount = entry.getValue() - 1; // Excluding the current folder itself
            System.out.println("Folder: " + folder + " has " + duplicatesCount + " files duplicated in other folders.");
        }
    }

    private static String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1) {
            return null;
        }
        return fileName.substring(lastIndex + 1);
    }

    private static String getFileChecksum(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (InputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            byte[] byteArray = new byte[1024];
            int bytesCount;

            while ((bytesCount = bis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }

        byte[] bytes = digest.digest();
        StringBuilder hash = new StringBuilder();

        for (byte aByte : bytes) {
            hash.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        return hash.toString();
    }
}
