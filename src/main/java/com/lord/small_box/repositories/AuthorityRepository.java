package com.lord.small_box.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Authority;
import com.lord.small_box.models.AuthorityName;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	public Optional<Authority> findByAuthority(AuthorityName authority);
}
