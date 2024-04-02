package com.lord.small_box.dao;

import java.util.List;

import com.lord.small_box.models.Organization;
import com.lord.small_box.models.Supply;

public interface SupplyDao {
	
	public Supply findSupplyById(Long id);
	
	public Supply saveSupply(Supply supply);
	
	public void deleteSupplyById(Long id);
	
	public List<Supply> findAllSupplies();
	
	public List<Supply> findAllSuppliesByOrganization(Organization organization); 

}
