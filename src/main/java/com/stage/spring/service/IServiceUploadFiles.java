package com.stage.spring.service;
import java.io.IOException;
import java.util.List;

import com.stage.spring.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
public interface IServiceUploadFiles {
    public List<String> uploadFiles(List<MultipartFile> multipartFiles) throws IOException;
    public ResponseEntity<Resource> downloadFiles(String filename) throws IOException;
    public List<File> getAllFiles();
    void deleteFile(Long idFile);

}
