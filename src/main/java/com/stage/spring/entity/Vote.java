package com.stage.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVote;

    private Date dateVote;

    private Long codeElecteur;

    private String nomElection;

    private String nomCandidat;

    private String nomClub;

    //relation avec election :un vote est associé à une seule élection.
    @ManyToOne
    @JoinColumn(name = "election_id")
    private Election election;
    //relation avec user :'un vote est associé à un seul utilisateur.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
