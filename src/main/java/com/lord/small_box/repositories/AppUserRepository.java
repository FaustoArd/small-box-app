package com.lord.small_box.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	
	public Optional<AppUser> findByUsername(String username);

}
