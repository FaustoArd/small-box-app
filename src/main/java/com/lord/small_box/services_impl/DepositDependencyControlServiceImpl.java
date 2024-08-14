package com.lord.small_box.services_impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lord.small_box.dtos.DepositDependencyControlDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.mappers.DepositDependencyControlMapper;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositDependencyControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.PurchaseOrderItem;
import com.lord.small_box.repositories.DepositDependencyControlRepository;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.services.DepositDependencyControlService;

@Service
public class DepositDependencyControlServiceImpl implements DepositDependencyControlService {

	@Autowired
	private final DepositDependencyControlRepository dependencyControlRepository;
	
	@Autowired
	private final OrganizationRepository organizationRepository;
	
	@Autowired
	private final DepositRepository depositRepository;

	

	private static final Logger log = LoggerFactory.getLogger(DepositDependencyControlServiceImpl.class);

	public DepositDependencyControlServiceImpl(DepositDependencyControlRepository dependencyControlRepository
			,OrganizationRepository organizationRepository,DepositRepository depositRepository) {
		this.dependencyControlRepository = dependencyControlRepository;
		this.organizationRepository = organizationRepository;
		this.depositRepository = depositRepository;
		
	}

	@Override
	public List<DepositDependencyControlDto> findAllByApplicantOrganizationAndDeposit(long applicantOrganizationId,long depositId) {
		Organization applicantOrganization = organizationRepository.findById(applicantOrganizationId)
				.orElseThrow(()-> new ItemNotFoundException("No se encontro la organizacion"));
		
		Deposit deposit = depositRepository.findById(depositId).orElseThrow(()-> new ItemNotFoundException("No se encontro la organizacion"));
		
		List<DepositDependencyControl> dependencyControls= 
				dependencyControlRepository.findAllByApplicantOrganizationAndDeposit(applicantOrganization,deposit);
		return DepositDependencyControlMapper.INSTANCE.dependencyControlToDtos(dependencyControls);
	
	}
	@Override
	public List<DepositDependencyControlDto> findAllByDeposit(long depositId) {
		Deposit deposit = depositRepository.findById(depositId).orElseThrow(()-> new ItemNotFoundException("No se encontro la organizacion"));
		List<DepositDependencyControl> dependencyControls = dependencyControlRepository.findAllByDeposit(deposit);
		return DepositDependencyControlMapper.INSTANCE.dependencyControlToDtos(dependencyControls);
	}
	

	@Override
	public List<PurchaseOrderToDepositReportDto> loadPurchaseOrderToDepositDependencyControl(PurchaseOrder purchaseOrder
			,List<PurchaseOrderItem> items,Deposit deposit,Organization applicantOrganization) {
		log.info("Load purchase order to dependency control");
		
		List<PurchaseOrderToDepositReportDto> report = new ArrayList<>();
		
		List<DepositDependencyControl> collectedItems = items.stream().map(orderItem -> {
			Optional<DepositDependencyControl> dependencyControlFound = dependencyControlRepository
					.findByItemCodeAndApplicantOrganizationAndDeposit(orderItem.getCode(), applicantOrganization, deposit);
			if(dependencyControlFound.isPresent()) {
				return updateDependencyControlExistingItem(report, purchaseOrder, deposit, dependencyControlFound, orderItem);
			}else {
				return createNewDependencyControlItem(report, purchaseOrder, deposit, orderItem,applicantOrganization);
			}
		}).toList();
		dependencyControlRepository.saveAll(collectedItems);
		
		return report;
	}

	private DepositDependencyControl updateDependencyControlExistingItem(List<PurchaseOrderToDepositReportDto> report,
			PurchaseOrder purchaseOrder, Deposit deposit, Optional<DepositDependencyControl> dependencyControlFound,
			PurchaseOrderItem purchaseOrderItem) {
		DepositDependencyControl dependencyControl = dependencyControlFound.get();
		dependencyControl.setQuantity(dependencyControl.getQuantity() + purchaseOrderItem.getQuantity());
		dependencyControl.setItemTotalPrice(
				dependencyControl.getItemUnitPrice().multiply(new BigDecimal(dependencyControl.getQuantity())));
	
		report.add(new PurchaseOrderToDepositReportDto(dependencyControl.getItemCode(),
				dependencyControl.getItemDescription(), dependencyControl.getQuantity(),
				dependencyControl.getMeasureUnit(), "ACTUALIZADO"));
		return dependencyControl;
	}

	private DepositDependencyControl createNewDependencyControlItem(List<PurchaseOrderToDepositReportDto> report,
			PurchaseOrder purchaseOrder, Deposit deposit, PurchaseOrderItem purchaseOrderItem,Organization applicantOrganization) {
		log.info("new item,creating dependency control item");
		DepositDependencyControl dependencyControl = new DepositDependencyControl();
		dependencyControl.setItemCode(purchaseOrderItem.getCode());
		dependencyControl.setItemDescription(purchaseOrderItem.getItemDetail());
		dependencyControl.setQuantity(purchaseOrderItem.getQuantity());
		dependencyControl.setItemTotalPrice(
				purchaseOrderItem.getUnitCost().multiply(new BigDecimal(dependencyControl.getQuantity())));
		dependencyControl.setItemUnitPrice(purchaseOrderItem.getUnitCost());
		dependencyControl.setMeasureUnit(purchaseOrderItem.getMeasureUnit());
		dependencyControl.setDeposit(deposit);
		dependencyControl.setApplicantOrganization(applicantOrganization);
		log.info("Add purchase order to dependency deposit report");
		report.add(new PurchaseOrderToDepositReportDto(dependencyControl.getItemCode(),
				dependencyControl.getItemDescription(), dependencyControl.getQuantity(),
				dependencyControl.getMeasureUnit(), "NUEVO"));
		return dependencyControl;
	}

	


}
