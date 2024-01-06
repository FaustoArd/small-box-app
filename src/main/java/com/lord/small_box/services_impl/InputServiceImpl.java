package com.lord.small_box.services_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Input;
import com.lord.small_box.repositories.InputRepository;
import com.lord.small_box.services.InputService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InputServiceImpl implements InputService {

	@Autowired
	private final InputRepository inputRepository;

	@Override
	public List<Input> findAll() {
		return (List<Input>)inputRepository.findAll();
	}

	@Override
	public Input findById(Integer id) {
		return inputRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Input not found"));
	}

	@Override
	public Input save(Input input) {
		return inputRepository.save(input);
	}

	@Override
	public List<Input> findByExample(Input input) {
		StringMatcher match = StringMatcher.CONTAINING;
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(match).withIgnoreCase();
		Example<Input> example = Example.of(input, matcher);
		return (List<Input>)inputRepository.findAll(example);
		
	}
	
	
}
