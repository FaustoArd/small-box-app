package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lord.small_box.models.SmallBox;

public interface SmallBoxRepository extends JpaRepository<SmallBox, Long> {
	
	
	public List<SmallBox> findAllOrderByInputInputNumber(String inputNumber);
	
	public List<SmallBox> findAllByContainerId(Long id);
	
	public List<SmallBox> findAllByContainerIdAndInputInputNumber(Long id, String inputNumber);
	
	public Integer countByInputInputNumber(String inputNumber);
	
	public List<SmallBox> findAllByContainerIdOrderByInputInputNumber(Long id);

}
