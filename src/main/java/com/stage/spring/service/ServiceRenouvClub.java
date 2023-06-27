package com.stage.spring.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stage.spring.entity.*;
import com.stage.spring.repository.ClubRepository;
import com.stage.spring.repository.FileRepository;
import com.stage.spring.repository.RenouvClubRepository;
import com.stage.spring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ServiceRenouvClub implements IServiceRenouvClub {

    @Autowired
    RenouvClubRepository renouvClubRepository;
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

    @Override
    public void addClub2(RenouvellementClub f, MultipartFile file) throws IOException {
        List<String> filenames = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        f.setCreatedAt(date);
        // Image
        Image image = new Image(file.getOriginalFilename());
        String filename1 = StringUtils.cleanPath(file.getOriginalFilename());
        filenames.add(filename1);
        f.setImage(image);
        imageService.save(file);
        renouvClubRepository.save(f);

    }

    @Override
    public void addClub(RenouvellementClub f, Long idu, MultipartFile file) throws IOException {
        List<String> filenames = new ArrayList<>();

        Date date = new Date(System.currentTimeMillis());
        f.setCreatedAt(date);
        //Image
        Image image = new Image(file.getOriginalFilename());
        String filename1 = StringUtils.cleanPath(file.getOriginalFilename());
        filenames.add(filename1);
        f.setImage(image);
        imageService.save(file);
        renouvClubRepository.save(f);
    }

    @Override
    public List<String> uploadFilesAndAffectToClub(Long idF, List<MultipartFile> multipartFiles) throws IOException {
        List<String> filenames = new ArrayList<>();

        RenouvellementClub f = renouvClubRepository.findById(idF).orElse(null);

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

    @Override
    public List<RenouvellementClub> getClubs() {
        return renouvClubRepository.findAll();
    }

    @Override
    public List<RenouvellementClub> getUndeletedClub() {
        return renouvClubRepository.getUndeletedClubs();
    }

    @Override
    public void deleteClub(Long id) {
        Date date = new Date(System.currentTimeMillis());
        RenouvellementClub deletedClub = renouvClubRepository.findById(id).orElse(null);
        deletedClub.setDeleted(true);
        deletedClub.setDeletedAt(date);
        renouvClubRepository.save(deletedClub);
    }

    @Override
    public RenouvellementClub updateClub(RenouvellementClub f, MultipartFile file,
                           List<MultipartFile> files2, List<MultipartFile> multipartFiles)
    {
        RenouvellementClub c = renouvClubRepository.findById(f.getIdREnouvClub()).orElse(null);
        ;
        f.setCreatedAt(c.getCreatedAt());

        Date date = new Date(System.currentTimeMillis());
        f.setModifiedAt(date);
        if (f.getImage() == null) {
            f.setImage(c.getImage());
        }
        //Image
        if (file != null) {
            Image image = new Image(file.getOriginalFilename());
            f.setImage(image);
            imageService.save(file);
        } else {
            f.setImage(c.getImage());
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


        return renouvClubRepository.save(f);
    }

    @Override
    public RenouvellementClub participerClub(Long idf, Long idu) {
        User u = userRepository.findById(idu).orElse(null);
        RenouvellementClub f = renouvClubRepository.findById(idf).orElse(null);


        //if(((f.getMaxParticipants()) >= (f.getNbParticipants() + 1))&&
        //	!(f.getParticipants().contains(u)))
        //	{
        f.getUsers().add(u);
        f.setNbParticipants(f.getNbParticipants() + 1);
        return renouvClubRepository.save(f);

    }

    @Override
    public Boolean annulerParticipation(Long idf, Long idu) {
        User u = userRepository.findById(idu).orElse(null);
        RenouvellementClub f = renouvClubRepository.findById(idf).orElse(null);


        if ((f.getUsers().contains(u))) {
            f.getUsers().remove(u);
            f.setNbParticipants(f.getNbParticipants() - 1);
            renouvClubRepository.save(f);
            return true;
        }
        return false;

    }

    @Override
    public RenouvellementClub retrieveClub(Long id) {
        return renouvClubRepository.findById(id).orElse(null);

    }

    @Override
    public List<User> retrieveClubParticipants(Long id) {
        RenouvellementClub f = renouvClubRepository.findById(id).orElse(null);
        return (List<User>) f.getUsers();
    }

    @Override
    public List<File> getOtherFiles(Long idF) {
        RenouvellementClub f = renouvClubRepository.findById(idF).orElse(null);
        return f.getOtherFiles();
    }

    @Override
    public void deleteFile(Long idFile) {
        fileRepository.deleteById(idFile);
    }
}
