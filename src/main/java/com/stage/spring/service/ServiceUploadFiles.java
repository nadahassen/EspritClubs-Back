package com.stage.spring.service;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.stage.spring.entity.File;
import com.stage.spring.entity.FileType;
import com.stage.spring.repository.FileRepository;
import com.stage.spring.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.util.EnumUtils;

import com.google.common.base.Objects;



@Service
@AllArgsConstructor
public class ServiceUploadFiles implements IServiceUploadFiles{


    IServiceImage imageService;

    ImageRepository imageRepository;

    FileRepository fileRepository;

    // define a location

    public static final String DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/";

    @Override
    public List<String> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> filenames = new ArrayList<>();

        for(MultipartFile file : multipartFiles) {
            File file1 = new File(file.getOriginalFilename());
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            file1.setType(FileType.SimpleFile);
            imageService.save(file);
            fileRepository.save(file1);
            filenames.add(filename);

        }
        return filenames;

    }

    @Override
    public ResponseEntity<Resource> downloadFiles(String filename) throws IOException {
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
        if(!Files.exists(filePath)) {
            throw new FileNotFoundException(filename + " was not found on the server");
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", filename);
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + filename);
        return  ResponseEntity.ok().contentType(MediaType.parseMediaType(filename))
                .headers(httpHeaders).body(resource);

    }
    enum Types{A1, A2, B1, B2};

    @Override
    public List<File> getAllFiles() {
        String SimpleFile="SimpleFile";
        List<File> files = new ArrayList<>();
        for (File f : fileRepository.findAll()){

            if( f.getType().toString()==SimpleFile){
                files.add(f);
            }
        }
        return files;
    }

    @Override
    public void deleteFile(Long idFile) {

        fileRepository.deleteById(idFile);
    }

}
