package com.stage.spring.repository;

import com.stage.spring.entity.PostulerVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PostulerVoteRepository extends JpaRepository<PostulerVote, Long> {
}
