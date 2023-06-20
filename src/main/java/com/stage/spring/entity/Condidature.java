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
    private String email;
    private Integer tel;
    private String motivation;
    private String expérience;


//relation avec Club
@ManyToOne
@JoinColumn(name = "club_id")
private Club clubSouhaite;





}
