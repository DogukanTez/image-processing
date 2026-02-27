package com.dogukantez.image_processing.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageProcessingService {

    public byte[] invertImage(final MultipartFile imageFile) throws IOException{
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());

        BufferedImage invertedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );


        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                int rgb = originalImage.getRGB(x, y);
                invertedImage.setRGB(x, y, ~rgb); // Bitwise NOT operation inverts the colors
            }
        }


        ByteArrayOutputStream imagebaos = new ByteArrayOutputStream();
        ImageIO.write(invertedImage,"png",imagebaos);
        return imagebaos.toByteArray();
    }

    public byte[] solarizeImage(final MultipartFile imageFile) throws IOException{
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());

        BufferedImage solarizedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );


        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                Color color = new Color(originalImage.getRGB(x,y));

                int red = solarizeComponent(color.getRed());
                int green= solarizeComponent(color.getGreen());
                int blue= solarizeComponent(color.getBlue());

                Color newColor = new Color(red,green,blue);
                solarizedImage.setRGB(x,y,newColor.getRGB());
            }
        }

        ByteArrayOutputStream imagebaos = new ByteArrayOutputStream();
        ImageIO.write(solarizedImage,"png",imagebaos);
        return imagebaos.toByteArray();

    }

    private int solarizeComponent(int component){
        return component < 128 ? 255 - component : component;
    }


    public byte[] clipImage(final MultipartFile imageFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());

        BufferedImage clippedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                Color color = new Color(originalImage.getRGB(x,y));

                int red = clipComponent(color.getRed());
                int green= clipComponent(color.getGreen());
                int blue= clipComponent(color.getBlue());

                Color newColor = new Color(red,green,blue);
                clippedImage.setRGB(x,y,newColor.getRGB());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(clippedImage,"png", baos);
        return baos.toByteArray();
    }

    private int clipComponent(int component){
        return Math.max(50,component);
    }




    public byte[] applyCustomFilter(final MultipartFile imageFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());

        BufferedImage filteredImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                Color color = new Color(originalImage.getRGB(x,y));

                int red = customFilterComponent(color.getRed());
                int green= customFilterComponent(color.getGreen());
                int blue= customFilterComponent(color.getBlue());

                Color newColor = new Color(red,green,blue);
                filteredImage.setRGB(x,y,newColor.getRGB());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(filteredImage,"png", baos);
        return baos.toByteArray();
    }

    private int customFilterComponent(int component){
        if(component < 60){
            return Math.min(255,component+50);
        }else if (component > 200){
            return Math.max(0,component-30);
        }
        return component;
    }


    public byte[] brightenImage(final MultipartFile imageFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());

        BufferedImage filteredImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                Color color = new Color(originalImage.getRGB(x,y));

                int red = Math.min(255, color.getRed() + 20);
                int green = Math.min (255, color.getGreen() + 20);
                int blue = Math.min(255, color.getBlue() + 20);

                Color newColor = new Color(red,green,blue);
                filteredImage.setRGB(x,y,newColor.getRGB());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(filteredImage,"png", baos);
        return baos.toByteArray();
    }

    public byte[] blackAndWhiteImage (MultipartFile imageFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());
        BufferedImage bwImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        // First pass: brighten the image
        BufferedImage brightenedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        // Step 1: Brighten
        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                Color color = new Color(originalImage.getRGB(x, y));
                int red = Math.min(255, color.getRed() + 20);
                int green = Math.min(255, color.getGreen() + 20);
                int blue = Math.min(255, color.getBlue() + 20);
                Color newColor = new Color(red, green, blue);
                brightenedImage.setRGB(x, y, newColor.getRGB());
            }
        }

        // Step 2: Convert to pure black and white
        for (int x = 0; x < brightenedImage.getWidth(); x++) {
            for (int y = 0; y < brightenedImage.getHeight(); y++) {
                Color color = new Color(brightenedImage.getRGB(x, y));
                // Calculate brightness (using perceived brightness formula)
                int brightness = (int) (0.299 * color.getRed() +
                        0.587 * color.getGreen() + 0.114 * color.getBlue());

                // If brightness is above threshold, make it white, otherwise black
                Color newColor = brightness > 128 ? Color.WHITE : Color.BLACK;
                bwImage.setRGB(x, y, newColor.getRGB());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bwImage, "png", baos);
        return baos.toByteArray();
    }
}
