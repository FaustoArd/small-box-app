package com.lord.small_box.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lord.small_box.dtos.AppUserDto;
import com.lord.small_box.mappers.AppUserMapper;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.services.AppUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/smallbox/users")
@RequiredArgsConstructor
public class UserController {
	
	@Autowired
	private final AppUserService userService;
	
	private static final Gson gson = new Gson();
	
	@GetMapping("/all-users")
	ResponseEntity<List<AppUserDto>> findAllUsers(){
		List<AppUserDto> usersDto = userService.findAll();
		return ResponseEntity.ok(usersDto);
		
	}
	
	@GetMapping("/all-users-with-authorities")
	ResponseEntity<List<AppUserDto>> findAllUsersWithAutorities(){
		List<AppUserDto> userDtos = userService.findAllWithAuthorities();
		return ResponseEntity.ok(userDtos);
	}
	
	@GetMapping("/by-id/{id}")
	ResponseEntity<AppUserDto> findUserById(@PathVariable("id")Long id){
		AppUser user = userService.findById(id);
		AppUserDto userDto = AppUserMapper.INSTANCE.toUserDto(user);
		return ResponseEntity.ok(userDto);
	}
	@DeleteMapping("/delete-by-id/{id}")
	ResponseEntity<String> deleteUserById(@PathVariable("id")long id){
		String deletedUserFullName = userService.deleteUserById(id);
		return ResponseEntity.ok(gson.toJson(deletedUserFullName));
	}

}
