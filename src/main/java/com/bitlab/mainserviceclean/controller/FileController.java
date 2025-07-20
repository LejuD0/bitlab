package com.bitlab.mainserviceclean.controller;

import com.bitlab.mainserviceclean.service.FileStorageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String filename = fileStorageService.uploadFile(file);
        return ResponseEntity.ok(filename);
    }

    @GetMapping("/download/{filename}")
    public void downloadFile(@PathVariable String filename, HttpServletResponse response) {
        try (InputStream in = fileStorageService.downloadFile(filename);
             OutputStream out = response.getOutputStream()) {

            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setContentType("application/octet-stream");

            in.transferTo(out);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при скачивании файла", e);
        }
    }

    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        fileStorageService.deleteFile(filename);
        return ResponseEntity.ok("Файл удален: " + filename);
    }
}

