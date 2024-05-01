package com.lord.small_box.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lord.small_box.models.Input;

public interface InputRepository extends JpaRepository<Input, Long>{
	
	public Optional<Input> findByDescription(String description);
	
	

	
}
