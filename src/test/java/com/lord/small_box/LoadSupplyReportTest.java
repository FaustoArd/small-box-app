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


import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.SupplyCorrectionNoteDto;
import com.lord.small_box.dtos.SupplyReportDto;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.Supply;
import com.lord.small_box.models.SupplyItem;
import com.lord.small_box.repositories.DepositControlRepository;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.repositories.PurchaseOrderItemRepository;
import com.lord.small_box.repositories.PurchaseOrderRepository;
import com.lord.small_box.repositories.SupplyItemRepository;
import com.lord.small_box.repositories.SupplyRepository;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.PurchaseOrderService;
import com.lord.small_box.services.SupplyService;
import com.lord.small_box.utils.PdfToStringUtils;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class LoadSupplyReportTest {
	
	
	
	@Autowired
	private PdfToStringUtils pdfToStringUtils;
	
	@Autowired
	private DepositControlService depositControlService;
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private SupplyService supplyService;
	
	@Autowired
	private LoadTestData loadTestData;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	private PurchaseOrderItemRepository purchaseOrderItemRepository;
	
	@Autowired
	private SupplyRepository supplyRepository;

	@Autowired
	private SupplyItemRepository supplyItemRepository;
	
	@Autowired
	private DepositControlRepository depositControlRepository;
	
	@Autowired
	private DepositRepository depositRepository;
	
	private PurchaseOrderDto purchaseOrderDto365;
	
	private long supplyId;
	
	@BeforeAll
	void setup()throws Exception {
		loadTestData.loadData();
		String strPurchaseOrder365= pdfToStringUtils.pdfToString("oc-365.pdf");
		purchaseOrderDto365 = purchaseOrderService.collectPurchaseOrderFromText(strPurchaseOrder365,2L);
		purchaseOrderService.loadPurchaseOrderToDepositControl(purchaseOrderDto365.getId(),1L);
		assertThat(purchaseOrderDto365.getOrderNumber()).isEqualTo(365);
		purchaseOrderDto365.getItems().forEach(e -> System.out.println("Items: " + e.getCode()));
		purchaseOrderDto365.getItems().forEach(e -> System.out.println("Items: " + e.getQuantity()));
		Organization org = organizationService.findById(2L);
		Supply supply = Supply.builder().supplyNumber(551)
				.date(Calendar.getInstance())
				.dependencyApplicant("Direccion de Inclusion")
				.estimatedTotalCost(new BigDecimal(70500))
				.jurisdiction("Desa")
				.mainOrganization(org)
				.build();
		Supply savedSupply = supplyRepository.save(supply);
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
		supplyItemRepository.saveAll(supplyItems);
			
		
	}
	
	@Test
	@Order(1)
	void loadSupplyToDeposit()throws Exception{
		Supply supply  = supplyRepository.findById(supplyId)
				.orElseThrow(()->new ItemNotFoundException("No se encontrol el suministro"));
		List<SupplyItem> supplyItems = supplyItemRepository.findAllBySupply(supply);
		List<SupplyReportDto> reportDto = createReport(supplyItems);
		reportDto.forEach(e -> System.out.println(e.getSupplyItemCode() + ", "+ e.getSupplyItemDetail() + ", " + e.getSupplyItemQuantity() + 
				", " + e.getDepositItemCode() + ", " + e.getDepositItemDetail()+ ", " + e.getDepositItemQuantity()));
	}
	
	@Test
	@Order(2)
	void mustReturnSupplyCorrectionNote()throws Exception{
		SupplyCorrectionNoteDto supplyCorrectionNote = createSupplyCorrectionNote();
		Stream.of(supplyCorrectionNote)
		.forEach(e -> System.out.println("to: " + e.getTo()+", from: "
		+ e.getFrom()+ ", "));
		
		supplyCorrectionNote.getSupplyReport().stream().forEach(e -> System.out.println(e.getDepositItemCode() + 
				", " + e.getDepositItemDetail() + ", " ));
		
	}
	
	private List<SupplyReportDto> createReport(List<SupplyItem> supplyItems) {
		Deposit deposit = depositRepository.findById(1L).orElseThrow(()-> new ItemNotFoundException(" No se encontro el deposito")); 
		List<SupplyReportDto> report = supplyItems.stream().map(supplyItem -> {
			SupplyReportDto supplyReportDto = new SupplyReportDto();
			Optional<DepositControl> depositItem = depositControlRepository.findByItemCodeAndDeposit(supplyItem.getCode(),deposit);
			if(depositItem.isPresent()) {
				supplyReportDto.setSupplyItemCode(supplyItem.getCode());
				supplyReportDto.setSupplyItemDetail(supplyItem.getItemDetail());
				supplyReportDto.setSupplyItemQuantity(supplyItem.getQuantity());
				supplyReportDto.setDepositItemCode(depositItem.get().getItemCode());
				supplyReportDto.setDepositItemDetail(depositItem.get().getItemDescription());
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
	
	private SupplyCorrectionNoteDto createSupplyCorrectionNote(){
		Supply supply = supplyRepository.findById(supplyId)
				.orElseThrow(()->new ItemNotFoundException("No se encontrol el suministro"));;
		List<SupplyItem> supplyItems = supplyItemRepository.findAllBySupply(supply);
		List<SupplyReportDto> reportDtos = createReport(supplyItems);
		Organization org = organizationService.findById(supply.getMainOrganization().getId());
		SupplyCorrectionNoteDto supplyCorrectionNote = new SupplyCorrectionNoteDto();
		supplyCorrectionNote.setFrom(org.getOrganizationName());
		supplyCorrectionNote.setTo(supply.getDependencyApplicant());
		supplyCorrectionNote.setSupplyReport(reportDtos);
		return supplyCorrectionNote;
	}
}
