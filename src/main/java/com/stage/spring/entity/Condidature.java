package com.stage.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Condidature {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConditature;

    private String nomPrenom;
    private String emailCondidat;
    private String classCondidat;


    private Integer telCondidat;
    private String motivation;
    private String experience;


//relation avec Clubpublication
@ManyToOne
@JoinColumn(name = "club_id")
private ClubPublication clubSouhaite;





}
