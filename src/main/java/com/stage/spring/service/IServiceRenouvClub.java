package com.stage.spring.service;

import com.stage.spring.entity.Club;
import com.stage.spring.entity.File;
import com.stage.spring.entity.RenouvellementClub;
import com.stage.spring.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IServiceRenouvClub {

    void addClub2(RenouvellementClub f, MultipartFile file) throws IOException;

    void addClub(RenouvellementClub f, Long idu, MultipartFile file) throws IOException;
    //public String uploadFilesAndAffectToClub(Long idF,MultipartFile multipartFiles)
    // throws IOException ;
    public List<String> uploadFilesAndAffectToClub(Long idF, List<MultipartFile> multipartFiles)
            throws IOException ;
    List<RenouvellementClub> getClubs();
    List<RenouvellementClub> getUndeletedClub();
    void deleteClub(Long id);
    RenouvellementClub updateClub(RenouvellementClub f,MultipartFile file,
                    List<MultipartFile>  files2,
                    List<MultipartFile> multipartFiles);
    RenouvellementClub participerClub(Long idf,Long idu);
    Boolean annulerParticipation(Long idf,Long idu);
    public RenouvellementClub retrieveClub(Long id);
    public List<User> retrieveClubParticipants(Long id);

    public List<File> getOtherFiles(Long idF);
    public void deleteFile(Long idFile);
}
