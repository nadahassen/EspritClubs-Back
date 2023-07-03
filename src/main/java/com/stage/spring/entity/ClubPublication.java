package com.stage.spring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClubPublication {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClub;

    private String nomClub;
    private String category;
    private String descriptionClub;
    private String emailClub;

    @ManyToOne
    private User organizer;
    private int nbParticipants;

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date modifiedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date deletedAt;

    private boolean isDeleted;
    //relation with class User
    @ManyToMany(mappedBy = "clubs")
    private Set<User> members = new HashSet<>();

    //relation condidature
    @OneToMany(mappedBy = "clubSouhaite")
    private List<Condidature> candidatures;

    @OneToMany(mappedBy = "clubPublication")
    private List<Election> elections;
}
