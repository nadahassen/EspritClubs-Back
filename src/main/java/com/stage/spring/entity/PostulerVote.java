package com.stage.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import java.util.List;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class PostulerVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPsotuler;

    private String nom;
    private String prenom;
    private String post;
    private String emailCondidat;
    private String ageCondidat;


    //relation avec vote
    @OneToMany(mappedBy = "postulerVote")
    private List<Vote> votes ;


    //relation avec election
    @ManyToOne
    @JoinColumn(name = "election_id")
    private Election election;

    //relation avec user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
