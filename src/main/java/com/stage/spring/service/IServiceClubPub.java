package com.stage.spring.service;

import com.stage.spring.entity.ClubPublication;
import com.stage.spring.entity.File;
import com.stage.spring.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IServiceClubPub {

    void addClub(ClubPublication f, Long idu, MultipartFile file) throws IOException;

    List<ClubPublication> getClubs();
    List<ClubPublication> getUndeletedClub();
    void deleteClub(Long id);
    ClubPublication updateClub(ClubPublication f,MultipartFile file,
                    List<MultipartFile>  files2,
                    List<MultipartFile> multipartFiles);
    ClubPublication participerClub(Long idf,Long idu);
    Boolean annulerParticipation(Long idf,Long idu);
    public ClubPublication retrieveClub(Long id);
    public List<User> retrieveClubParticipants(Long id);

    public List<File> getOtherFiles(Long idF);
}
