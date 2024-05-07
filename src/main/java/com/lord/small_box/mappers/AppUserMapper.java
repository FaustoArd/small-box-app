package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.AppUserDto;
import com.lord.small_box.models.AppUser;

@Mapper
public interface AppUserMapper {
	
	public static AppUserMapper INSTANCE = Mappers.getMapper(AppUserMapper.class);
	
	//@Mapping(target = "organizations", ignore = true)
	@Mapping(target = "authorities",ignore=true)
	public AppUserDto toUserDto(AppUser user);
	
	//@Mapping(target="organizations", ignore = true)
	List<AppUserDto> toUsersDto(List<AppUser> users);

}
