package com.lord.small_box;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNotNull;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

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
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.PurchaseOrderItemDto;
import com.lord.small_box.dtos.PurchaseOrderToDepositReportDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.dtos.SupplyItemDto;
import com.lord.small_box.mappers.PurchaseOrderMapper;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.models.SupplyItem;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.repositories.PurchaseOrderItemRepository;
import com.lord.small_box.repositories.PurchaseOrderRepository;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.text_analisys.TextToPurchaseOrder;
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
		
			Organization org1= new Organization();
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
			organizationService.save(org3);
			organizationService.save(org4);
			organizationService.save(org5);
	}
	
	
	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 365_24")
	@Order(1)
	void pdfToPurchaseOrder365_24()throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-365.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text,2L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0013");
		assertEquals(purchaseOrderDto.getItems().get(0).getQuantity(), 15);
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00705.0035");
		assertEquals(purchaseOrderDto.getItems().get(1).getQuantity(), 8);
		assertEquals(purchaseOrderDto.getItems().get(2).getCode(), "2.1.1.00592.0001");
		assertEquals(purchaseOrderDto.getItems().get(2).getQuantity(), 15);
		assertEquals(purchaseOrderDto.getItems().get(3).getCode(), "2.1.1.00591.0002");
		assertEquals(purchaseOrderDto.getItems().get(3).getQuantity(), 20);
		assertEquals(purchaseOrderDto.getItems().get(4).getCode(), "2.1.1.00705.0036");
		assertEquals(purchaseOrderDto.getItems().get(4).getQuantity(), 10);
		assertEquals(purchaseOrderDto.getItems().get(5).getCode(), "2.1.1.00511.0008");
		assertEquals(purchaseOrderDto.getItems().get(5).getQuantity(), 10);
		assertEquals(purchaseOrderDto.getItems().get(6).getCode(), "2.1.1.00705.0012");
		assertEquals(purchaseOrderDto.getItems().get(6).getQuantity(), 8);
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02113.0002");
		assertEquals(purchaseOrderDto.getItems().get(7).getQuantity(), 1);
		assertEquals(purchaseOrderDto.getOrderNumber(), 365);
		
		//Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item ->{
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
		
	assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(),295600.00);
	}
	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 340_24")
	@Order(2)
	void pdfToPurchaseOrder340_24()throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-340.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text,2L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0091");
		assertEquals(purchaseOrderDto.getItems().get(0).getQuantity(), 350);
		assertEquals(purchaseOrderDto.getItems().get(0).getUnitCost().doubleValue(), 1580.00000);
		assertEquals(purchaseOrderDto.getItems().get(0).getMeasureUnit(),"UNIDAD");
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00439.0001");
		assertEquals(purchaseOrderDto.getItems().get(1).getQuantity(), 50);
		assertEquals(purchaseOrderDto.getItems().get(1).getUnitCost().doubleValue(), 4500.00000);
		assertEquals(purchaseOrderDto.getItems().get(1).getMeasureUnit(),"UNIDAD");
		assertEquals(purchaseOrderDto.getItems().get(2).getCode(), "2.1.1.00520.0003");
		assertEquals(purchaseOrderDto.getItems().get(2).getQuantity(), 90);
		assertEquals(purchaseOrderDto.getItems().get(2).getUnitCost().doubleValue(), 600.00000);
		assertEquals(purchaseOrderDto.getItems().get(2).getMeasureUnit(),"CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(3).getCode(), "2.1.1.00455.0007");
		assertEquals(purchaseOrderDto.getItems().get(3).getQuantity(), 6);
		assertEquals(purchaseOrderDto.getItems().get(3).getUnitCost().doubleValue(), 3100.00000);
		assertEquals(purchaseOrderDto.getItems().get(3).getMeasureUnit(),"KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().get(4).getCode(), "2.1.1.00489.0066");
		assertEquals(purchaseOrderDto.getItems().get(4).getQuantity(), 3);
		assertEquals(purchaseOrderDto.getItems().get(4).getUnitCost().doubleValue(), 3100.00000);
		assertEquals(purchaseOrderDto.getItems().get(4).getMeasureUnit(),"BOLSA");
		assertEquals(purchaseOrderDto.getItems().get(5).getCode(), "2.1.1.00489.0096");
		assertEquals(purchaseOrderDto.getItems().get(5).getQuantity(), 3);
		assertEquals(purchaseOrderDto.getItems().get(5).getUnitCost().doubleValue(), 6900.00000);
		assertEquals(purchaseOrderDto.getItems().get(5).getMeasureUnit(),"BOLSA");
		assertEquals(purchaseOrderDto.getItems().get(6).getCode(), "2.1.1.00489.0067");
		assertEquals(purchaseOrderDto.getItems().get(6).getQuantity(), 3);
		assertEquals(purchaseOrderDto.getItems().get(6).getUnitCost().doubleValue(), 5100.00000);
		assertEquals(purchaseOrderDto.getItems().get(6).getMeasureUnit(),"BOLSA");
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02444.0003");
		assertEquals(purchaseOrderDto.getItems().get(7).getQuantity(), 7);
		assertEquals(purchaseOrderDto.getItems().get(7).getUnitCost().doubleValue(), 1400.00000);
		assertEquals(purchaseOrderDto.getItems().get(7).getMeasureUnit(),"UNIDAD");
		assertEquals(purchaseOrderDto.getOrderNumber(), 340);
		
		//Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item ->{
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
		
	assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(), 905700.00);
	}
	@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 429_24")
	@Order(3)
	void pdfToPurchaseOrder429_24()throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-429.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text,2L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.02610.0001");
		assertEquals(purchaseOrderDto.getItems().get(0).getQuantity(), 32);
		assertEquals(purchaseOrderDto.getItems().get(0).getUnitCost().doubleValue(), 1550.00000);
		assertEquals(purchaseOrderDto.getItems().get(0).getMeasureUnit(),"CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.02429.0001");
		assertEquals(purchaseOrderDto.getItems().get(1).getQuantity(), 20);
		assertEquals(purchaseOrderDto.getItems().get(1).getUnitCost().doubleValue(), 1490.00000);
		assertEquals(purchaseOrderDto.getItems().get(1).getMeasureUnit(),"UNIDAD");
		assertEquals(purchaseOrderDto.getItems().get(2).getCode(), "2.1.1.02430.0003");
		assertEquals(purchaseOrderDto.getItems().get(2).getQuantity(), 20);
		assertEquals(purchaseOrderDto.getItems().get(2).getUnitCost().doubleValue(), 1410.00000);
		assertEquals(purchaseOrderDto.getItems().get(2).getMeasureUnit(),"CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(3).getCode(), "2.1.1.00542.0023");
		assertEquals(purchaseOrderDto.getItems().get(3).getQuantity(), 24);
		assertEquals(purchaseOrderDto.getItems().get(3).getUnitCost().doubleValue(), 7550.00000);
		assertEquals(purchaseOrderDto.getItems().get(3).getMeasureUnit(),"KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().get(4).getCode(), "2.1.1.00542.0030");
		assertEquals(purchaseOrderDto.getItems().get(4).getQuantity(), 24);
		assertEquals(purchaseOrderDto.getItems().get(4).getUnitCost().doubleValue(), 1810.00000);
		assertEquals(purchaseOrderDto.getItems().get(4).getMeasureUnit(),"CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(5).getCode(), "2.1.1.00542.0019");
		assertEquals(purchaseOrderDto.getItems().get(5).getQuantity(), 40);
		assertEquals(purchaseOrderDto.getItems().get(5).getUnitCost().doubleValue(), 10210.00000);
		assertEquals(purchaseOrderDto.getItems().get(5).getMeasureUnit(),"KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().get(6).getCode(), "2.1.1.00542.0028");
		assertEquals(purchaseOrderDto.getItems().get(6).getQuantity(), 24);
		assertEquals(purchaseOrderDto.getItems().get(6).getUnitCost().doubleValue(), 8100.00000);
		assertEquals(purchaseOrderDto.getItems().get(6).getMeasureUnit(),"KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.00542.0029");
		assertEquals(purchaseOrderDto.getItems().get(7).getQuantity(), 24);
		assertEquals(purchaseOrderDto.getItems().get(7).getUnitCost().doubleValue(), 7850.00000);
		assertEquals(purchaseOrderDto.getItems().get(7).getMeasureUnit(),"KILOGRAMO");
		assertEquals(purchaseOrderDto.getItems().get(8).getCode(), "2.1.1.00611.0002");
		assertEquals(purchaseOrderDto.getItems().get(8).getQuantity(), 12);
		assertEquals(purchaseOrderDto.getItems().get(8).getUnitCost().doubleValue(), 2870.00000);
		assertEquals(purchaseOrderDto.getItems().get(8).getMeasureUnit(),"CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(9).getCode(), "2.1.1.00515.0009");
		assertEquals(purchaseOrderDto.getItems().get(9).getQuantity(), 16);
		assertEquals(purchaseOrderDto.getItems().get(9).getUnitCost().doubleValue(), 2545.00000);
		assertEquals(purchaseOrderDto.getItems().get(9).getMeasureUnit(),"CADA-UNO");
		assertEquals(purchaseOrderDto.getItems().get(10).getCode(), "2.1.1.00456.0032");
		assertEquals(purchaseOrderDto.getItems().get(10).getQuantity(), 40);
		assertEquals(purchaseOrderDto.getItems().get(10).getUnitCost().doubleValue(), 1300.00000);
		assertEquals(purchaseOrderDto.getItems().get(10).getMeasureUnit(),"LITROS");
		assertEquals(purchaseOrderDto.getItems().get(11).getCode(), "2.1.1.00707.0008");
		assertEquals(purchaseOrderDto.getItems().get(11).getQuantity(), 24);
		assertEquals(purchaseOrderDto.getItems().get(11).getUnitCost().doubleValue(), 2255.00000);
		assertEquals(purchaseOrderDto.getItems().get(11).getMeasureUnit(),"CAJA");
		assertEquals(purchaseOrderDto.getItems().get(12).getCode(), "2.1.1.00969.0001");
		assertEquals(purchaseOrderDto.getItems().get(12).getQuantity(), 20);
		assertEquals(purchaseOrderDto.getItems().get(12).getUnitCost().doubleValue(), 9370.00000);
		assertEquals(purchaseOrderDto.getItems().get(12).getMeasureUnit(),"KILOGRAMO");
		assertEquals(purchaseOrderDto.getOrderNumber(), 429);
		
		//Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item ->{
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
		
	assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(), 1492120.00);
	}
	//@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 454_24")
	@Order(4)
	void pdfToPurchaseOrder454_24()throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-365.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text,2L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0013");
		assertEquals(purchaseOrderDto.getItems().get(0).getQuantity(), 15);
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00705.0035");
		assertEquals(purchaseOrderDto.getItems().get(1).getQuantity(), 8);
		assertEquals(purchaseOrderDto.getItems().get(2).getCode(), "2.1.1.00592.0001");
		assertEquals(purchaseOrderDto.getItems().get(2).getQuantity(), 15);
		assertEquals(purchaseOrderDto.getItems().get(3).getCode(), "2.1.1.00591.0002");
		assertEquals(purchaseOrderDto.getItems().get(3).getQuantity(), 20);
		assertEquals(purchaseOrderDto.getItems().get(4).getCode(), "2.1.1.00705.0036");
		assertEquals(purchaseOrderDto.getItems().get(4).getQuantity(), 10);
		assertEquals(purchaseOrderDto.getItems().get(5).getCode(), "2.1.1.00511.0008");
		assertEquals(purchaseOrderDto.getItems().get(5).getQuantity(), 10);
		assertEquals(purchaseOrderDto.getItems().get(6).getCode(), "2.1.1.00705.0012");
		assertEquals(purchaseOrderDto.getItems().get(6).getQuantity(), 8);
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02113.0002");
		assertEquals(purchaseOrderDto.getItems().get(7).getQuantity(), 1);
		assertEquals(purchaseOrderDto.getOrderNumber(), 365);
		
		//Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item ->{
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
		
	assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(),295600.00);
	}
	//@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 493_24")
	@Order(5)
	void pdfToPurchaseOrder493_24()throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-365.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text,2L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0013");
		assertEquals(purchaseOrderDto.getItems().get(0).getQuantity(), 15);
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00705.0035");
		assertEquals(purchaseOrderDto.getItems().get(1).getQuantity(), 8);
		assertEquals(purchaseOrderDto.getItems().get(2).getCode(), "2.1.1.00592.0001");
		assertEquals(purchaseOrderDto.getItems().get(2).getQuantity(), 15);
		assertEquals(purchaseOrderDto.getItems().get(3).getCode(), "2.1.1.00591.0002");
		assertEquals(purchaseOrderDto.getItems().get(3).getQuantity(), 20);
		assertEquals(purchaseOrderDto.getItems().get(4).getCode(), "2.1.1.00705.0036");
		assertEquals(purchaseOrderDto.getItems().get(4).getQuantity(), 10);
		assertEquals(purchaseOrderDto.getItems().get(5).getCode(), "2.1.1.00511.0008");
		assertEquals(purchaseOrderDto.getItems().get(5).getQuantity(), 10);
		assertEquals(purchaseOrderDto.getItems().get(6).getCode(), "2.1.1.00705.0012");
		assertEquals(purchaseOrderDto.getItems().get(6).getQuantity(), 8);
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02113.0002");
		assertEquals(purchaseOrderDto.getItems().get(7).getQuantity(), 1);
		assertEquals(purchaseOrderDto.getOrderNumber(), 365);
		
		//Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item ->{
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
		
	assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(),295600.00);
	}
	//@Test
	@DisplayName("CARGAR ORDEN DE COMPRA N 619_24")
	@Order(6)
	void pdfToPurchaseOrder619_24()throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-365.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text,2L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0013");
		assertEquals(purchaseOrderDto.getItems().get(0).getQuantity(), 15);
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00705.0035");
		assertEquals(purchaseOrderDto.getItems().get(1).getQuantity(), 8);
		assertEquals(purchaseOrderDto.getItems().get(2).getCode(), "2.1.1.00592.0001");
		assertEquals(purchaseOrderDto.getItems().get(2).getQuantity(), 15);
		assertEquals(purchaseOrderDto.getItems().get(3).getCode(), "2.1.1.00591.0002");
		assertEquals(purchaseOrderDto.getItems().get(3).getQuantity(), 20);
		assertEquals(purchaseOrderDto.getItems().get(4).getCode(), "2.1.1.00705.0036");
		assertEquals(purchaseOrderDto.getItems().get(4).getQuantity(), 10);
		assertEquals(purchaseOrderDto.getItems().get(5).getCode(), "2.1.1.00511.0008");
		assertEquals(purchaseOrderDto.getItems().get(5).getQuantity(), 10);
		assertEquals(purchaseOrderDto.getItems().get(6).getCode(), "2.1.1.00705.0012");
		assertEquals(purchaseOrderDto.getItems().get(6).getQuantity(), 8);
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02113.0002");
		assertEquals(purchaseOrderDto.getItems().get(7).getQuantity(), 1);
		assertEquals(purchaseOrderDto.getOrderNumber(), 365);
		
		//Assert item unit cost * item quantity equals item total cost
		ListIterator<PurchaseOrderItemDto> items = purchaseOrderDto.getItems().listIterator();
		items.forEachRemaining(item ->{
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
		
	assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(),295600.00);
	}
	
	@Test
	@DisplayName("ENCONTRAR ORDEN DE COMPRA N 365 CON ITEMS")
	@Order(7)
	void findFullPurchaseOrder()throws Exception{
		PurchaseOrderDto purchaseOrderDto = depositControlService.findFullPurchaseOrder(1L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0013");
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00705.0035");
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02113.0002");
		assertEquals(purchaseOrderDto.getOrderNumber(), 365);
		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(),295600.00);
	}
	
	 @Test
	 @DisplayName("CARGAR ORDEN DE COMPRA N 365 A DEPOSITO")
	 @Order(8)
	void loadPurchaseOrderToDepositControl()throws Exception{
		 List<PurchaseOrderToDepositReportDto> result = depositControlService.loadPurchaseOrderToDepositControl(1L,1L);
		 result.forEach(e -> System.out.println("Report result: " +e));
	 }
	 
	@Test
	 @DisplayName("CARGAR SUMINISTRO N 551")
	@Order(9)
	void loadSupply551()throws Exception{
		String text = pdfToStringUtils.pdfToString("sum-551.pdf");
		System.err.println(text);
		SupplyDto dto = depositControlService.collectSupplyFromText(text,2L);
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(2024, 1, 5);
		cal2.set(2024, 1, 7);
		assertThat(dto.getId()).isNotNull();
		assertThat(dto.getDate()).isBetween(cal,cal2);
		assertEquals(dto.getEstimatedTotalCost().doubleValue(),new BigDecimal(43697001.00).doubleValue());
		
		//Assert Purchase order total cost equals sum of all items total cost
		assertThat(dto.getEstimatedTotalCost().doubleValue()).isEqualTo(dto.getSupplyItems().stream()
				.mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());
		
		assertThat(dto.getSupplyItems().get(0).getCode()).isEqualTo("5.1.4.03451.0001");
		assertThat(dto.getSupplyItems().get(0).getQuantity()).isEqualTo(5500);
		assertEquals(dto.getSupplyItems().get(0).getMeasureUnit(), "KILOGRAMO");
		assertThat(dto.getSupplyItems().get(1).getCode()).isEqualTo("5.1.4.03503.0003");
		assertThat(dto.getSupplyItems().get(1).getQuantity()).isEqualTo(5000);
		assertEquals(dto.getSupplyItems().get(1).getMeasureUnit(), "CADA-UNO");
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
		
		//Assert item unit cost * item unit quantity equals item total cost 
		ListIterator<SupplyItemDto> it = dto.getSupplyItems().listIterator();
		it.forEachRemaining(item ->{
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
		
	}
	
	
	@Test
	@DisplayName("CARGAR SUMINISTRO N 223")
	@Order(10)
	void loadSupply223()throws Exception{
		String text = pdfToStringUtils.pdfToString("sum-223.pdf");
		System.err.println(text);
		SupplyDto dto = depositControlService.collectSupplyFromText(text,2L);
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(2023, 0, 9);
		cal2.set(2023, 0,11);
		assertThat(dto.getId()).isNotNull();
		assertThat(dto.getDate()).isBetween(cal, cal2);
		assertEquals(dto.getEstimatedTotalCost().doubleValue(),new BigDecimal(1305266.71).doubleValue());
		
		//Assert Purchase order total cost equals sum of all items total cost
		assertThat(dto.getEstimatedTotalCost().doubleValue()).isEqualTo(dto.getSupplyItems().stream()
				.mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());
		
		assertThat(dto.getSupplyItems().get(0).getCode()).isEqualTo("3.3.1.07030.0001");
		assertThat(dto.getSupplyItems().get(0).getProgramaticCat()).isEqualTo("39.00.00");
		System.err.println("sum 223 quantity:" +dto.getSupplyItems().get(0).getQuantity());
		assertThat(dto.getSupplyItems().get(0).getQuantity()).isEqualTo(1);
		assertThat(dto.getSupplyItems().get(0).getMeasureUnit()).isEqualTo("UNIDAD");
		
		assertThat(dto.getSupplyItems().get(0).getUnitCost().doubleValue())
		.isEqualTo(new BigDecimal( 1305266.71000).doubleValue());
		
		assertThat(dto.getSupplyItems().get(0).getTotalEstimatedCost().doubleValue())
		.isEqualTo(new BigDecimal(  1305266.71).doubleValue());
		
		ListIterator<SupplyItemDto> it = dto.getSupplyItems().listIterator();
		
		//Assert item unit cost * item unit quantity equals item total cost 
		it.forEachRemaining(item ->{
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
		
	}
	@Test
	@DisplayName("CARGAR SUMINISTRO N 177")
	@Order(11)
	void loadSupply177()throws Exception{
		String text = pdfToStringUtils.pdfToString("sum-177.pdf");
		System.err.println(text);
		SupplyDto dto = depositControlService.collectSupplyFromText(text,2L);
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(2024, 0, 10);
		cal2.set(2024, 0,12);
		assertThat(dto.getId()).isNotNull();
		assertEquals(dto.getSupplyNumber(), 177);
		assertThat(dto.getDate()).isBetween(cal, cal2);
		assertEquals(dto.getEstimatedTotalCost().doubleValue(),new BigDecimal(27104000.00).doubleValue());
		
		//Assert Purchase order total cost equals sum of all items total cost
		assertThat(dto.getEstimatedTotalCost().doubleValue()).isEqualTo(dto.getSupplyItems().stream()
				.mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());
		assertThat(dto.getSupplyItems().get(0).getCode()).isEqualTo("5.1.4.07522.0001");
		assertThat(dto.getSupplyItems().get(0).getQuantity()).isEqualTo(50);
		assertThat(dto.getSupplyItems().get(1).getCode()).isEqualTo("5.1.4.07522.0002");
		assertThat(dto.getSupplyItems().get(1).getQuantity()).isEqualTo(100);
		assertThat(dto.getSupplyItems().get(2).getCode()).isEqualTo("5.1.4.07522.0003");
		assertThat(dto.getSupplyItems().get(2).getQuantity()).isEqualTo(150);
		assertThat(dto.getSupplyItems().get(3).getCode()).isEqualTo("5.1.4.07522.0004");
		assertThat(dto.getSupplyItems().get(3).getQuantity()).isEqualTo(150);
		assertThat(dto.getSupplyItems().get(4).getCode()).isEqualTo("5.1.4.07522.0005");
		assertThat(dto.getSupplyItems().get(4).getQuantity()).isEqualTo(150);
		assertThat(dto.getSupplyItems().get(5).getCode()).isEqualTo("5.1.4.07522.0006");
		assertThat(dto.getSupplyItems().get(5).getQuantity()).isEqualTo(200);
		assertThat(dto.getSupplyItems().get(6).getCode()).isEqualTo("5.1.4.07522.0007");
		assertThat(dto.getSupplyItems().get(6).getQuantity()).isEqualTo(150);
		assertThat(dto.getSupplyItems().get(7).getCode()).isEqualTo("5.1.4.07522.0008");
		assertThat(dto.getSupplyItems().get(7).getQuantity()).isEqualTo(200);
		assertThat(dto.getSupplyItems().get(8).getCode()).isEqualTo("5.1.4.07522.0009");
		assertThat(dto.getSupplyItems().get(8).getQuantity()).isEqualTo(150);
		ListIterator<SupplyItemDto> it = dto.getSupplyItems().listIterator();
		
		//Assert item unit cost * item unit quantity equals item total cost 
		it.forEachRemaining(item ->{
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
			
		});
	
	}
	@Test
	@DisplayName("CARGAR SUMINISTRO N 1043")
	@Order(12)
	void loadSupply1043()throws Exception{
		String text = pdfToStringUtils.pdfToString("sum-1043.pdf");
		System.err.println(text);
		SupplyDto dto = depositControlService.collectSupplyFromText(text,2L);
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.set(2023, 2, 7);
		cal2.set(2023, 2,9);
		assertThat(dto.getId()).isNotNull();
		assertEquals(dto.getSupplyNumber(), 1043);
		assertThat(dto.getDate()).isBetween(cal, cal2);
		assertEquals(dto.getEstimatedTotalCost().doubleValue(),new BigDecimal(5500000.00).doubleValue());
		
		//Assert Purchase order total cost equals sum of all items total cost
		assertThat(dto.getEstimatedTotalCost().doubleValue()).isEqualTo(dto.getSupplyItems().stream()
				.mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());
		
		assertThat(dto.getSupplyItems().get(0).getCode()).isEqualTo("5.1.4.07776.0001");
		assertEquals(dto.getSupplyItems().get(0).getQuantity(), 5000);
		
		//Assert item unit cost * item unit quantity equals item total cost 
		ListIterator<SupplyItemDto> it = dto.getSupplyItems().listIterator();
		it.forEachRemaining(item ->{
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
	}
	

}
