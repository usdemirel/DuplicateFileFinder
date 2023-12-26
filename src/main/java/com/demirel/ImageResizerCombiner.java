package com.demirel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResizerCombiner {

    public static void main(String[] args) {
        String image1Path = "/Users/serkandemirel/Documents/java-test-folder/png/1654321216526-3-1024x1024.png"; // Replace with path to the first image
        String image2Path = "/Users/serkandemirel/Documents/java-test-folder/png/cat_PNG50438.png"; // Replace with path to the second image
        String outputFolderPath = "/Users/serkandemirel/Documents/java-test-folder/png/output"; // Replace with the output folder path

        int width1 = 400; // Width of the first image after resizing
        int height1 = 400; // Height of the first image after resizing

        int width2 = 400; // Width of the second image after resizing
        int height2 = 600; // Height of the second image after resizing

        try {
            BufferedImage image1 = resizeImage(ImageIO.read(new File(image1Path)), width1, height1);
            BufferedImage image2 = resizeImage(ImageIO.read(new File(image2Path)), width2, height2);

            int combinedWidth = width1 + width2;
            int combinedHeight = Math.max(height1, height2);

            BufferedImage combinedImage = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = combinedImage.createGraphics();
            g.drawImage(image1, 0, 0, null);
            g.drawImage(image2, width1, 0, null);
            g.dispose();

            File outputFolder = new File(outputFolderPath);
            outputFolder.mkdirs();

            File outputFile = new File(outputFolder, "combined_image.png");
            ImageIO.write(combinedImage, "png", outputFile);

            System.out.println("Images combined and saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
}
