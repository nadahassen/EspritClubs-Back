package com.stage.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stage.spring.entity.Role;
import com.stage.spring.entity.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
	Optional<Role> findByName(UserRole name);
}
