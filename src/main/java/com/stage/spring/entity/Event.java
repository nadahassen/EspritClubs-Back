package com.stage.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvent;

    private String titre;

    private String description;

    private Date date;

    private String lieu;
    private int nbPlace;

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;
    private boolean isDeleted=false;
    private int nbParticipants;
    @ManyToOne
    private User organizer;
   /* @ManyToMany
    private Set<User> participants = new HashSet<>();*/

    //relation avec table club
    @ManyToMany
    @JoinTable(
            name = "club_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    private List<Club> clubs;

    //relation avec table reservation
    @OneToMany(mappedBy = "event")
    private List<Reservation> reservations;
}
