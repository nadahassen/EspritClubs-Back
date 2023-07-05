package com.stage.spring.controllers;

import com.stage.spring.entity.Condidature;
import com.stage.spring.entity.PostulerVote;
import com.stage.spring.service.ServiceCondidature;
import com.stage.spring.service.ServicePostulerVote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Postuler")
@CrossOrigin(origins = "http://localhost:4200")
public class PostulerVoteRestController
{

    @Autowired
    ServicePostulerVote servicePost;

    @GetMapping("/retrieveAllPost")
    public List<PostulerVote> retrieveAllPost() {
        List<PostulerVote> list = servicePost.retrieveAllPost();
        return list;
    }

    @PostMapping("/addPost")
    public PostulerVote addPost(@RequestBody PostulerVote A) {
        return servicePost.addPost(A);
    }

    @PostMapping("/updatePost")
    public PostulerVote updatePost(@RequestBody PostulerVote A) {
        return servicePost.updatePost(A);
    }

    @DeleteMapping("/deletePost/{id}")
    public void deletePost(@PathVariable("id") Long id) {
        servicePost.deletePost(id);
    }

    @GetMapping("retrievePostById/{id}")
    public PostulerVote retrievePostById(@PathVariable Long id) {
        return servicePost.retrievePostById(id);
    }



}
