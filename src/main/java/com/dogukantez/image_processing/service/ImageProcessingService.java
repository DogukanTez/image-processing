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

}
