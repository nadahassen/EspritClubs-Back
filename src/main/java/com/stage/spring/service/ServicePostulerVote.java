package com.stage.spring.service;

import com.stage.spring.entity.Condidature;
import com.stage.spring.entity.PostulerVote;
import com.stage.spring.repository.CondidatureRepo;
import com.stage.spring.repository.PostulerVoteRepository;
import com.stage.spring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ServicePostulerVote implements IServicePostulerVote
{
    @Autowired
    PostulerVoteRepository postRepo;
    @Autowired
    UserRepository userRepository;

    @Override
    public List<PostulerVote> retrieveAllPost() {
        return postRepo.findAll();
    }

    @Override
    public PostulerVote addPost(PostulerVote a) {
        return postRepo.save(a);
    }

    @Override
    public void deletePost(Long id) {
        postRepo.deleteById(id);
    }

    @Override
    public PostulerVote updatePost(PostulerVote a) {
        return postRepo.save(a);
    }

    @Override
    public PostulerVote retrievePostById(Long id) {
        PostulerVote a =postRepo.findById(id).get();
        return a;
    }
}
