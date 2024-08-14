package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.DepositDependencyControlDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositDependencyControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;

public interface DepositDependencyControlService {
	
	public List<DepositDependencyControlDto> findAllByApplicantOrganizationAndDeposit(long applicantOrganizationId,long depositId);
	
	public List<DepositDependencyControlDto> findAllByDeposit(long depositId);
	
	public List<PurchaseOrderToDepositReportDto> loadPurchaseOrderToDepositDependencyControl(PurchaseOrder purchaseOrder
			,List<PurchaseOrderItem> orderItems,Deposit deposit,Organization applicantOrganization);
	

}
