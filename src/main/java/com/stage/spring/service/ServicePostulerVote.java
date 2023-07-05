package com.stage.spring.service;

import com.stage.spring.entity.PostulerVote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ServicePostulerVote implements IServicePostulerVote
{


    @Override
    public List<PostulerVote> retrieveAllPost() {
        return null;
    }

    @Override
    public PostulerVote addPost(PostulerVote a) {
        return null;
    }

    @Override
    public void deletePost(Long id) {

    }

    @Override
    public PostulerVote updatePost(PostulerVote a) {
        return null;
    }

    @Override
    public PostulerVote retrievePostById(Long id) {
        return null;
    }
}
