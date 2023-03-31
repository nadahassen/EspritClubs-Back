package com.stage.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stage.spring.entity.PasswordResetTokenEntity;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long>{
	PasswordResetTokenEntity findByToken(String token);
	PasswordResetTokenEntity findByCode(String code);
}
