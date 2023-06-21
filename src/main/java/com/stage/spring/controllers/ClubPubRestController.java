package com.stage.spring.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.spring.entity.ClubPublication;
import com.stage.spring.entity.File;
import com.stage.spring.entity.User;
import com.stage.spring.service.ServiceClubPub;
import com.stage.spring.service.ServiceMail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ClubPub")
@CrossOrigin(origins = "http://localhost:4200")
public class ClubPubRestController {

    @Autowired
    ServiceClubPub serviceClub;
    @Autowired
    ServiceMail serviceMail;
    // add
    @PostMapping("/addclub/{idu}")
    public void addClub(@RequestParam("club") String club,
                        @RequestParam("file") MultipartFile file,

                        @PathVariable("idu") Long idu)
            throws IOException
    {
        ClubPublication f = new ObjectMapper().readValue(club, ClubPublication.class);
        serviceClub.addClub(f, idu,file);
    }

    //add without user



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


    //retreiveAll

    @GetMapping("/getclub")
    public List<ClubPublication> retreiveAllClubs() {
        return serviceClub.getClubs();
    }
    //get udelete club
    @GetMapping("/getundfor")
    public List<ClubPublication> retreiveAllUndeletedClubs() {
        return serviceClub.getUndeletedClub();
    }
    //update
    @PutMapping("/updateclub")
    public ClubPublication updateClub(
            @RequestParam("club") String club,
            @RequestParam(name="file", required=false) MultipartFile file,
            @RequestParam(name="files1", required=false) List<MultipartFile> files1,
            @RequestParam(name="files2", required=false) List<MultipartFile> files2 )

            throws JsonMappingException, JsonProcessingException
    {
        ClubPublication f = new ObjectMapper().readValue(club, ClubPublication.class);
        return  serviceClub.updateClub(f,file,files1,files2);

    }

    //delete
    @PutMapping("/deleteclub/{idf}")
    public void deleteClub(@PathVariable("idf") Long idf) {
        serviceClub.deleteClub(idf);
    }
    //participer
    @PutMapping("/participerclub/{idf}/{idu}")
    public ClubPublication participerClub(@PathVariable("idf") Long idf,@PathVariable("idu") Long idu) {
        return serviceClub.participerClub(idf, idu);
    }

    //annuler particip
    @PutMapping("/annulerparticipation/{idf}/{idu}")
    public Boolean annulerParticipation(@PathVariable("idf") Long idf,@PathVariable("idu") Long idu) {
        return serviceClub.annulerParticipation(idf, idu);
    }
    //retreive club X
    @GetMapping("/retrieve-club/{club-id}")
    public ClubPublication retrieveClub(@PathVariable("club-id") Long clubId) {
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
