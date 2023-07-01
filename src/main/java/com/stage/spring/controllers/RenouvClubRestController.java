package com.stage.spring.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stage.spring.entity.File;
import com.stage.spring.entity.RenouvellementClub;
import com.stage.spring.entity.User;

import com.stage.spring.service.ServiceMail;
import com.stage.spring.service.ServiceRenouvClub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/RenouvClub")
@CrossOrigin(origins = "http://localhost:4200")
public class RenouvClubRestController {

    @Autowired
    ServiceRenouvClub serviceRenouvClub;
    @Autowired
    ServiceMail serviceMail;
    // add avec user
    @PostMapping("/addclub/{idu}")
    public void addClub(@RequestParam("club") String club,
                        @RequestParam("file") MultipartFile file,

                        @PathVariable("idu") Long idu)
            throws IOException
    {
        RenouvellementClub f = new ObjectMapper().readValue(club, RenouvellementClub.class);
        serviceRenouvClub.addClub(f, idu,file);
    }

    //add without user

    @PostMapping("/addclub2")
    public void addClub2(@RequestParam("club") String club,
                         @RequestParam("file") MultipartFile file

    )throws IOException
    {
        RenouvellementClub f = new ObjectMapper().readValue(club, RenouvellementClub.class);
        serviceRenouvClub.addClub2(f,file);
    }

    @PostMapping("/sendMail")
    public void sendMail(@RequestBody Map<String, String> mailData) {
        String to = mailData.get("to");
        String subject = "Club Renewal Request";
        String body = "Dear student,\n\nThank you for your request to Renewal a club. Your request has been accepted " +
                "\n\nBest regards,\nEsprit Team";
        serviceMail.sendMail(to, subject, body);
    }
    //send when refused

    @PostMapping("/sendMailrefus")
    public void sendMailrefus(@RequestBody Map<String, String> mailData) {
        String to = mailData.get("to");
        String subject = "Rejet de la demande de renouvellement de club";
        String body = "Cher etudiant,\n\nNous regrettons de vous informer que votre demande de renouvellement de club a été refusée. Merci de votre compréhension.\\n\\nCordialement,\\nL'équipe d'Esprit";
        serviceMail.sendMailRefus(to, subject, body);
    }
    //uploadFiles
    @PostMapping("/uploadFileClub/{idF}")
    public List<String> uploadFiles(@PathVariable("idF") Long idF, @RequestParam("files")List<MultipartFile> multipartFiles) throws IOException {

        return  serviceRenouvClub.uploadFilesAndAffectToClub(idF,multipartFiles);
    }

    //retreiveAll

    @GetMapping("/getclub")
    public List<RenouvellementClub> retreiveAllClubs()
    {
        return serviceRenouvClub.getClubs();
    }

    //get udelete club
    @GetMapping("/getundfor")
    public List<RenouvellementClub> retreiveAllUndeletedClubs() {
        return serviceRenouvClub.getUndeletedClub();
    }
    //update
    @PutMapping("/updateclub")
    public RenouvellementClub updateClub(
            @RequestParam("club") String club,
            @RequestParam(name="file", required=false) MultipartFile file,
            @RequestParam(name="files1", required=false) List<MultipartFile> files1,
            @RequestParam(name="files2", required=false) List<MultipartFile> files2 )

            throws JsonMappingException, JsonProcessingException
    {
        RenouvellementClub f = new ObjectMapper().readValue(club, RenouvellementClub.class);
        return  serviceRenouvClub.updateClub(f,file,files1,files2);

    }

    //delete
    @PutMapping("/deleteclub/{idf}")
    public void deleteClub(@PathVariable("idf") Long idf) {
        serviceRenouvClub.deleteClub(idf);
    }
    //participer
    @PutMapping("/participerclub/{idf}/{idu}")
    public RenouvellementClub participerClub(@PathVariable("idf") Long idf,@PathVariable("idu") Long idu) {
        return serviceRenouvClub.participerClub(idf, idu);
    }

    //annuler particip
    @PutMapping("/annulerparticipation/{idf}/{idu}")
    public Boolean annulerParticipation(@PathVariable("idf") Long idf,@PathVariable("idu") Long idu) {
        return serviceRenouvClub.annulerParticipation(idf, idu);
    }
    //retreive club X
    @GetMapping("/retrieve-club/{club-id}")
    public RenouvellementClub retrieveClub(@PathVariable("club-id") Long clubId) {

        return serviceRenouvClub.retrieveClub(clubId);
    }

    // retrieve participant
    @GetMapping("/retrieve-participants-club/{club-id}")

    public List<User> retrieveClubParticipants(@PathVariable("club-id") Long id){
        return serviceRenouvClub.retrieveClubParticipants(id);

    }
    // get files
    @GetMapping("/list-other-files/{club-id}")
    public List<File> getOtherFiles(@PathVariable("club-id") Long clubId){
        return serviceRenouvClub.getOtherFiles(clubId);
    }




}