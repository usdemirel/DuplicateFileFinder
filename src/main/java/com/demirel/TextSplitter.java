package com.demirel;

/**
 * Reads all .serkan files and puts them in a map
 *
 */
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class TextSplitter {

    private static Map<String, String> resultMap = new HashMap<>();
    private static Map<String,Map<String, String>> folderResultMap = new HashMap<>();
    private static Map<String, Double> percentageResultMap = new HashMap<>();

    public static void main(String[] args) {
        String rootFolder = ConfigReader.getFolderPath() + "/temp_serkan";
        readTextFiles(rootFolder);


//        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//        }

        for (String folderKey : folderResultMap.keySet()) {
            for (Map.Entry<String, String> entry : folderResultMap.get(folderKey).entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
                entry.setValue(entry.getValue() + "," + resultMap.get(entry.getKey()));
            }
        }
        DecimalFormat df = new DecimalFormat("#.#");
        for (String folderKey : folderResultMap.keySet()) {
            percentageResultMap.put(folderKey, 0d);
            for (Map.Entry<String, String> entry : folderResultMap.get(folderKey).entrySet()) {
                if(entry.getValue().split(",").length > 2){
                    percentageResultMap.put(folderKey, percentageResultMap.get(folderKey)+1);
                }
            }
            percentageResultMap.put(folderKey, 100*percentageResultMap.get(folderKey)/folderResultMap.get(folderKey).size());
        }
        System.out.println("__________________________");

                for (Map.Entry<String, Double> entry : percentageResultMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + df.format(entry.getValue()) + "%");
        }

    }

    public static void readTextFiles(String folderPath) {
        File rootDir = new File(folderPath);

        File[] files = rootDir.listFiles((dir, name) -> name.toLowerCase().endsWith("hashcode.txt"));

        if (files != null) {
            for (File file : files) {
                processFile(file);
            }
        }

        File[] directories = rootDir.listFiles(File::isDirectory);

        if (directories != null) {
            for (File directory : directories) {
                readTextFiles(directory.getAbsolutePath());
            }
        }
    }

    public static void processFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String key = parts[1].trim();
                    String folderPath = file.getParentFile().getAbsolutePath();

                    if (!folderResultMap.containsKey(folderPath)) {
                        folderResultMap.put(folderPath, new HashMap<>());
                    }
                    String val="";
                    if (folderResultMap.get(folderPath).containsKey(key)) {
                        val = folderResultMap.get(folderPath).get(key) + "," + folderPath;
                        folderResultMap.get(folderPath).put(key, val);
                    }else{
                        folderResultMap.get(folderPath).put(key, parts[0].trim());
                    }


                    if (resultMap.containsKey(key)) {
                        folderPath = resultMap.get(key) + "," + folderPath;
                    }
                    resultMap.put(key, folderPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void processFile(File file) {
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length >= 2) {
//                    String key = parts[1].trim();
//                    String folderPath = file.getParentFile().getAbsolutePath();
//
//                    if (resultMap.containsKey(key)) {
//                        folderPath = resultMap.get(key) + "," + folderPath;
//                    }
//                    resultMap.put(key, folderPath);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}

