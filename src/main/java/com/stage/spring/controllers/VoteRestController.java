package com.stage.spring.controllers;

import com.stage.spring.entity.PostulerVote;
import com.stage.spring.repository.PostulerVoteRepository;
import com.stage.spring.service.IServiceVote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stage.spring.entity.Vote;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/Vote")
@CrossOrigin(origins = "http://localhost:4200")
public class VoteRestController {

    @Autowired
    private IServiceVote serviceVote;
    @Autowired
    private PostulerVoteRepository postulerVoteRepository;
    @GetMapping("/get-all-votes")
    public List<Vote> getAllVotes() {

        return serviceVote.retrieveAllVotes();
    }

    @GetMapping("getVoteById/{id}")
    public ResponseEntity<Vote> getVoteById(@PathVariable Long id) {
        Vote v = serviceVote.retrieveVote(id);
        if (v != null) {
            return ResponseEntity.ok(v);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/addVote")
    public ResponseEntity<Vote> addVote(@RequestBody Vote v) {
        Vote addedVote = serviceVote.addVote(v);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedVote);
    }

    @PutMapping("/updateVote/{id}")
    public ResponseEntity<Vote> updateVote(@PathVariable Long id, @RequestBody Vote v) {
        if (!serviceVote.checkIfExist(id)) {
            return ResponseEntity.notFound().build();
        }
        v.setIdVote(id);
        Vote updatedVote = serviceVote.updateVote(v);
        return ResponseEntity.ok(updatedVote);
    }

    @DeleteMapping("/deleteVote/{id}")
    public ResponseEntity<Void> deleteElection(@PathVariable Long id) {
        if (!serviceVote.checkIfExist(id)) {
            return ResponseEntity.notFound().build();
        }
        serviceVote.deleteVote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paged")
    public Page<Vote> getPagedVotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField));
        return serviceVote.getVotesPaged(pageable);
    }

   /* @GetMapping("/election/{electionId}/candidate/{candidateName}/party/{clubName}")
    public ResponseEntity<Map<String, Long>> getVoteResultsForElection(
            @PathVariable("electionId") Long electionId,
            @PathVariable("candidateName") String candidateName,
            @PathVariable("clubName") String clubName) {
        Map<String, Long> voteResults = serviceVote.getVoteResultsForElection(electionId, candidateName, clubName);
        if (!voteResults.isEmpty()) {
            return new ResponseEntity<>(voteResults, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/



    //vote counting for a candidate
    @PostMapping("/count")
    public ResponseEntity<?> countVote(@RequestParam Long candidateId) {
        // Retrieve the postulerVote record for the candidate from the database
        PostulerVote postulerVote = postulerVoteRepository.findById(candidateId).orElse(null);

        if (postulerVote == null) {
            // Handle the case when the candidate does not exist
            return ResponseEntity.notFound().build();
        }

        // Increment the vote count
        int newVoteCount = postulerVote.getVoteCount() + 1;
        postulerVote.setVoteCount(newVoteCount);

        // Calculate the percentage of votes for the candidate
        int totalVotes = postulerVote.getElection().getPostulerVotes().size();
        double percentage = (newVoteCount / (double) totalVotes) * 100;
        postulerVote.setPercentage(percentage);

        // Save the updated postulerVote record
        postulerVoteRepository.save(postulerVote);

        // Create a response object with the updated vote count and percentage
        Map<String, Object> response = new HashMap<>();
        response.put("voteCount", newVoteCount);
        response.put("percentage", percentage);

        // Return the response as the API response
        return ResponseEntity.ok(response);
    }



}
