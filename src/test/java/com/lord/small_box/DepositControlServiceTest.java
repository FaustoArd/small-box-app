package com.lord.small_box;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lord.small_box.dtos.BigBagDto;
import com.lord.small_box.dtos.BigBagItemDto;
import com.lord.small_box.dtos.DepositControlDto;
import com.lord.small_box.dtos.ExcelItemDto;
import com.lord.small_box.dtos.DepositItemComparatorDto;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemCandidateDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.mappers.BigBagMapper;
import com.lord.small_box.mappers.DepositControlMapper;
import com.lord.small_box.mappers.PurchaseOrderItemMapper;
import com.lord.small_box.mappers.PurchaseOrderMapper;
import com.lord.small_box.models.BigBag;
import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.DepositControl;
import com.lord.small_box.models.ExcelItem;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.SupplyItem;
import com.lord.small_box.repositories.BigBagItemRepository;
import com.lord.small_box.repositories.BigBagRepository;
import com.lord.small_box.repositories.DepositControlRepository;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.repositories.ExcelItemRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.repositories.PurchaseOrderItemRepository;
import com.lord.small_box.repositories.PurchaseOrderRepository;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.text_analisys.TextToPurchaseOrder;
import com.lord.small_box.utils.ExcelToListUtils;
import com.lord.small_box.utils.PdfToStringUtils;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class DepositControlServiceTest {

	@Autowired
	private DepositControlService depositControlService;

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

	private long depositAvellanedaId;

	private long admYDespachoId;

	private long purchaseOrder365Id;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private PurchaseOrderItemRepository purchaseOrderItemRepository;

	@Autowired
	private ExcelToListUtils excelToListUtils;
	
	@Autowired
	private ExcelItemRepository excelItemRepository;
	

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
	@DisplayName("CARGAR ORDEN DE COMPRA N 365_24")
	@Order(1)
	void pdfToPurchaseOrder365_24() throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-365.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text, 2L);
		purchaseOrder365Id = purchaseOrderDto.getId();
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0013");
		assertEquals(purchaseOrderDto.getItems().get(0).getQuantity(), 15);
		assertThat(purchaseOrderDto.getItems().get(0).getItemDetail().length()).isGreaterThan(4);
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00705.0035");
		assertEquals(purchaseOrderDto.getItems().get(1).getQuantity(), 8);
		assertThat(purchaseOrderDto.getItems().get(1).getItemDetail().length()).isGreaterThan(4);
		assertEquals(purchaseOrderDto.getItems().get(2).getCode(), "2.1.1.00592.0001");
		assertEquals(purchaseOrderDto.getItems().get(2).getQuantity(), 15);
		assertThat(purchaseOrderDto.getItems().get(2).getItemDetail().length()).isGreaterThan(4);
		assertEquals(purchaseOrderDto.getItems().get(3).getCode(), "2.1.1.00591.0002");
		assertEquals(purchaseOrderDto.getItems().get(3).getQuantity(), 20);
		assertThat(purchaseOrderDto.getItems().get(3).getItemDetail().length()).isGreaterThan(4);
		assertEquals(purchaseOrderDto.getItems().get(4).getCode(), "2.1.1.00705.0036");
		assertEquals(purchaseOrderDto.getItems().get(4).getQuantity(), 10);
		assertThat(purchaseOrderDto.getItems().get(4).getItemDetail().length()).isGreaterThan(4);
		assertEquals(purchaseOrderDto.getItems().get(5).getCode(), "2.1.1.00511.0008");
		assertEquals(purchaseOrderDto.getItems().get(5).getQuantity(), 10);
		assertThat(purchaseOrderDto.getItems().get(5).getItemDetail().length()).isGreaterThan(4);
		assertEquals(purchaseOrderDto.getItems().get(6).getCode(), "2.1.1.00705.0012");
		assertEquals(purchaseOrderDto.getItems().get(6).getQuantity(), 8);
		assertThat(purchaseOrderDto.getItems().get(6).getItemDetail().length()).isGreaterThan(4);
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02113.0002");
		assertEquals(purchaseOrderDto.getItems().get(7).getQuantity(), 1);
		assertThat(purchaseOrderDto.getItems().get(7).getItemDetail().length()).isGreaterThan(4);
		assertEquals(purchaseOrderDto.getOrderNumber(), 365);

		// Assert Purchase Order total cost equals sum of all items total cost
		assertThat(purchaseOrderDto.getPurchaseOrderTotal().doubleValue()).isEqualTo(purchaseOrderDto.getItems()
				.stream().mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item -> {

			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(), 295600.00);
	}

	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 340_24")
	@Order(2)
	void pdfToPurchaseOrder340_24() throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-340.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text, 2L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0091");
		assertEquals(purchaseOrderDto.getItems().get(0).getQuantity(), 350);
		assertEquals(purchaseOrderDto.getItems().get(0).getUnitCost().doubleValue(), 1580.00000);
		assertEquals(purchaseOrderDto.getItems().get(0).getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00439.0001");
		assertEquals(purchaseOrderDto.getItems().get(1).getQuantity(), 50);
		assertEquals(purchaseOrderDto.getItems().get(1).getUnitCost().doubleValue(), 4500.00000);
		assertEquals(purchaseOrderDto.getItems().get(1).getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getItems().get(2).getCode(), "2.1.1.00520.0003");
		assertEquals(purchaseOrderDto.getItems().get(2).getQuantity(), 90);
		assertEquals(purchaseOrderDto.getItems().get(2).getUnitCost().doubleValue(), 600.00000);
		assertEquals(purchaseOrderDto.getItems().get(2).getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(3).getCode(), "2.1.1.00455.0007");
		assertEquals(purchaseOrderDto.getItems().get(3).getQuantity(), 6);
		assertEquals(purchaseOrderDto.getItems().get(3).getUnitCost().doubleValue(), 3100.00000);
		assertEquals(purchaseOrderDto.getItems().get(3).getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().get(4).getCode(), "2.1.1.00489.0066");
		assertEquals(purchaseOrderDto.getItems().get(4).getQuantity(), 3);
		assertEquals(purchaseOrderDto.getItems().get(4).getUnitCost().doubleValue(), 3100.00000);
		assertEquals(purchaseOrderDto.getItems().get(4).getMeasureUnit(), "BOLSA");
		assertEquals(purchaseOrderDto.getItems().get(5).getCode(), "2.1.1.00489.0096");
		assertEquals(purchaseOrderDto.getItems().get(5).getQuantity(), 3);
		assertEquals(purchaseOrderDto.getItems().get(5).getUnitCost().doubleValue(), 6900.00000);
		assertEquals(purchaseOrderDto.getItems().get(5).getMeasureUnit(), "BOLSA");
		assertEquals(purchaseOrderDto.getItems().get(6).getCode(), "2.1.1.00489.0067");
		assertEquals(purchaseOrderDto.getItems().get(6).getQuantity(), 3);
		assertEquals(purchaseOrderDto.getItems().get(6).getUnitCost().doubleValue(), 5100.00000);
		assertEquals(purchaseOrderDto.getItems().get(6).getMeasureUnit(), "BOLSA");
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02444.0003");
		assertEquals(purchaseOrderDto.getItems().get(7).getQuantity(), 7);
		assertEquals(purchaseOrderDto.getItems().get(7).getUnitCost().doubleValue(), 1400.00000);
		assertEquals(purchaseOrderDto.getItems().get(7).getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getOrderNumber(), 340);

		// Assert Purchase Order total cost equals sum of all items total cost
		assertThat(purchaseOrderDto.getPurchaseOrderTotal().doubleValue()).isEqualTo(purchaseOrderDto.getItems()
				.stream().mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});

		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(), 905700.00);
	}

	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 429_24")
	@Order(3)
	void pdfToPurchaseOrder429_24() throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-429.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text, 2L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.02610.0001");
		assertEquals(purchaseOrderDto.getItems().get(0).getQuantity(), 32);
		assertEquals(purchaseOrderDto.getItems().get(0).getProgramaticCat(), "38.09.00");
		assertEquals(purchaseOrderDto.getItems().get(0).getUnitCost().doubleValue(), 1550.00000);
		assertEquals(purchaseOrderDto.getItems().get(0).getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.02429.0001");
		assertEquals(purchaseOrderDto.getItems().get(1).getQuantity(), 20);
		assertEquals(purchaseOrderDto.getItems().get(1).getUnitCost().doubleValue(), 1490.00000);
		assertEquals(purchaseOrderDto.getItems().get(1).getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getItems().get(2).getCode(), "2.1.1.02430.0003");
		assertEquals(purchaseOrderDto.getItems().get(2).getQuantity(), 20);
		assertEquals(purchaseOrderDto.getItems().get(2).getUnitCost().doubleValue(), 1410.00000);
		assertEquals(purchaseOrderDto.getItems().get(2).getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(3).getCode(), "2.1.1.00542.0023");
		assertEquals(purchaseOrderDto.getItems().get(3).getQuantity(), 24);
		assertEquals(purchaseOrderDto.getItems().get(3).getProgramaticCat(), "38.09.00");
		assertEquals(purchaseOrderDto.getItems().get(3).getUnitCost().doubleValue(), 7550.00000);
		assertEquals(purchaseOrderDto.getItems().get(3).getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().get(4).getCode(), "2.1.1.00542.0030");
		assertEquals(purchaseOrderDto.getItems().get(4).getQuantity(), 24);
		assertEquals(purchaseOrderDto.getItems().get(4).getUnitCost().doubleValue(), 1810.00000);
		assertEquals(purchaseOrderDto.getItems().get(4).getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(5).getCode(), "2.1.1.00542.0019");
		assertEquals(purchaseOrderDto.getItems().get(5).getQuantity(), 40);
		assertEquals(purchaseOrderDto.getItems().get(5).getUnitCost().doubleValue(), 10210.00000);
		assertEquals(purchaseOrderDto.getItems().get(5).getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().get(6).getCode(), "2.1.1.00542.0028");
		assertEquals(purchaseOrderDto.getItems().get(6).getQuantity(), 24);
		assertEquals(purchaseOrderDto.getItems().get(6).getUnitCost().doubleValue(), 8100.00000);
		assertEquals(purchaseOrderDto.getItems().get(6).getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.00542.0029");
		assertEquals(purchaseOrderDto.getItems().get(7).getQuantity(), 24);
		assertEquals(purchaseOrderDto.getItems().get(7).getUnitCost().doubleValue(), 7850.00000);
		assertEquals(purchaseOrderDto.getItems().get(7).getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().get(8).getCode(), "2.1.1.00611.0002");
		assertEquals(purchaseOrderDto.getItems().get(8).getQuantity(), 12);
		assertEquals(purchaseOrderDto.getItems().get(8).getUnitCost().doubleValue(), 2870.00000);
		assertEquals(purchaseOrderDto.getItems().get(8).getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(9).getCode(), "2.1.1.00515.0009");
		assertEquals(purchaseOrderDto.getItems().get(9).getQuantity(), 16);
		assertEquals(purchaseOrderDto.getItems().get(9).getUnitCost().doubleValue(), 2545.00000);
		assertEquals(purchaseOrderDto.getItems().get(9).getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(10).getCode(), "2.1.1.00456.0032");
		assertEquals(purchaseOrderDto.getItems().get(10).getQuantity(), 40);
		assertEquals(purchaseOrderDto.getItems().get(10).getUnitCost().doubleValue(), 1300.00000);
		assertEquals(purchaseOrderDto.getItems().get(10).getMeasureUnit(), "LITROS");
		assertEquals(purchaseOrderDto.getItems().get(11).getCode(), "2.1.1.00707.0008");
		assertEquals(purchaseOrderDto.getItems().get(11).getQuantity(), 24);
		assertEquals(purchaseOrderDto.getItems().get(11).getUnitCost().doubleValue(), 2255.00000);
		assertEquals(purchaseOrderDto.getItems().get(11).getMeasureUnit(), "CAJA");
		assertEquals(purchaseOrderDto.getItems().get(12).getCode(), "2.1.1.00969.0001");
		assertEquals(purchaseOrderDto.getItems().get(12).getQuantity(), 20);
		assertEquals(purchaseOrderDto.getItems().get(12).getUnitCost().doubleValue(), 9370.00000);
		assertEquals(purchaseOrderDto.getItems().get(12).getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getOrderNumber(), 429);

		// Assert Purchase Order total cost equals sum of all items total cost
		assertThat(purchaseOrderDto.getPurchaseOrderTotal().doubleValue()).isEqualTo(purchaseOrderDto.getItems()
				.stream().mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});

		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(), 1492120.00);
	}

	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 454_24")
	@Order(4)
	void pdfToPurchaseOrder454_24() throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-454.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text, admYDespachoId);

		assertEquals(purchaseOrderDto.getOrderNumber(), 454);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.03311.0003"))
				.findFirst().get().getQuantity(), 60);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.03311.0003"))
				.findFirst().get().getMeasureUnit(), "CAJA");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.03311.0003"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.03311.0003")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.03311.0003"))
				.findFirst().get().getUnitCost().doubleValue(), 6909.31000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00704.0008"))
				.findFirst().get().getQuantity(), 60);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00704.0008"))
				.findFirst().get().getMeasureUnit(), "CAJA");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00704.0008"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00704.0008")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00704.0008"))
				.findFirst().get().getUnitCost().doubleValue(), 7898.88000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00521.0031"))
				.findFirst().get().getQuantity(), 60);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00521.0031"))
				.findFirst().get().getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00521.0031"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00521.0031")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00521.0031"))
				.findFirst().get().getUnitCost().doubleValue(), 799.84000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00482.0004"))
				.findFirst().get().getQuantity(), 60);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00482.0004"))
				.findFirst().get().getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00482.0004"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00482.0004")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00482.0004"))
				.findFirst().get().getUnitCost().doubleValue(), 955.45000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00482.0014"))
				.findFirst().get().getQuantity(), 60);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00482.0014"))
				.findFirst().get().getMeasureUnit(), "PAQUETE");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00482.0014"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00482.0014")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00482.0014"))
				.findFirst().get().getUnitCost().doubleValue(), 1108.77000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.04441.0002"))
				.findFirst().get().getQuantity(), 30);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.04441.0002"))
				.findFirst().get().getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.04441.0002"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.04441.0002")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.04441.0002"))
				.findFirst().get().getUnitCost().doubleValue(), 949, 61000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00705.0023"))
				.findFirst().get().getQuantity(), 90);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00705.0023"))
				.findFirst().get().getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00705.0023"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00705.0023")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00705.0023"))
				.findFirst().get().getUnitCost().doubleValue(), 1007.77000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02113.0001"))
				.findFirst().get().getQuantity(), 3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02113.0001"))
				.findFirst().get().getMeasureUnit(), "CAJON");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02113.0001"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02113.0001")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02113.0001"))
				.findFirst().get().getUnitCost().doubleValue(), 10911.99000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02113.0002"))
				.findFirst().get().getQuantity(), 3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02113.0002"))
				.findFirst().get().getMeasureUnit(), "CAJON");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02113.0002"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02113.0002")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02113.0002"))
				.findFirst().get().getUnitCost().doubleValue(), 15503.99000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02125.0003"))
				.findFirst().get().getQuantity(), 15);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02125.0003"))
				.findFirst().get().getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02125.0003"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02125.0003")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02125.0003"))
				.findFirst().get().getUnitCost().doubleValue(), 1593.60000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02116.0003"))
				.findFirst().get().getQuantity(), 1);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02116.0003"))
				.findFirst().get().getMeasureUnit(), "PACK");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02116.0003"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02116.0003")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.02116.0003"))
				.findFirst().get().getUnitCost().doubleValue(), 10919.29000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00435.0001"))
				.findFirst().get().getQuantity(), 12);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00435.0001"))
				.findFirst().get().getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00435.0001"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00435.0001")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00435.0001"))
				.findFirst().get().getUnitCost().doubleValue(), 2420.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00454.0003"))
				.findFirst().get().getQuantity(), 12);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00454.0003"))
				.findFirst().get().getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00454.0003"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00454.0003")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00454.0003"))
				.findFirst().get().getUnitCost().doubleValue(), 2.440, 41000);

		// Assert Purchase Order total cost equals sum of all items total cost
		assertThat(purchaseOrderDto.getPurchaseOrderTotal().doubleValue()).isEqualTo(purchaseOrderDto.getItems()
				.stream().mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});

		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(), 1351918, 75);
	}

	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 493_24")
	@Order(5)
	void pdfToPurchaseOrder493_24() throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-493.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text, admYDespachoId);

		assertEquals(purchaseOrderDto.getOrderNumber(), 493);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00598.0002"))
				.findFirst().get().getQuantity(), 25);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00598.0002"))
				.findFirst().get().getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00598.0002"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00598.0002")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00598.0002"))
				.findFirst().get().getUnitCost().doubleValue(), 1830.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00594.0008"))
				.findFirst().get().getQuantity(), 130);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00594.0008"))
				.findFirst().get().getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00594.0008"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00594.0008")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00594.0008"))
				.findFirst().get().getUnitCost().doubleValue(), 870.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00603.0003"))
				.findFirst().get().getQuantity(), 100);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00603.0003"))
				.findFirst().get().getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00603.0003"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00603.0003")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00603.0003"))
				.findFirst().get().getUnitCost().doubleValue(), 690.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00599.0001"))
				.findFirst().get().getQuantity(), 50);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00599.0001"))
				.findFirst().get().getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00599.0001"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00599.0001")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00599.0001"))
				.findFirst().get().getUnitCost().doubleValue(), 790.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00617.0004"))
				.findFirst().get().getQuantity(), 270);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00617.0004"))
				.findFirst().get().getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00617.0004"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00617.0004")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00617.0004"))
				.findFirst().get().getUnitCost().doubleValue(), 890.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00607.0001"))
				.findFirst().get().getQuantity(), 70);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00607.0001"))
				.findFirst().get().getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00607.0001"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00607.0001")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00607.0001"))
				.findFirst().get().getUnitCost().doubleValue(), 1270.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00600.0001"))
				.findFirst().get().getQuantity(), 185);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00600.0001"))
				.findFirst().get().getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00600.0001"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00600.0001")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00600.0001"))
				.findFirst().get().getUnitCost().doubleValue(), 1590.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00620.0002"))
				.findFirst().get().getQuantity(), 185);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00620.0002"))
				.findFirst().get().getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00620.0002"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00620.0002")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00620.0002"))
				.findFirst().get().getUnitCost().doubleValue(), 2390.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00601.0004"))
				.findFirst().get().getQuantity(), 160);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00601.0004"))
				.findFirst().get().getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00601.0004"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00601.0004")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00601.0004"))
				.findFirst().get().getUnitCost().doubleValue(), 1850.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00483.0002"))
				.findFirst().get().getQuantity(), 32);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00483.0002"))
				.findFirst().get().getMeasureUnit(), "MAPLE");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00483.0002"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00483.0002")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00483.0002"))
				.findFirst().get().getUnitCost().doubleValue(), 4230.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00598.0001"))
				.findFirst().get().getQuantity(), 25);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00598.0001"))
				.findFirst().get().getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00598.0001"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00598.0001")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00598.0001"))
				.findFirst().get().getUnitCost().doubleValue(), 1830.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00610.0001"))
				.findFirst().get().getQuantity(), 15);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00610.0001"))
				.findFirst().get().getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00610.0001"))
				.findFirst().get().getProgramaticCat(), "38.01.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00610.0001")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00610.0001"))
				.findFirst().get().getUnitCost().doubleValue(), 2050.00000);

		// Assert Purchase Order total cost equals sum of all items total cost
		assertThat(purchaseOrderDto.getPurchaseOrderTotal().doubleValue()).isEqualTo(purchaseOrderDto.getItems()
				.stream().mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});

		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(), 1840710.00);
	}

	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 619_24")
	@Order(6)
	void pdfToPurchaseOrder619_24() throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-619.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text, admYDespachoId);
		assertEquals(purchaseOrderDto.getOrderNumber(), 619);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.01033.0031"))
				.findFirst().get().getQuantity(), 30);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.01033.0031"))
				.findFirst().get().getMeasureUnit(), "ROLLO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.01033.0031"))
				.findFirst().get().getProgramaticCat(), "38.06.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.01033.0031")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.01033.0031"))
				.findFirst().get().getUnitCost().doubleValue(), 905.50000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.01454.0011"))
				.findFirst().get().getQuantity(), 30);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.01454.0011"))
				.findFirst().get().getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.01454.0011"))
				.findFirst().get().getProgramaticCat(), "38.06.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.01454.0011")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.01454.0011"))
				.findFirst().get().getUnitCost().doubleValue(), 265.80000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03825.0012"))
				.findFirst().get().getQuantity(), 4);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03825.0012"))
				.findFirst().get().getMeasureUnit(), "ROLLO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03825.0012"))
				.findFirst().get().getProgramaticCat(), "38.06.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03825.0012")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03825.0012"))
				.findFirst().get().getUnitCost().doubleValue(), 46190.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.00657.0012"))
				.findFirst().get().getQuantity(), 30);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.00657.0012"))
				.findFirst().get().getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.00657.0012"))
				.findFirst().get().getProgramaticCat(), "38.06.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.00657.0012")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.00657.0012"))
				.findFirst().get().getUnitCost().doubleValue(), 630.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.06927.0004"))
				.findFirst().get().getQuantity(), 30);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.06927.0004"))
				.findFirst().get().getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.06927.0004"))
				.findFirst().get().getProgramaticCat(), "38.06.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.06927.0004")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.06927.0004"))
				.findFirst().get().getUnitCost().doubleValue(), 1200.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03791.0004"))
				.findFirst().get().getQuantity(), 30);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03791.0004"))
				.findFirst().get().getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03791.0004"))
				.findFirst().get().getProgramaticCat(), "38.06.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03791.0004")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03791.0004"))
				.findFirst().get().getUnitCost().doubleValue(), 510.90000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03792.0002"))
				.findFirst().get().getQuantity(), 30);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03792.0002"))
				.findFirst().get().getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03792.0002"))
				.findFirst().get().getProgramaticCat(), "38.06.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03792.0002")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.03792.0002"))
				.findFirst().get().getUnitCost().doubleValue(), 630.00000);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.05921.0004"))
				.findFirst().get().getQuantity(), 30);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.05921.0004"))
				.findFirst().get().getMeasureUnit(), "UNIDAD");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.05921.0004"))
				.findFirst().get().getProgramaticCat(), "38.06.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.05921.0004")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.9.3.05921.0004"))
				.findFirst().get().getUnitCost().doubleValue(), 870.75000);

		// Assert Purchase Order total cost equals sum of all items total cost
		assertThat(purchaseOrderDto.getPurchaseOrderTotal().doubleValue()).isEqualTo(purchaseOrderDto.getItems()
				.stream().mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});

		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(), 335148.50);
	}
	
	

	@Test
	@DisplayName("ENCONTRAR ORDEN DE COMPRA N 365_24 CON ITEMS")
	@Order(7)
	void findFullPurchaseOrder365_24() throws Exception {
		PurchaseOrderDto purchaseOrderDto = depositControlService.findFullPurchaseOrder(purchaseOrder365Id);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0013");
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00705.0035");
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02113.0002");
		assertEquals(purchaseOrderDto.getOrderNumber(), 365);
		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(), 295600.00);
	}

	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 365_24 A DEPOSITO")
	@Order(8)
	void loadPurchaseOrder365_24ToDepositControl() throws Exception {
		List<PurchaseOrderToDepositReportDto> report = depositControlService
				.loadPurchaseOrderToDepositControl(purchaseOrder365Id, depositAvellanedaId);
		List<DepositControlDto> depositItems = depositControlService.findDepositControlsByDeposit(depositAvellanedaId);

		report.stream().forEach(e -> {
			assertEquals(e.getDepositItemStatus(), "NUEVO");
		});

		assertThat(depositItems.stream().filter(f -> f.getItemCode().equals("2.1.1.00788.0013")).findFirst().get()
				.getQuantity()).isEqualTo(15);
		assertThat(depositItems.stream().filter(f -> f.getItemCode().equals("2.1.1.00705.0035")).findFirst().get()
				.getQuantity()).isEqualTo(8);
		assertThat(depositItems.stream().filter(f -> f.getItemCode().equals("2.1.1.00591.0002")).findFirst().get()
				.getQuantity()).isEqualTo(20);
		assertThat(depositItems.stream().filter(f -> f.getItemCode().equals("2.1.1.00705.0036")).findFirst().get()
				.getQuantity()).isEqualTo(10);
		assertThat(depositItems.stream().filter(f -> f.getItemCode().equals("2.1.1.00511.0008")).findFirst().get()
				.getQuantity()).isEqualTo(10);
		assertThat(depositItems.stream().filter(f -> f.getItemCode().equals("2.1.1.00705.0012")).findFirst().get()
				.getQuantity()).isEqualTo(8);

	}

	@Test
	@DisplayName("CARGAR SUMINISTRO N 551_24")
	@Order(9)
	void loadSupply551_24() throws Exception {
		String text = pdfToStringUtils.pdfToString("sum-551.pdf");
		SupplyDto dto = depositControlService.collectSupplyFromText(text, 2L);
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(2024, 1, 5);
		cal2.set(2024, 1, 7);
		assertThat(dto.getId()).isNotNull();
		assertThat(dto.getDate()).isBetween(cal, cal2);
		assertEquals(dto.getEstimatedTotalCost().doubleValue(), new BigDecimal(43697001.00).doubleValue());
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.03451.0001"))
				.findFirst().get().getProgramaticCat()).isEqualTo("01.10.00");
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.03451.0001"))
				.findFirst().get().getQuantity()).isEqualTo(5500);
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.03451.0001"))
				.findFirst().get().getMeasureUnit()).isEqualTo("KILOGRAMO");
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.03503.0003"))
				.findFirst().get().getProgramaticCat()).isEqualTo("01.10.00");
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.03503.0003"))
				.findFirst().get().getQuantity()).isEqualTo(5000);
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.03503.0003"))
				.findFirst().get().getMeasureUnit()).isEqualTo("CADA-UNO");
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.03411.0002"))
				.findFirst().get().getProgramaticCat()).isEqualTo("01.10.00");
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.03411.0002"))
				.findFirst().get().getQuantity()).isEqualTo(2000);
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.03411.0002"))
				.findFirst().get().getMeasureUnit()).isEqualTo("CADA-UNO");
		assertThat(dto.getSupplyItems().get(2).getCode()).isEqualTo("5.1.4.03411.0002");
		assertThat(dto.getSupplyItems().get(2).getQuantity()).isEqualTo(2000);
		assertEquals(dto.getSupplyItems().get(2).getMeasureUnit(), "CADA-UNO");
		assertThat(dto.getSupplyItems().get(3).getCode()).isEqualTo("5.1.4.03411.0001");
		assertThat(dto.getSupplyItems().get(3).getQuantity()).isEqualTo(1000);
		assertEquals(dto.getSupplyItems().get(3).getMeasureUnit(), "CADA-UNO");
		assertThat(dto.getSupplyItems().get(4).getCode()).isEqualTo("5.1.4.03410.0001");
		assertThat(dto.getSupplyItems().get(4).getQuantity()).isEqualTo(4300);
		assertEquals(dto.getSupplyItems().get(4).getMeasureUnit(), "CADA-UNO");
		assertThat(dto.getSupplyItems().get(5).getCode()).isEqualTo("5.1.4.03413.0001");
		assertThat(dto.getSupplyItems().get(5).getQuantity()).isEqualTo(4663);
		assertEquals(dto.getSupplyItems().get(5).getMeasureUnit(), "CADA-UNO");
		assertThat(dto.getSupplyItems().get(6).getCode()).isEqualTo("5.1.4.03412.0002");
		assertThat(dto.getSupplyItems().get(6).getQuantity()).isEqualTo(2000);
		assertEquals(dto.getSupplyItems().get(6).getMeasureUnit(), "CADA-UNO");
		assertThat(dto.getSupplyItems().get(7).getCode()).isEqualTo("5.1.4.03501.0001");
		assertThat(dto.getSupplyItems().get(7).getQuantity()).isEqualTo(5000);
		assertEquals(dto.getSupplyItems().get(7).getMeasureUnit(), "CADA-UNO");
		assertThat(dto.getSupplyItems().get(8).getCode()).isEqualTo("5.1.4.03453.0001");
		assertThat(dto.getSupplyItems().get(8).getQuantity()).isEqualTo(7000);
		assertEquals(dto.getSupplyItems().get(8).getMeasureUnit(), "CADA-UNO");
		assertThat(dto.getSupplyItems().get(9).getCode()).isEqualTo("5.1.4.03560.0002");
		assertThat(dto.getSupplyItems().get(9).getQuantity()).isEqualTo(1000);
		assertEquals(dto.getSupplyItems().get(9).getMeasureUnit(), "CADA-UNO");

		// Assert Supply total cost equals sum of all items total cost
		assertThat(dto.getEstimatedTotalCost().doubleValue()).isEqualTo(dto.getSupplyItems().stream()
				.mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item unit quantity equals item total cost
		ListIterator<SupplyItemDto> it = dto.getSupplyItems().listIterator();
		it.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});

	}

	@Test
	@DisplayName("CARGAR SUMINISTRO N 223_23")
	@Order(10)
	void loadSupply223_23() throws Exception {
		String text = pdfToStringUtils.pdfToString("sum-223.pdf");
		SupplyDto dto = depositControlService.collectSupplyFromText(text, 2L);
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(2023, 0, 9);
		cal2.set(2023, 0, 11);
		assertThat(dto.getId()).isNotNull();
		assertThat(dto.getDate()).isBetween(cal, cal2);
		assertEquals(dto.getEstimatedTotalCost().doubleValue(), new BigDecimal(1305266.71).doubleValue());

		assertThat(dto.getSupplyItems().get(0).getCode()).isEqualTo("3.3.1.07030.0001");
		assertThat(dto.getSupplyItems().get(0).getProgramaticCat()).isEqualTo("39.00.00");
		System.err.println("sum 223 quantity:" + dto.getSupplyItems().get(0).getQuantity());
		assertThat(dto.getSupplyItems().get(0).getQuantity()).isEqualTo(1);
		assertThat(dto.getSupplyItems().get(0).getMeasureUnit()).isEqualTo("UNIDAD");

		assertThat(dto.getSupplyItems().get(0).getUnitCost().doubleValue())
				.isEqualTo(new BigDecimal(1305266.71000).doubleValue());

		assertThat(dto.getSupplyItems().get(0).getTotalEstimatedCost().doubleValue())
				.isEqualTo(new BigDecimal(1305266.71).doubleValue());

		ListIterator<SupplyItemDto> it = dto.getSupplyItems().listIterator();

		// Assert Supply total cost equals sum of all items total cost
		assertThat(dto.getEstimatedTotalCost().doubleValue()).isEqualTo(dto.getSupplyItems().stream()
				.mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item unit quantity equals item total cost
		it.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});

	}

	@Test
	@DisplayName("CARGAR SUMINISTRO N 177_24")
	@Order(11)
	void loadSupply177_24() throws Exception {
		String text = pdfToStringUtils.pdfToString("sum-177.pdf");
		SupplyDto dto = depositControlService.collectSupplyFromText(text, 2L);
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(2024, 0, 10);
		cal2.set(2024, 0, 12);
		assertThat(dto.getId()).isNotNull();
		assertEquals(dto.getSupplyNumber(), 177);
		assertThat(dto.getDate()).isBetween(cal, cal2);
		assertEquals(dto.getEstimatedTotalCost().doubleValue(), new BigDecimal(27104000.00).doubleValue());
		assertThat(dto.getSupplyItems().get(0).getCode()).isEqualTo("5.1.4.07522.0001");
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.07522.0001")).findFirst().get()
				.getQuantity()).isEqualTo(50);
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.07522.0001")).findFirst().get()
				.getUnitCost().doubleValue(), 12705.00000);
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.07522.0001")).findFirst().get()
				.getMeasureUnit()).isEqualTo("UNIDAD");
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.07522.0001")).findFirst().get()
				.getItemDetail().length()).isGreaterThan(3);
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.07522.0002")).findFirst().get()
				.getCode()).isEqualTo("5.1.4.07522.0002");
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.07522.0002")).findFirst().get()
				.getQuantity()).isEqualTo(100);
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.07522.0002")).findFirst().get()
				.getUnitCost().doubleValue(), 12705.00000);
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.07522.0002")).findFirst().get()
				.getMeasureUnit()).isEqualTo("UNIDAD");
		assertThat(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("5.1.4.07522.0002")).findFirst().get()
				.getItemDetail().length()).isGreaterThan(3);
		assertThat(dto.getSupplyItems().get(2).getCode()).isEqualTo("5.1.4.07522.0003");
		assertThat(dto.getSupplyItems().get(2).getQuantity()).isEqualTo(150);
		assertEquals(dto.getSupplyItems().get(2).getUnitCost().doubleValue(), 12705.00000);
		assertThat(dto.getSupplyItems().get(2).getMeasureUnit()).isEqualTo("UNIDAD");
		assertThat(dto.getSupplyItems().get(2).getItemDetail().length()).isGreaterThan(3);
		assertThat(dto.getSupplyItems().get(3).getCode()).isEqualTo("5.1.4.07522.0004");
		assertThat(dto.getSupplyItems().get(3).getQuantity()).isEqualTo(150);
		assertEquals(dto.getSupplyItems().get(3).getUnitCost().doubleValue(), 12705.00000);
		assertThat(dto.getSupplyItems().get(3).getMeasureUnit()).isEqualTo("UNIDAD");
		assertThat(dto.getSupplyItems().get(3).getItemDetail().length()).isGreaterThan(3);
		assertThat(dto.getSupplyItems().get(4).getCode()).isEqualTo("5.1.4.07522.0005");
		assertThat(dto.getSupplyItems().get(4).getQuantity()).isEqualTo(150);
		assertEquals(dto.getSupplyItems().get(4).getUnitCost().doubleValue(), 12705.00000);
		assertThat(dto.getSupplyItems().get(4).getMeasureUnit()).isEqualTo("UNIDAD");
		assertThat(dto.getSupplyItems().get(4).getItemDetail().length()).isGreaterThan(3);
		assertThat(dto.getSupplyItems().get(5).getCode()).isEqualTo("5.1.4.07522.0006");
		assertThat(dto.getSupplyItems().get(5).getQuantity()).isEqualTo(200);
		assertEquals(dto.getSupplyItems().get(5).getUnitCost().doubleValue(), 12705.00000);
		assertThat(dto.getSupplyItems().get(5).getMeasureUnit()).isEqualTo("UNIDAD");
		assertThat(dto.getSupplyItems().get(5).getItemDetail().length()).isGreaterThan(3);
		assertThat(dto.getSupplyItems().get(6).getCode()).isEqualTo("5.1.4.07522.0007");
		assertThat(dto.getSupplyItems().get(6).getQuantity()).isEqualTo(150);
		assertEquals(dto.getSupplyItems().get(6).getUnitCost().doubleValue(), 12705.00000);
		assertThat(dto.getSupplyItems().get(6).getMeasureUnit()).isEqualTo("UNIDAD");
		assertThat(dto.getSupplyItems().get(6).getItemDetail().length()).isGreaterThan(3);
		assertThat(dto.getSupplyItems().get(7).getCode()).isEqualTo("5.1.4.07522.0008");
		assertThat(dto.getSupplyItems().get(7).getQuantity()).isEqualTo(200);
		assertEquals(dto.getSupplyItems().get(7).getUnitCost().doubleValue(), 12705.00000);
		assertThat(dto.getSupplyItems().get(7).getMeasureUnit()).isEqualTo("UNIDAD");
		assertThat(dto.getSupplyItems().get(7).getItemDetail().length()).isGreaterThan(3);
		assertThat(dto.getSupplyItems().get(8).getCode()).isEqualTo("5.1.4.07522.0009");
		assertThat(dto.getSupplyItems().get(8).getQuantity()).isEqualTo(150);
		assertEquals(dto.getSupplyItems().get(8).getUnitCost().doubleValue(), 12705.00000);
		assertThat(dto.getSupplyItems().get(8).getMeasureUnit()).isEqualTo("UNIDAD");
		assertThat(dto.getSupplyItems().get(8).getItemDetail().length()).isGreaterThan(3);
		ListIterator<SupplyItemDto> it = dto.getSupplyItems().listIterator();

		// Assert Supply total cost equals sum of all items total cost
		assertThat(dto.getEstimatedTotalCost().doubleValue()).isEqualTo(dto.getSupplyItems().stream()
				.mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item unit quantity equals item total cost
		it.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());

		});

	}

	@Test
	@DisplayName("CARGAR SUMINISTRO N 1043_23")
	@Order(12)
	void loadSupply1043_23() throws Exception {
		String text = pdfToStringUtils.pdfToString("sum-1043.pdf");
		SupplyDto dto = depositControlService.collectSupplyFromText(text, 2L);
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(2023, 2, 7);
		cal2.set(2023, 2, 9);
		assertThat(dto.getId()).isNotNull();
		assertEquals(dto.getSupplyNumber(), 1043);
		assertThat(dto.getDate()).isBetween(cal, cal2);
		assertEquals(dto.getEstimatedTotalCost().doubleValue(), new BigDecimal(5500000.00).doubleValue());
		assertThat(dto.getSupplyItems().get(0).getCode()).isEqualTo("5.1.4.07776.0001");
		assertEquals(dto.getSupplyItems().get(0).getQuantity(), 5000);
		assertEquals(dto.getSupplyItems().get(0).getProgramaticCat(), "32.06.00");
		assertEquals(dto.getSupplyItems().get(0).getMeasureUnit(), "UNIDAD");
		assertEquals(dto.getSupplyItems().get(0).getUnitCost().doubleValue(), 1100.00000);

		// Assert Supply total cost equals sum of all items total cost
		assertThat(dto.getEstimatedTotalCost().doubleValue()).isEqualTo(dto.getSupplyItems().stream()
				.mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item unit quantity equals item total cost
		ListIterator<SupplyItemDto> it = dto.getSupplyItems().listIterator();
		it.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
	}

	@Test
	@DisplayName("CARGAR SUMINISTRO N 100_24")
	@Order(13)
	void loadSupply100_24() throws Exception {
		String text = pdfToStringUtils.pdfToString("sum-100.pdf");
		SupplyDto dto = depositControlService.collectSupplyFromText(text, 2L);
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(2024, 0, 7);
		cal2.set(2024, 0, 9);
		assertThat(dto.getId()).isNotNull();
		assertEquals(dto.getSupplyNumber(), 100);
		assertThat(dto.getDate()).isBetween(cal, cal2);
		assertEquals(dto.getEstimatedTotalCost().doubleValue(), new BigDecimal(231000.00).doubleValue());

		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.3.4.05032.0002")).findFirst().get()
				.getProgramaticCat(), "35.04.00");
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.3.4.05032.0002")).findFirst().get()
				.getQuantity(), 30);
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.3.4.05032.0002")).findFirst().get()
				.getMeasureUnit(), "CAJA");
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.3.4.05032.0002")).findFirst().get()
				.getUnitCost().doubleValue(), 7700.00000);
		// Assert Supply total cost equals sum of all items total cost
		assertThat(dto.getEstimatedTotalCost().doubleValue()).isEqualTo(dto.getSupplyItems().stream()
				.mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item unit quantity equals item total cost
		ListIterator<SupplyItemDto> it = dto.getSupplyItems().listIterator();
		it.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
	}

	@Test
	@DisplayName("CARGAR SUMINISTRO N 525_24")
	@Order(14)
	void loadSupply525_24() throws Exception {
		String text = pdfToStringUtils.pdfToString("sum-525.pdf");
		SupplyDto dto = depositControlService.collectSupplyFromText(text, 2L);
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(2024, 1, 4);
		cal2.set(2024, 1, 6);
		assertThat(dto.getId()).isNotNull();
		assertEquals(dto.getSupplyNumber(), 525);
		assertThat(dto.getDate()).isBetween(cal, cal2);
		assertEquals(dto.getEstimatedTotalCost().doubleValue(), new BigDecimal(38423400.00).doubleValue());

		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.02859.0011")).findFirst().get()
				.getProgramaticCat(), "01.01.00");
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.02859.0011")).findFirst().get()
				.getQuantity(), 340);
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.02859.0011")).findFirst().get()
				.getMeasureUnit(), "CADA-UNO");
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.02859.0011")).findFirst().get()
				.getUnitCost().doubleValue(), 12500.00000);

		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.00826.0013")).findFirst().get()
				.getProgramaticCat(), "01.01.00");
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.00826.0013")).findFirst().get()
				.getQuantity(), 340);
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.00826.0013")).findFirst().get()
				.getMeasureUnit(), "CADA-UNO");
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.00826.0013")).findFirst().get()
				.getUnitCost().doubleValue(), 34800.00000);

		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.00830.0017")).findFirst().get()
				.getProgramaticCat(), "01.01.00");
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.00830.0017")).findFirst().get()
				.getQuantity(), 340);
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.00830.0017")).findFirst().get()
				.getMeasureUnit(), "PAR");
		assertEquals(dto.getSupplyItems().stream().filter(f -> f.getCode().equals("2.2.2.00830.0017")).findFirst().get()
				.getUnitCost().doubleValue(), 65710.00000);
		// Assert Supply total cost equals sum of all items total cost
		assertThat(dto.getEstimatedTotalCost().doubleValue()).isEqualTo(dto.getSupplyItems().stream()
				.mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		// Assert item unit cost * item unit quantity equals item total cost
		ListIterator<SupplyItemDto> it = dto.getSupplyItems().listIterator();
		it.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
	}

	@Test
	@DisplayName("CREAR BOLSON NAVIDAD")
	@Order(15)
	void createBigbagNavidad() throws Exception {
		Deposit deposit = depositRepository.findById(depositAvellanedaId).get();
		List<DepositControl> depositItems = depositControlRepository.findAllByDeposit(deposit).stream().map(m -> {
			if (m.getItemCode().equals("2.1.1.00705.0035")) {
				return m;
			}
			m.setQuantity(20);
			return m;
		}).toList();
		depositControlRepository.saveAll(depositItems);
		// List<DepositControl> updatedControls =
		// depositControlRepository.saveAll(DepositControlMapper.INSTANCE.dtosToDepositControls(udpatedItems));

		List<BigBagItemDto> bigBagItemDtos = depositItems.stream().map(depoItem -> {
			BigBagItemDto dto = new BigBagItemDto();
			dto.setDepositControlId(depoItem.getId());
			return dto;
		}).toList();
		BigBagDto bigBagDto = new BigBagDto();
		bigBagDto.setName("Navidad");
		bigBagDto.setItems(bigBagItemDtos);
		BigBagDto savedbigBagDto = depositControlService.createBigBag(bigBagDto, admYDespachoId);
		BigBag returnedbigBag = bigBagRepository.save(BigBagMapper.INSTANCE.dtoToBigBah(savedbigBagDto));
		bigBagItemRepository.findByBigBag(returnedbigBag).stream().forEach(e -> {
			assertEquals(e.getQuantity(), 1);
		});
		assertEquals(depositControlService.getTotalBigBagQuantityAvailable(savedbigBagDto.getId(), depositAvellanedaId),
				8);
	}

	@Test
	@DisplayName("GENERAR LISTA DE COMPARACION: EXCEL ITEM <-> ITEM DE ORDEN DE COMPRA")
	@Order(16)
	void excelToDepositControl() throws Exception {
		String fileLocation = "control_excel3.xls";
		List<ExcelItemDto> excelCandidates = excelToListUtils.excelDataToDeposit(fileLocation,admYDespachoId);
		
		//  excelCandidates.forEach(e -> System.out.println("[" + e.getExcelItemId() +
		 // "] " + "[" + e.getItemDescription() + "] " + "[" + e.getItemMeasureUnit() +
		// "] " + "[" + e.getQuantity() + "]"));
		 
		List<DepositItemComparatorDto> comparators = depositControlService
				.getExcelToPuchaseOrderComparator(excelCandidates, admYDespachoId);
		List<ExcelItem> dbExcelItems = excelItemRepository.findAll();
		// comparators.forEach(e -> System.out.println("Candidates: " +
		 //e.getExcelItemDto().getExcelItemId()+" " +
		// e.getExcelItemDto().getItemDescription()));
		/*comparators.stream().forEach(e -> {
			List<PurchaseOrderItemCandidateDto> dtos = e.getPurchaseOrderItemCandidateDtos();
			dtos.forEach(item -> System.out
					.println("orderID: " + item.getOrderId() + "description: " + item.getItemDetail()));
		});*/
	}
	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 360_24")
	@Order(17)
	void pdfToPurchaseOrder360_24() throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-360.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text, admYDespachoId);

		assertEquals(purchaseOrderDto.getOrderNumber(), 360);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("5.1.4.05125.0002"))
				.findFirst().get().getQuantity(),3000);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("5.1.4.05125.0002"))
				.findFirst().get().getMeasureUnit(), "CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("5.1.4.05125.0002"))
				.findFirst().get().getProgramaticCat(), "32.02.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("5.1.4.05125.0002")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("5.1.4.05125.0002"))
				.findFirst().get().getUnitCost().doubleValue(),291.36);

		

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
	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 572_24")
	@Order(18)
	void pdfToPurchaseOrder572_24()throws Exception{
		String text = pdfToStringUtils.pdfToString("oc-572.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text, admYDespachoId);

		assertEquals(purchaseOrderDto.getOrderNumber(), 572);

		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00481.0010"))
				.findFirst().get().getQuantity(),587);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00481.0010"))
				.findFirst().get().getMeasureUnit(), "KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00481.0010"))
				.findFirst().get().getProgramaticCat(), "1.09.00");
		assertThat(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00481.0010")).findFirst()
				.get().getItemDetail().length()).isGreaterThan(3);
		assertEquals(purchaseOrderDto.getItems().stream().filter(f -> f.getCode().equals("2.1.1.00481.0010"))
				.findFirst().get().getUnitCost().doubleValue(),1146.11000);

		

		 //Assert Purchase Order total cost equals sum of all items total cost
		assertThat(purchaseOrderDto.getPurchaseOrderTotal().doubleValue()).isEqualTo(purchaseOrderDto.getItems()
				.stream().mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());

		 //Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item -> {
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});

		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(),  2017632.97);
	}
	
}
