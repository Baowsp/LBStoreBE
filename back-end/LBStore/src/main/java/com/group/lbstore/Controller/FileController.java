package com.group.lbstore.Controller;

import com.group.lbstore.Service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Sử dụng FileStorageService có sẵn để lưu file
            String fileUrl = fileStorageService.storeFile(file);
            System.out.println("File URL: " + fileUrl);
            // Trả về URL của file để Frontend lưu vào Database
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Could not upload the file: " + e.getMessage());
        }
    }
}
