package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.lord.small_box.dtos.BigBagDto;
import com.lord.small_box.dtos.BigBagItemDto;
import com.lord.small_box.models.BigBag;
import com.lord.small_box.models.BigBagItem;

@Mapper
public interface BigBagMapper {

	public static final BigBagMapper INSTANCE = Mappers.getMapper(BigBagMapper.class);
	
	@Mapping(target="organization.id",source="organizationId")
	public BigBag dtoToBigBah(BigBagDto bigBagDto);
	
	@Mapping(target = "items", ignore = true)
	@Mapping(target = "organizationId", source = "organization.id")
	public BigBagDto bigBagToDto(BigBag bigBag);
	
	public List<BigBagDto> bigBagToDto(List<BigBag> bigBags);
}
