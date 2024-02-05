package com.lord.small_box.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.WorkTemplateDto;
import com.lord.small_box.models.WorkTemplate;

@Mapper
public interface WorkTemplateMapper {

	public static WorkTemplateMapper INSTANCE = Mappers.getMapper(WorkTemplateMapper.class);
	
	@Mapping(target="organization", ignore = true)
	public WorkTemplate toWorkTemplate(WorkTemplateDto workTemplateDto);
	
	public WorkTemplateDto toWorkTemplateDto(WorkTemplate workTemplate);
	
	public List<WorkTemplateDto> toWorkTemplateDtoList(List<WorkTemplate> workTemplates);
}
