package com.stage.spring.service;

import com.stage.spring.entity.Club;
import java.io.IOException;
import java.util.List;

import com.stage.spring.entity.User;
import org.springframework.web.multipart.MultipartFile;

import com.stage.spring.entity.File;
public interface IServiceClub {

    void addClub(Club f, Long idu, MultipartFile file) throws IOException;
    public List<String> uploadFilesAndAffectToClub(Long idF,List<MultipartFile> multipartFiles)
            throws IOException ;
    List<Club> getClubs();
    List<Club> getUndeletedClub();
    void deleteClub(Long id);
    Club updateClub(Club f,MultipartFile file,
                              List<MultipartFile>  files2,
                              List<MultipartFile> multipartFiles);
    Club participerClub(Long idf,Long idu);
    Boolean annulerParticipation(Long idf,Long idu);
    public Club retrieveClub(Long id);
    public List<User> retrieveClubParticipants(Long id);

    public List<File> getOtherFiles(Long idF);
    public void deleteFile(Long idFile);


}
