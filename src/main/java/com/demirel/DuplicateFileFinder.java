package com.demirel;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

/**
 * Initial work. Ignore
 */
public class DuplicateFileFinder {

    public static void main(String[] args) {
        String folderPath = ConfigReader.getFolderPath();
        findDuplicateFiles(folderPath);
    }

    public static void findDuplicateFiles(String folderPath) {
        Map<String, List<File>> filesMap = new HashMap<>();

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
                                if (!filesMap.containsKey(key)) {
                                    filesMap.put(key, new ArrayList<>());
                                }
                                filesMap.get(key).add(file);
                            } catch (IOException | NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (List<File> fileList : filesMap.values()) {
            if (fileList.size() > 1) {
                System.out.println("Duplicates:");
                for (File file : fileList) {
                    System.out.println(file.getAbsolutePath());
                }
                System.out.println("---------------");
            }
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
