package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.SmallBoxUnifier;

public interface SmallBoxUnifierRepository extends JpaRepository<SmallBoxUnifier, Integer> {
	
	public List<SmallBoxUnifier> findByContainerId(Integer id);
	
	public void deleteAllByContainerId(Integer id);

}
