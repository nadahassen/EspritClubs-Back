package com.stage.spring.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stage.spring.entity.*;
import com.stage.spring.repository.ClubPubRepository;
import com.stage.spring.repository.ClubRepository;
import com.stage.spring.repository.FileRepository;
import com.stage.spring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@Slf4j
public class ServiceClubPub implements IServiceClubPub {

    @Autowired
    ClubPubRepository clubRepository;
    @Autowired

    UserRepository userRepository;
    @Autowired

    IServiceImage imageService;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date date;
    @Autowired
    FileRepository fileRepository;

    // add

    @Transactional
    public void addClub(ClubPublication f, Long idu, MultipartFile file) throws IOException {

        List<String> filenames = new ArrayList<>();

        Date date = new Date(System.currentTimeMillis());
        f.setCreatedAt(date);
        f.setOrganizer(userRepository.findById(idu).orElse(null));
        //Image
        Image image = new Image(file.getOriginalFilename());
        String filename1 = StringUtils.cleanPath(file.getOriginalFilename());
        filenames.add(filename1);
        f.setImage(image);
        imageService.save(file);
        clubRepository.save(f);

    }


    @Transactional




    //get clubs
    public List<ClubPublication> getClubs() {

        return clubRepository.findAll();
    }

    // get undelete clubs
    public List<ClubPublication> getUndeletedClub() {

        return clubRepository.getUndeletedClubs();
    }

    // delete club
    public void deleteClub(Long id) {
        Date date = new Date(System.currentTimeMillis());
        ClubPublication deletedClub = clubRepository.findById(id).orElse(null);
        deletedClub.setDeleted(true);
        deletedClub.setDeletedAt(date);
        clubRepository.save(deletedClub);

    }


    public ClubPublication updateClub(ClubPublication f, MultipartFile file,
                           List<MultipartFile> files2,
                           List<MultipartFile> multipartFiles) {
        User user = userRepository.findById(f.getOrganizer().getIdUser()).orElse(null);
        ClubPublication club = clubRepository.findById(f.getIdClub()).orElse(null);
        f.setOrganizer(user);
        ;
        f.setCreatedAt(club.getCreatedAt());

        Date date = new Date(System.currentTimeMillis());
        f.setModifiedAt(date);
        if (f.getImage() == null) {
            f.setImage(club.getImage());
        }
        //Image
        if (file != null) {
            Image image = new Image(file.getOriginalFilename());
            f.setImage(image);
            imageService.save(file);
        } else {
            f.setImage(club.getImage());
        }
        if (files2 != null) {
            for (MultipartFile fi : files2) {
                File video = new File(fi.getOriginalFilename());
                imageService.save(fi);
                fileRepository.save(video);

            }
        }

        if (multipartFiles != null) {
            for (MultipartFile fi : multipartFiles) {
                File fil = new File(fi.getOriginalFilename());
                //f.getOtherFiles().add(fil);
                imageService.save(fi);

                fileRepository.save(fil);
            }
        }
        //EndImage


        return clubRepository.save(f);
    }

    // participe club
    public ClubPublication participerClub(Long idf, Long idu) {
        User u = userRepository.findById(idu).orElse(null);
        ClubPublication f = clubRepository.findById(idf).orElse(null);


        //if(((f.getMaxParticipants()) >= (f.getNbParticipants() + 1))&&
        //	!(f.getParticipants().contains(u)))
        //	{
        f.getMembers().add(u);
        f.setNbParticipants(f.getNbParticipants() + 1);
        return clubRepository.save(f);


    }

    //annuler participe
    public Boolean annulerParticipation(Long idf, Long idu) {
        User u = userRepository.findById(idu).orElse(null);
        ClubPublication f = clubRepository.findById(idf).orElse(null);


        if ((f.getMembers().contains(u))) {
            f.getMembers().remove(u);
            f.setNbParticipants(f.getNbParticipants() - 1);
            clubRepository.save(f);
            return true;
        }
        return false;

    }

    // retrieve club
    public ClubPublication retrieveClub(Long id) {
        return clubRepository.findById(id).orElse(null);
    }

    // retrieve club participants
    public List<User> retrieveClubParticipants(Long id) {
        ClubPublication f = clubRepository.findById(id).orElse(null);
        return (List<User>) f.getMembers();
        // return club != null ? club.getMembers() : Collections.emptySet();
    }


    //get other files
    public List<File> getOtherFiles(Long idF) {
        //ClubPublication f = clubRepository.findById(idF).orElse(null);
       // return f.getOtherFiles();
        return fileRepository.findAll(); //ghalta
    }

    //delete file
    public void deleteFile(Long idFile) {

        fileRepository.deleteById(idFile);
    }
    @Transactional
    public void addClub2(Club f, MultipartFile file) throws IOException {

    }
}