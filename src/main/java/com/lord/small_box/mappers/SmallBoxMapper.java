package com.lord.small_box.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.lord.small_box.dtos.SmallBoxDto;
import com.lord.small_box.models.SmallBox;

@Mapper
public interface  SmallBoxMapper {
	
	public static SmallBoxMapper INSTANCE = Mappers.getMapper(SmallBoxMapper.class);
	
	@Mapping(target="input.inputNumber", source="inputNumber")
	@Mapping(target="input.description", source="description")
	@Mapping(target="input.id", source="inputId")
	@Mapping(target="container.id", source="containerId")
	@Mapping(target="subtotal", ignore = true)
	public  SmallBox toSmallBox(SmallBoxDto smallBoxDto);
	
	@Mapping(target="inputNumber",source="input.inputNumber" )
	@Mapping(target="description", source="input.description")
	@Mapping(target="inputId", source="input.id")
	@Mapping(target="containerId", source="container.id")
	@Mapping(target="subtotal", source="subtotal.subtotal")
	public SmallBoxDto toSmallBoxDto(SmallBox smallBox);
		
	
	@Mapping(target="inputNumber",source="input.inputNumber" )
	@Mapping(target="description", source="input.description")
	@Mapping(target="inputId", source="input.id")
	@Mapping(target="containerId", source="container.id")
	@Mapping(target="subtotal", source="subtotal.subtotal")
	public abstract List<SmallBoxDto> toSmallBoxesDtos(List<SmallBox> smallBoxes);
	
	

}
