package com.stage.spring.service;

import com.stage.spring.entity.Condidature;
import com.stage.spring.entity.PostulerVote;

import java.util.List;

public interface IServicePostulerVote {

    List<PostulerVote> retrieveAllPost ();

    PostulerVote  addPost (PostulerVote a) ;

    void deletePost (Long id);

    PostulerVote  updatePost(PostulerVote  a);

    PostulerVote  retrievePostById(Long id);
}
