package com.stage.spring.entity;

import java.io.Serializable;
import java.util.Date;

import java.util.List;

import java.util.HashSet;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stage.spring.utils.UserCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUser; 


	
	private String firstName;
	
	private String lastName;

	@Column(unique = true)
	
	private String userName;
	
	private String email;
	
	private String password;
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> userRoles = new HashSet<>();

	private boolean stateUser;
	private boolean connected;
	private boolean locked;

	private boolean enabled;
	
	private boolean isDeleted;
	/*
	 * 
	 * 
	 * @Column(name = "failed_attempt") private int failedAttempt;
	 */

	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
	private Date createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedAt;

	@JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedAt;

	@OneToOne(cascade = CascadeType.ALL)
	private Image image;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date birthDate;

	private Integer age;
	private String roleU;
	@Column(name = "resettoken")
	private String resettoken;

	@OneToOne(cascade = CascadeType.ALL,fetch= FetchType.EAGER)
	@JoinColumn(name="code_id")
	private Code code;

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isLocked() {
		return locked;
	}

	//relation with class club
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_club",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "club_id")
	)
	private Set<Club> clubs = new HashSet<>();

//relation with class club publication
	@ManyToMany
	@JoinTable(name = "user_club_publication",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "club_publication_id"))
	private List<ClubPublication> clubPublications;

//relation with class renouvellement_club
@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
@JoinTable(
		name = "user_renouvellement_club",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "renouvellement_club_id")
)
private Set<RenouvellementClub> renouvellementClubs = new HashSet<>();
		
	
	
	
	
	


	


	


	


}