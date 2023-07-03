package com.stage.spring.repository;

import com.stage.spring.entity.Vote;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository

public interface VoteRepository extends JpaRepository<Vote, String> {
}
