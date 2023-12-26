package com.demirel;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class FolderLastModified {

    static final String HASHCODE_FILE_NAME= "hashcode.serkan";

    public static void main(String[] args) {
        String folderPath = ConfigReader.getFolderPath();

        Path directory = Paths.get(folderPath + "/all");
        Date latestFileUpdateDate=null;
        Date latestScanDate=null;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path file : stream) {
                BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);

                if (attrs.isRegularFile()) {
                    Date lastModified = new Date(attrs.lastModifiedTime().toMillis());

                    if(file.endsWith(HASHCODE_FILE_NAME)){
                        latestScanDate=lastModified;
                    }

                    if(latestFileUpdateDate == null || latestFileUpdateDate.before(lastModified)){
                        latestFileUpdateDate=lastModified;
                    }
                    System.out.println("File: " + file.toString() + " Last Modified: " + lastModified);
                }
            }
            System.out.println(latestFileUpdateDate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
