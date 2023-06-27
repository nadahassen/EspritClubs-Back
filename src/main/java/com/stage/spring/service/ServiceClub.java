package com.stage.spring.service;

import java.io.IOException;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stage.spring.entity.File;
import com.stage.spring.entity.FileType;
import com.stage.spring.entity.Club;
import com.stage.spring.entity.Image;
import com.stage.spring.entity.User;
import com.stage.spring.repository.FileRepository;
import com.stage.spring.repository.ClubRepository;
import com.stage.spring.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceClub implements IServiceClub {

    @Autowired
    ClubRepository clubRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IServiceImage imageService;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date date;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    IServiceUploadFiles uploadFilesService;

    // add

    @Transactional
    public void addClub(Club f, Long idu, MultipartFile file) throws IOException {

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
    //add club without user

    @Transactional
    public void addClub2(Club f, MultipartFile file) throws IOException {
        List<String> filenames = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        f.setCreatedAt(date);
        // Image
        Image image = new Image(file.getOriginalFilename());
        String filename1 = StringUtils.cleanPath(file.getOriginalFilename());
        filenames.add(filename1);
        f.setImage(image);
        imageService.save(file);
        clubRepository.save(f);
    }




    //uploadFiles And Affect To Club

    @Transactional

    public List<String> uploadFilesAndAffectToClub(Long idF,List<MultipartFile> multipartFiles) throws IOException {

        List<String> filenames = new ArrayList<>();

        Club f = clubRepository.findById(idF).orElse(null);

        for(MultipartFile file : multipartFiles) {

            File file1 = new File(file.getOriginalFilename());
            file1.setType(FileType.FormationFile);
            f.getOtherFiles().add(file1);

            fileRepository.save(file1);
            String filename = StringUtils.cleanPath(file.getOriginalFilename());

            imageService.save(file);


            filenames.add(filename);

        }

        return filenames;

    }
    /*@Transactional

    public String uploadFilesAndAffectToClub(Long idF, MultipartFile multipartFiles) throws IOException {

        List<String> filenames = new ArrayList<>();

        Club f = clubRepository.findById(idF).orElse(null);


        File file = new File(multipartFiles.getOriginalFilename());
        file.setType(FileType.FormationFile);
        f.getOtherFiles().add(file);

        fileRepository.save(file);
        String filename = StringUtils.cleanPath(multipartFiles.getOriginalFilename());

        imageService.save(multipartFiles);

        return filename;

    }*/

    //get clubs
    public List<Club> getClubs() {

        return clubRepository.findAll();
    }

    // get undelete clubs
    public List<Club> getUndeletedClub() {

        return clubRepository.getUndeletedClubs();
    }

    // delete club
    public void deleteClub(Long id) {
        Date date = new Date(System.currentTimeMillis());
        Club deletedClub = clubRepository.findById(id).orElse(null);
        deletedClub.setDeleted(true);
        deletedClub.setDeletedAt(date);
        clubRepository.save(deletedClub);

    }


    public Club updateClub(Club f, MultipartFile file,
                           List<MultipartFile> files2,
                           List<MultipartFile> multipartFiles) {
        User user = userRepository.findById(f.getOrganizer().getIdUser()).orElse(null);
        Club formation = clubRepository.findById(f.getIdClub()).orElse(null);
        f.setOrganizer(user);
        ;
        f.setCreatedAt(formation.getCreatedAt());

        Date date = new Date(System.currentTimeMillis());
        f.setModifiedAt(date);
        if (f.getImage() == null) {
            f.setImage(formation.getImage());
        }
        //Image
        if (file != null) {
            Image image = new Image(file.getOriginalFilename());
            f.setImage(image);
            imageService.save(file);
        } else {
            f.setImage(formation.getImage());
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
                f.getOtherFiles().add(fil);
                imageService.save(fi);

                fileRepository.save(fil);
            }
        }
        //EndImage


        return clubRepository.save(f);
    }
    // update club

    /* @Transactional
     public Club updateClub(Club f,MultipartFile file,List<MultipartFile> multipartFiles) {
         User user = userRepository.findById(f.getOrganizer().getIdUser()).orElse(null);
         Club club = clubRepository.findById(f.getIdClub()).orElse(null);
         f.setOrganizer(user);;
         f.setCreatedAt(club.getCreatedAt());

         Date date = new Date(System.currentTimeMillis());
         f.setModifiedAt(date);
         if (f.getImage() == null ) {
             f.setImage(club.getImage());
         }
         //Image
         if (file!=null) {
             Image image = new Image(file.getOriginalFilename());
             f.setImage(image);
             imageService.save(file);
         } else {
             f.setImage(club.getImage());

         }

         if (multipartFiles!=null){
             for (MultipartFile fi : multipartFiles){
                 File fil = new File(fi.getOriginalFilename());
                 f.getOtherFiles().add(fil);
                 imageService.save(fi);

                 fileRepository.save(fil);
             }
         }
         //EndImage


         return clubRepository.save(f);
     }*/
// participe club
    public Club participerClub(Long idf, Long idu) {
        User u = userRepository.findById(idu).orElse(null);
        Club f = clubRepository.findById(idf).orElse(null);


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
        Club f = clubRepository.findById(idf).orElse(null);


        if ((f.getMembers().contains(u))) {
            f.getMembers().remove(u);
            f.setNbParticipants(f.getNbParticipants() - 1);
            clubRepository.save(f);
            return true;
        }
        return false;

    }

    // retrieve club
    public Club retrieveClub(Long id) {

        return clubRepository.findById(id).orElse(null);
    }

    // retrieve club participants
    public List<User> retrieveClubParticipants(Long id) {
        Club f = clubRepository.findById(id).orElse(null);
        return (List<User>) f.getMembers();
        // return club != null ? club.getMembers() : Collections.emptySet();
    }

    /*public List<User> retrieveClubParticipants(Long id) {
        Club f = clubRepository.findById(id).orElse(null);
        return  f.getMembers();
    }*/
    //get other files
    public List<File> getOtherFiles(Long idF) {
        Club f = clubRepository.findById(idF).orElse(null);
        return f.getOtherFiles();
    }

    //delete file
    public void deleteFile(Long idFile) {

        fileRepository.deleteById(idFile);
    }
 //************************publication club***********
 /*public List<Club> getClubsByUser(Long idUser) {
     return clubRepository.findByCreatedByUserId(idUser);
 }*/
}
