package com.site.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploaded_solutions";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("taskNumber") String taskNumber,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("school") String school,
            @RequestParam("grade") String grade
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
         
            String folderName = String.format("%s/%s_class/%s_%s",
                    UPLOAD_DIR, grade, lastName, firstName);

            File directory = new File(folderName);
            if (!directory.exists()) {
                directory.mkdirs(); 
            }

            
            String originalName = file.getOriginalFilename();
            String fileName = String.format("Task_%s_%s", taskNumber, originalName);

            // 3. Сохраняем файл
            Path path = Paths.get(folderName + File.separator + fileName);
            Files.write(path, file.getBytes());

            System.out.println("Saved file: " + path.toString());
            return ResponseEntity.ok("File uploaded successfully");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error saving file");
        }
    }
}
