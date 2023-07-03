package com.stage.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idElection;

    private String nomElection;

    private Date dateElection;

    private String typeElection;

    // relation avec club
    @ManyToOne
    @JoinColumn(name = "club_publication_id")
    private ClubPublication clubPublication;

    //relation avec vote :'une élection peut avoir plusieurs votes associés.
    @OneToMany(mappedBy = "election")
    private List<Vote> votes;














}
