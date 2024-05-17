package com.lord.small_box;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ListIterator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.repositories.BigBagItemRepository;
import com.lord.small_box.repositories.BigBagRepository;
import com.lord.small_box.repositories.DepositControlRepository;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.repositories.ExcelItemRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.repositories.PurchaseOrderItemRepository;
import com.lord.small_box.repositories.PurchaseOrderRepository;
import com.lord.small_box.repositories.SupplyRepository;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.PurchaseOrderService;
import com.lord.small_box.services.SupplyService;
import com.lord.small_box.utils.ExcelToListUtils;
import com.lord.small_box.utils.PdfToStringUtils;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class DepositRequestCorrectionNoteTest {
	
	
	@Autowired
	private DepositControlService depositControlService;
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private SupplyService supplyService;

	@Autowired
	private PdfToStringUtils pdfToStringUtils;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private OrganizationResponsibleRepository organizationResponsibleRepository;

	@Autowired
	private BigBagRepository bigBagRepository;

	@Autowired
	private BigBagItemRepository bigBagItemRepository;

	@Autowired
	private DepositRepository depositRepository;

	@Autowired
	private DepositControlRepository depositControlRepository;
	
	@Autowired
	private SupplyRepository supplyRepository;

	private long depositAvellanedaId;

	private long admYDespachoId;

	private long purchaseOrder365Id;
	
	private long supply177Id;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private PurchaseOrderItemRepository purchaseOrderItemRepository;

	@Autowired
	private ExcelToListUtils excelToListUtils;
	
	@Autowired
	private ExcelItemRepository excelItemRepository;
	
	private long depositControlTestId;
	@BeforeAll
	void setup() {
		OrganizationResponsible reyes = new OrganizationResponsible();
		reyes.setName("Blasa");
		reyes.setLastname("Reyes");
		OrganizationResponsible savedReyes = organizationResponsibleRepository.save(reyes);
		OrganizationResponsible pierpa = new OrganizationResponsible();
		pierpa.setName("Roxana");
		pierpa.setLastname("Pierpaoli");
		OrganizationResponsible savedPierpa = organizationResponsibleRepository.save(pierpa);
		OrganizationResponsible fabi = new OrganizationResponsible();
		fabi.setName("Fabian");
		fabi.setLastname("Yanes");
		OrganizationResponsible saveFabi = organizationResponsibleRepository.save(fabi);
		OrganizationResponsible iasil = new OrganizationResponsible();
		iasil.setName("Luciana");
		iasil.setLastname("Iasil");
		OrganizationResponsible saveIasil = organizationResponsibleRepository.save(iasil);

		OrganizationResponsible lagunas = new OrganizationResponsible();
		lagunas.setName("Analia");
		lagunas.setLastname("Lagunas");
		OrganizationResponsible savedLagunas = organizationResponsibleRepository.save(lagunas);

		Organization org1 = new Organization();
		org1.setOrganizationName("Secretaria de Desarrollo Social");
		org1.setOrganizationNumber(1);
		org1.setMaxRotation(12);
		org1.setMaxAmount(new BigDecimal(180000));
		org1.setResponsible(savedPierpa);

		Organization org2 = new Organization();
		org2.setOrganizationName("Direccion de administracion y despacho");
		org2.setOrganizationNumber(2);
		org2.setMaxRotation(12);
		org2.setMaxAmount(new BigDecimal(80000));
		org2.setResponsible(savedReyes);

		Organization org3 = new Organization();
		org3.setOrganizationName("Direccion de logistica");
		org3.setOrganizationNumber(3);
		org3.setMaxRotation(12);
		org3.setMaxAmount(new BigDecimal(45000));
		org3.setResponsible(saveFabi);

		Organization org4 = new Organization();
		org4.setOrganizationName("Subsecretaria de Politicas Socio Comunitarias");
		org4.setResponsible(saveIasil);
		org4.setMaxRotation(12);
		org4.setMaxAmount(new BigDecimal(100000));
		org4.setOrganizationNumber(4);

		Organization org5 = new Organization();
		org5.setOrganizationName("Dirección de Reinserción Social");
		org5.setOrganizationNumber(5);
		org5.setMaxAmount(new BigDecimal(100000));
		org5.setMaxRotation(12);
		org5.setResponsible(savedLagunas);

		Organization secDesSocial = organizationService.save(org1);
		Organization dirAdmDesp = organizationService.save(org2);
		admYDespachoId = dirAdmDesp.getId();
		organizationService.save(org3);
		organizationService.save(org4);
		organizationService.save(org5);

		Deposit deposit = Deposit.builder().name("AVELLANEDA").streetName("AVELLANEDA").houseNumber("2212")
				.organization(dirAdmDesp).build();
		Deposit savedDeposit = depositRepository.save(deposit);
		depositAvellanedaId = savedDeposit.getId();
	}
	
	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 360_24")
	@Order(17)
	void pdfToPurchaseOrder360_24() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "oc-67.pdf", "application/pdf",
				new ClassPathResource("\\pdf-test\\oc-667.pdf").getContentAsByteArray());
		String text = pdfToStringUtils.pdfToString(file.getBytes());
		PurchaseOrderDto purchaseOrderDto = purchaseOrderService.collectPurchaseOrderFromText(text, admYDespachoId);

		assertEquals(purchaseOrderDto.getOrderNumber(), 667);
		
		//Assert Purchase Order total cost equals sum of all items total cost
		assertThat(purchaseOrderDto.getPurchaseOrderTotal().doubleValue()).isEqualTo(purchaseOrderDto.getItems()
				.stream().mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		 //Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});

		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(), 19003030.00);
	}
	

}
