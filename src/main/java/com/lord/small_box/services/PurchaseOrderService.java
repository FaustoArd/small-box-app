package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;

public interface PurchaseOrderService {
	
	
	public PurchaseOrderDto collectPurchaseOrderFromText(String text, long organizationId);

	public PurchaseOrderDto findPurchaseOrder(long purchaseOrderId);

	public PurchaseOrderDto findFullPurchaseOrder(Long id);

	public int deletePurchaseOrder(long orderId);

	public List<PurchaseOrderToDepositReportDto> loadPurchaseOrderToDepositControl(Long purchaseOrderId,
			Long depositId);

	public List<PurchaseOrderDto> findAllOrdersByOrganizationId(long organizationId);

	public List<PurchaseOrderItemDto> findPurchaseOrderItems(long purchaseOrderId);
}
