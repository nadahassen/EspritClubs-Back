package com.stage.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.stage.spring.entity.User;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findUserByresettoken(String token);
	@Query("Select u from User u where u.isDeleted= 0")
	public List<User>  getUndeletedUsers();
	public List<User> findByUserName(String userName);
	@Query("Select u from User u where u.userName= :urname")
	public User findOneByUserName(@Param("urname")String urname );
	@Transactional
	@Modifying
	@Query("UPDATE User u " + "SET u.enabled = TRUE WHERE u.userName = ?1")
	int enableAppUser(String userName);
	/*@Transactional
	@Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.userName = ?2")
    @Modifying
    public void updateFailedAttempts(int failAttempts, String userName);*/
	
	User findByEmail(String email);
	
	
	
}
