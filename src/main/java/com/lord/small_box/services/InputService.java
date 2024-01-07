package com.lord.small_box.services;

import java.util.List;

import org.springframework.data.domain.Example;

import com.lord.small_box.models.Input;

public interface InputService {
	
	public List<Input> findAll();
	
	public Input findById(Long id);
	
	public Input save(Input input);
	
	public List<Input> findByExample(Input input);

}
