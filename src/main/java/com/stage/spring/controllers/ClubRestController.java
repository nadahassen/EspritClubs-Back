package com.stage.spring.controllers;

import com.stage.spring.entity.Club;
import com.stage.spring.entity.Mail;
import com.stage.spring.service.ServiceMail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;

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
    @Autowired
    ServiceMail serviceMail;
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

    // send email to the request
    /*@PostMapping("/sendMail")
    public void sendMail(@RequestBody  Mail mail) {
        String to = mail.getTo();
        String subject = "Club Creation Request";
        String body = "Dear student,\n\nThank you for your request to create a club. Your request has been accepted we will send you your crediantials soon." +
                "\n\nBest regards,\nEsprit Team";
        serviceMail.sendMail(to, subject, body);
    }
*/
    @PostMapping("/sendMail")
    public void sendMail(@RequestBody Map<String, String> mailData) {
        String to = mailData.get("to");
        String subject = "Club Creation Request";
        String body = "Dear student,\n\nThank you for your request to create a club. Your request has been accepted we will send you your crediantials soon." +
                "\n\nBest regards,\nEsprit Team";
        serviceMail.sendMail(to, subject, body);
    }
 //send when refused

    @PostMapping("/sendMailrefus")
    public void sendMailrefus(@RequestBody Map<String, String> mailData) {
        String to = mailData.get("to");
        String subject = "Rejet de la demande de création de club";
        String body = "Cher etudiant,\n\nNous regrettons de vous informer que votre demande de création de club a été refusée. Merci de votre compréhension.\\n\\nCordialement,\\nL'équipe d'Esprit";
        serviceMail.sendMailRefus(to, subject, body);
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












