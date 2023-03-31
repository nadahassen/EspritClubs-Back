package com.stage.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_code")
public class Code implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idCode;
    @Lob
    @Column(name = "code")
    private String Code;

    @OneToOne(mappedBy = "code")
    private User user;

}

