package com.demirel;

import java.io.*;
import java.security.*;
import java.util.*;

public class FileHashcodeWriter {

    static final String TEMP_FILE = "temp_serkan";
    static final String FOLDER_TREE_FILE = "/folder_tree.serkan";

    public static void main(String[] args) {
        String parentDir = ConfigReader.getFolderPath();
        FolderTreeStructureFile.main(new String[]{});
        String filePaths = parentDir + "/" + TEMP_FILE + FOLDER_TREE_FILE;
        readFolderPathsAndCreateHashcodeFiles(filePaths,parentDir);
    }

    public static void readFolderPathsAndCreateHashcodeFiles(String filePaths, String parentDir) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePaths))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains(TEMP_FILE)){
                    continue;
                }
                createHashcodeFile(line,parentDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createHashcodeFile(String folderPath, String parentDir) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                Map<String, String> fileHashcodes = new HashMap<>();
                for (File file : files) {
                    if (file.isFile()) {
                        try {
                            String hash = getFileChecksum(file);
                            String extension = getFileExtension(file.getName());
                            if (extension != null) {
                                String fileDetails = extension + "-" + hash;
                                fileHashcodes.put(file.getName(), fileDetails);
                            }
                        } catch (IOException | NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                }
                writeHashcodeFile(folder.getAbsolutePath(), parentDir, fileHashcodes);
            }
        }
    }

    private static void writeHashcodeFile(String folderPath, String parentDir, Map<String, String> fileHashcodes) {
        String tempHashcodeFilePath = folderPath.replace(parentDir, parentDir + "/temp_serkan");
        String tempHashcodeFilePath2 = folderPath.replace(parentDir, "temp_serkan");
        String[] subfolders = tempHashcodeFilePath2.split("/");
        NestedFolderCreator.main(subfolders);
        String hashcodeFilePath = tempHashcodeFilePath + "/hashcode.serkan";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(hashcodeFilePath))) {
            for (Map.Entry<String, String> entry : fileHashcodes.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
            System.out.println("Hashcode file created for folder: " + folderPath);
        } catch (IOException e) {
            e.printStackTrace();
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

