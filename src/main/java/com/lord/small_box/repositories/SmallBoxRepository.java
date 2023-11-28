package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lord.small_box.models.SmallBox;

public interface SmallBoxRepository extends JpaRepository<SmallBox, Integer> {
	
	
	public List<SmallBox> findAllOrderByInputInputNumber(String inputNumber);
	
	public List<SmallBox> findAllByContainerId(Integer id);
	
	public List<SmallBox> findAllByContainerIdAndInputInputNumber(Integer id, String inputNumber);
	
	public Integer countByInputInputNumber(String inputNumber);
	
	public List<SmallBox> findAllByContainerIdOrderByInputInputNumber(Integer id);

}
