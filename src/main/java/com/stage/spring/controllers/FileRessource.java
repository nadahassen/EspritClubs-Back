package com.stage.spring.controllers;
import com.stage.spring.entity.File;
import com.stage.spring.service.IServiceUploadFiles;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class FileRessource {

    IServiceUploadFiles serviceUploadFiles;

    public static final String DIRECTORY = "C:\\xampp\\htdocs\\FileUploads";

    // Define a method to upload files
    @PostMapping("/upload")
    public List<String> uploadFiles(@RequestParam("files")List<MultipartFile> multipartFiles) throws IOException {


        return  serviceUploadFiles.uploadFiles(multipartFiles);
    }

    // Define a method to download files
    @GetMapping("download/{filename}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {

        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
        if(!Files.exists(filePath)) {
            throw new FileNotFoundException(filename + " was not found on the server");
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", filename);
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        return  ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);

    }

    @GetMapping("/list-files")
    public List<File> getFiles(){
        return serviceUploadFiles.getAllFiles();
    }

    @DeleteMapping("/delete-file/{idf}")
    public void deleteFile(@PathVariable("idf") Long idf) {
        serviceUploadFiles.deleteFile(idf);
    }

}
