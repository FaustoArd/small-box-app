package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

	public List<PurchaseOrder> findAllByOrganization(Organization organization);
}
