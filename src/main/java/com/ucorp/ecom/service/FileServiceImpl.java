package com.ucorp.ecom.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //File name of current file or orginal file
        String originalFilename = file.getOriginalFilename();
        //generate a unique file name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filePath = path + File.separatorChar + fileName;

        //check if path exist and create
        File folder =new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        //upload to server
        Files.copy(file.getInputStream(), Path.of(filePath));
        //return file name
        return fileName;
    }
}
