package com.lord.small_box.services;

import java.util.List;

import com.lord.small_box.dtos.BigBagDto;
import com.lord.small_box.dtos.BigBagItemDto;
import com.lord.small_box.dtos.DepositControlDto;
import com.lord.small_box.dtos.DepositDto;
import com.lord.small_box.dtos.DepositItemComparatorDto;
import com.lord.small_box.dtos.DepositResponseDto;
import com.lord.small_box.dtos.ExcelItemDto;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.dtos.SupplyCorrectionNoteDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.models.BigBag;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.models.SupplyItem;

public interface DepositControlService {
	
	public PurchaseOrderDto collectPurchaseOrderFromText(String text, long organizationId);
	
	public PurchaseOrderDto findFullPurchaseOrder(Long id);
	
	public List<PurchaseOrderToDepositReportDto> loadPurchaseOrderToDepositControl(Long purchaseOrderId,Long depositId);
	
	public SupplyDto collectSupplyFromText(String text, long organizationId);
	
	public List<SupplyReportDto> createSupplyReport(long supplyId,long depositId);
	
	public SupplyCorrectionNoteDto createSupplyCorrectionNote(long supplyId,Long  depositId);
	
	public List<PurchaseOrderDto> findAllOrdersByOrganizationId(long organizationId);
	
	public List<SupplyDto> findAllSuppliesByOrganizationId(long organizationId);
	
	public List<SupplyItemDto> findSupplyItems(long supplyId);
	
	public List<PurchaseOrderItemDto> findPurchaseOrderItems(long purchaseOrderId);
	
	public List<DepositControlDto> findDepositControlsByDeposit(long depositId);
	
	public String createDeposit(DepositDto depositDto);
	
	public List<DepositDto> findAllDepositsbyOrganization(long organizationId);
	
	public DepositResponseDto setCurrentDeposit(long userId,long organizationId,long depositId);
	
	public DepositResponseDto getCurrentDepositId(long userId,long organizationId);
	
	public int deletePurchaseOrder(long orderId);
	
	public int deleteSupply(long supplyId);
	
	public PurchaseOrderDto findPurchaseOrder(long purchaseOrderId);
	
	public SupplyDto findsupply(long supplyId);
	
	public BigBagDto createBigBag(BigBagDto bigBagDto,long organizationId);
	
	public int getTotalBigBagQuantityAvailable(long bigBagId, long depositId);
	
	public List<BigBagDto> findAllBigBagsByOrg(long organizationId);
	
	public BigBagItemDto updateBigBagItemQuantity(long bigBagItemId,int quantity);
	
	public List<BigBagItemDto> findAllBigBagItems(long bigBagId);
	
	public List<DepositItemComparatorDto> getExcelToPuchaseOrderComparator(List<ExcelItemDto> excelItems,long organizationId);
	
	public List<PurchaseOrderItem> saveExcelItemsToDepositControls(long organizationId,List<Long> selectedPurchaseOrderItemIds);

}
