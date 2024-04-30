package com.lord.small_box.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.lord.small_box.dtos.InputDto;
import com.lord.small_box.models.Input;

@Mapper
public interface InputMapper {

	public static InputMapper INSTNACE  = Mappers.getMapper(InputMapper.class);
	
	
	public Input toInput(InputDto inputDto);
	
	public InputDto toInputDto(Input input);
	
	public List<InputDto> toInputsDto(List<Input> inputs);
}
