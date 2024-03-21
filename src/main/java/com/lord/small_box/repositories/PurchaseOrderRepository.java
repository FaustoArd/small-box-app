package com.lord.small_box.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

}
