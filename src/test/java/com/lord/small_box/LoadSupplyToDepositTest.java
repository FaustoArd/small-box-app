package com.lord.small_box;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lord.small_box.dao.DepositControlDao;
import com.lord.small_box.dao.PurchaseOrderDao;
import com.lord.small_box.dao.PurchaseOrderItemDao;
import com.lord.small_box.dao.SupplyDao;
import com.lord.small_box.dao.SupplyItemDao;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.SupplyCorrectionNote;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.Supply;
import com.lord.small_box.models.SupplyItem;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.utils.PdfToStringUtils;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class LoadSupplyToDepositTest {
	
	
	
	@Autowired
	private PdfToStringUtils pdfToStringUtils;
	
	@Autowired
	private DepositControlService depositControlService;
	
	@Autowired
	private LoadTestData loadTestData;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
	@Autowired
	private SupplyDao supplyDao;

	@Autowired
	private SupplyItemDao supplyItemDao;
	
	@Autowired
	private DepositControlDao depositControlDao;
	
	private PurchaseOrderDto purchaseOrderDto365;
	
	private long supplyId;
	
	@BeforeAll
	void setup()throws Exception {
		loadTestData.loadData();
		String strPurchaseOrder365= pdfToStringUtils.pdfToString("oc-365");
		purchaseOrderDto365 = depositControlService.collectPurchaseOrderFromText(strPurchaseOrder365);
		depositControlService.loadPurchaseOrderToDepositControl(purchaseOrderDto365.getId());
		assertThat(purchaseOrderDto365.getOrderNumber()).isEqualTo(365);
		purchaseOrderDto365.getItems().forEach(e -> System.out.println("Items: " + e.getCode()));
		Organization org = organizationService.findById(1L);
		Supply supply = Supply.builder().supplyNumber(551)
				.date(Calendar.getInstance())
				.dependencyApplicant(org)
				.estimatedTotalCost(new BigDecimal(70500))
				.jurisdiction("Desa")
				.build();
		Supply savedSupply = supplyDao.saveSupply(supply);
		supplyId = savedSupply.getId();
		SupplyItem supplyItem1 = SupplyItem.builder()
				.code("2.1.1.00788.0013")
				.quantity(5)
				.measureUnit("PAQUETE")
				.itemDetail("ho")
				.unitCost(new BigDecimal(3100))
				.estimatedCost(new BigDecimal(15500))
				.supply(savedSupply)
				.build();
		SupplyItem supplyItem2 = SupplyItem.builder()
				.code("2.1.1.00705.0035")
				.quantity(3)
				.measureUnit("PAQUETE")
				.unitCost(new BigDecimal(5000))
				.estimatedCost(new BigDecimal(15000))
				.supply(savedSupply)
				.build();
		
		SupplyItem supplyItem3 = SupplyItem.builder()
				.code("2.3.5.00407.0030")
				.quantity(8)
				.measureUnit("PAQUETE")
				.unitCost(new BigDecimal(8000))
				.estimatedCost(new BigDecimal(40000))
				.supply(savedSupply)
				.build();
		List<SupplyItem> supplyItems = List.of(supplyItem1,supplyItem2,supplyItem3);
		supplyItemDao.saveAll(supplyItems);
			
		
	}
	
	@Test
	@Order(1)
	void loadSupplyToDeposit()throws Exception{
		Supply supply  = supplyDao.findSupplyById(supplyId);
		List<SupplyItem> supplyItems = supplyItemDao.findAllBySupply(supply);
		List<SupplyReportDto> reportDto = createReport(supplyItems);
		reportDto.forEach(e -> System.out.println(e.getSupplyItemCode() + ", "+ e.getSupplyItemDetail() + ", " + e.getSupplyItemQuantity() + 
				", " + e.getDepositItemCode() + ", " + e.getDepositItemDetail()+ ", " + e.getDepositItemQuantity()));
	}
	
	@Test
	@Order(2)
	void mustReturnSupplyCorrectionNote()throws Exception{
		SupplyCorrectionNote supplyCorrectionNote = createSupplyCorrectionNote();
		Stream.of(supplyCorrectionNote)
		.forEach(e -> System.out.println("to: " + e.getTo()+", from: "
		+ e.getFrom()+ ", "));
		
		supplyCorrectionNote.getSupplyReport().stream().forEach(e -> System.out.println(e.getDepositItemCode() + 
				", " + e.getDepositItemDetail() + ", " ));
		
	}
	
	private List<SupplyReportDto> createReport(List<SupplyItem> supplyItems) {
		
		List<SupplyReportDto> report = supplyItems.stream().map(supplyItem -> {
			SupplyReportDto supplyReportDto = new SupplyReportDto();
			Optional<DepositControl> depositItem = depositControlDao.findByItemCode(supplyItem.getCode());
			if(depositItem.isPresent()) {
				supplyReportDto.setSupplyItemCode(supplyItem.getCode());
				supplyReportDto.setSupplyItemDetail(supplyItem.getItemDetail());
				supplyReportDto.setSupplyItemQuantity(supplyItem.getQuantity());
				supplyReportDto.setDepositItemCode(depositItem.get().getItemCode());
				supplyReportDto.setDepositItemDetail(depositItem.get().getItemName());
				supplyReportDto.setDepositItemQuantity(depositItem.get().getQuantity());
				supplyReportDto.setDepositQuantityLeft(depositItem.get().getQuantity()-supplyItem.getQuantity());
				return supplyReportDto;
			}else {
				
				supplyReportDto.setSupplyItemCode(supplyItem.getCode());
				supplyReportDto.setSupplyItemDetail(supplyItem.getItemDetail());
				supplyReportDto.setSupplyItemQuantity(supplyItem.getQuantity());
				supplyReportDto.setDepositItemCode("No encontrado");
				supplyReportDto.setDepositItemDetail("No encontrado");
				supplyReportDto.setDepositItemQuantity(0);
				
				return supplyReportDto;
			}
		}).toList();
		return report;
	}
	
	private SupplyCorrectionNote createSupplyCorrectionNote(){
		Supply supply = supplyDao.findSupplyById(supplyId);
		List<SupplyItem> supplyItems = supplyItemDao.findAllBySupply(supply);
		List<SupplyReportDto> reportDtos = createReport(supplyItems);
		Organization org = organizationService.findById(1L);
		String to = organizationService.findById(supply.getDependencyApplicant().getId()).getOrganizationName();
		SupplyCorrectionNote supplyCorrectionNote = new SupplyCorrectionNote();
		supplyCorrectionNote.setFrom(org.getOrganizationName());
		supplyCorrectionNote.setTo(to);
		supplyCorrectionNote.setSupplyReport(reportDtos);
		return supplyCorrectionNote;
	}
}
