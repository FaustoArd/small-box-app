package com.lord.small_box.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.mapstruct.factory.Mappers;

import com.lord.small_box.dtos.DepositDependencyControlDto;
import com.lord.small_box.models.DepositDependencyControl;

@Mapper
public interface DepositDependencyControlMapper {
	
	public static DepositDependencyControlMapper INSTANCE = Mappers.getMapper(DepositDependencyControlMapper.class);
	
	@Mapping(target="mainOrganization", source = "organization.organizationName")
	@Mapping(target="applicantOrganization", source = "applicantOrganization.organizationName")
	@Mapping(target="applicantOrganizationId", source="applicantOrganization.id")
	@Mapping(target="depositName", source="deposit.name")
	@Mapping(target="depositId", source="deposit.id")
	public DepositDependencyControlDto DependencyControlToDto(DepositDependencyControl dependencyControl);
	
	@Mapping(target="organization.organizationName", source="mainOrganization")
	@Mapping(target="applicantOrganization.organizationName", source="applicantOrganization")
	@Mapping(target="applicantOrganization.id", source="applicantOrganizationId")
	@Mapping(target="deposit.name", source="depositName")
	@Mapping(target = "deposit.id", source="depositId")
	public DepositDependencyControl dtoToDependencyControl(DepositDependencyControlDto dependencyControlDto);
	
	public List<DepositDependencyControlDto> dependencyControlToDtos(List<DepositDependencyControl> dependencyControls);

}
