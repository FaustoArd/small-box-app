package com.lord.small_box;

import java.util.Comparator;

import com.lord.small_box.dtos.OrganizationDto;

public class NameComparator implements Comparator<OrganizationDto> {

	@Override
	public int compare(OrganizationDto o1, OrganizationDto o2) {
		return o1.getOrganizationName().compareTo(o2.getOrganizationName());
	}

}
