package com.stage.spring.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import javax.persistence.CascadeType;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Club  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClub;

    private String nomClub;
    private String descriptionClub;
    private String category;
    private boolean isDeleted;
    private int nbMembres;
    private String emailResponsable;

    @ManyToOne
    private User organizer;
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
    @ManyToMany(mappedBy = "clubs")
    private Set<User> members = new HashSet<>();
    //relation with class event
    @ManyToMany(mappedBy = "clubs")
    private List<Event> events;


}
