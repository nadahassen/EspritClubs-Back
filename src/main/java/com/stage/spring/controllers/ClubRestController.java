package com.stage.spring.controllers;

import com.stage.spring.entity.Club;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.spring.entity.File;
import com.stage.spring.entity.User;
import com.stage.spring.service.ServiceClub;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/Club")
@CrossOrigin(origins = "http://localhost:4200")
public class ClubRestController {

    @Autowired
    ServiceClub serviceClub;

   // add
    @PostMapping("/addclub/{idu}")
    public void addClub(@RequestParam("club") String club,
                             @RequestParam("file") MultipartFile file,

                             @PathVariable("idu") Long idu)
            throws IOException
    {
        Club f = new ObjectMapper().readValue(club, Club.class);
        serviceClub.addClub(f, idu,file);
    }

    //add without user

    @PostMapping("/addclub2")
    public void addClub2(@RequestParam("club") String club,
                        @RequestParam("file") MultipartFile file

                       )
            throws IOException
    {
        Club f = new ObjectMapper().readValue(club, Club.class);
        serviceClub.addClub2(f,file);
    }

    //uploadFiles

   /* @PostMapping("/uploadFilesClub/{idF}")
    public List<String> uploadFiles(@PathVariable("idF") Long idF,@RequestParam("files")List<MultipartFile>
            multipartFiles) throws IOException {
        return  serviceClub.uploadFilesAndAffectToClub(idF,multipartFiles);
    }*/

    @PostMapping("/uploadFileClub/{idF}")
    public List<String> uploadFiles(	@PathVariable("idF") Long idF,@RequestParam("files")List<MultipartFile> multipartFiles) throws IOException {


        return  serviceClub.uploadFilesAndAffectToClub(idF,multipartFiles);

    }

    //retreiveAll

    @GetMapping("/getclub")
    public List<Club> retreiveAllClubs() {
        return serviceClub.getClubs();
    }
    //get udelete club
    @GetMapping("/getundfor")
    public List<Club> retreiveAllUndeletedClubs() {
        return serviceClub.getUndeletedClub();
    }
    //update
    @PutMapping("/updateclub")
    public Club updateClub(
            @RequestParam("club") String club,
            @RequestParam(name="file", required=false) MultipartFile file,
            @RequestParam(name="files1", required=false) List<MultipartFile> files1,
            @RequestParam(name="files2", required=false) List<MultipartFile> files2 )

            throws JsonMappingException, JsonProcessingException
    {
        Club f = new ObjectMapper().readValue(club, Club.class);
        return  serviceClub.updateClub(f,file,files1,files2);

    }

    //delete
    @PutMapping("/deleteclub/{idf}")
    public void deleteClub(@PathVariable("idf") Long idf) {
        serviceClub.deleteClub(idf);
    }
    //participer
    @PutMapping("/participerclub/{idf}/{idu}")
    public Club participerClub(@PathVariable("idf") Long idf,@PathVariable("idu") Long idu) {
        return serviceClub.participerClub(idf, idu);
    }

    //annuler particip
    @PutMapping("/annulerparticipation/{idf}/{idu}")
    public Boolean annulerParticipation(@PathVariable("idf") Long idf,@PathVariable("idu") Long idu) {
        return serviceClub.annulerParticipation(idf, idu);
    }
    //retreive club X
    @GetMapping("/retrieve-club/{club-id}")
    public Club retrieveClub(@PathVariable("club-id") Long clubId) {
        return serviceClub.retrieveClub(clubId);
    }



    // retrieve participant
    @GetMapping("/retrieve-participants-club/{club-id}")

    public List<User> retrieveClubParticipants(@PathVariable("club-id") Long id){
        return serviceClub.retrieveClubParticipants(id);

    }


    // get files
    @GetMapping("/list-other-files/{club-id}")
    public List<File> getOtherFiles(@PathVariable("club-id") Long clubId){
        return serviceClub.getOtherFiles(clubId);
    }




}












