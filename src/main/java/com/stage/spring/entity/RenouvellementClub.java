package com.stage.spring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RenouvellementClub implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idREnouvClub;

    private String nomClub;
    private String descriptionClub;
    private String category;

    private int nbMembres;
    private String emailResponsable;

    private boolean isDeleted;
    private int nbParticipants;

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;
    @OneToMany(cascade =CascadeType.ALL)
    private List<File> OtherFiles;


    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date modifiedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date deletedAt;


    //relation with class User
    @ManyToMany(mappedBy = "renouvellementClubs")
    private Set<User> users = new HashSet<>();




}
