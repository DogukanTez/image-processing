package com.dogukantez.image_processing.controller;

import com.dogukantez.image_processing.service.ImageProcessingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {
    private final ImageProcessingService imageProcessingService;

    public ImageController(ImageProcessingService imageProcessingService) {
        this.imageProcessingService = imageProcessingService;
    }

    @PostMapping(value="/filter/invert",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> invertFilter(@RequestParam("image") MultipartFile imageFile) throws IOException{
        byte[] invertedImage = imageProcessingService.invertImage(imageFile);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(invertedImage);
    }

    @PostMapping(value ="/filter/solarize",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> solarizeFilter(@RequestParam("image") MultipartFile imageFile) throws IOException{
        byte[] image = imageProcessingService.solarizeImage(imageFile);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }

    @PostMapping(value ="/filter/clip",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> clipFilter(@RequestParam("image") MultipartFile imageFile) throws IOException{
        byte[] image = imageProcessingService.clipImage(imageFile);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }

    @PostMapping(value ="/filter/custom-filter",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> customFilter(@RequestParam("image") MultipartFile imageFile) throws IOException{
        byte[] image = imageProcessingService.applyCustomFilter(imageFile);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }

}
