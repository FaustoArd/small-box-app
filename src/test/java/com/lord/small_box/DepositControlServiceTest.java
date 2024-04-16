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
	private PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	private PurchaseOrderItemRepository purchaseOrderItemRepository;
	
	@Autowired
	private DepositControlService depositControlService;
	
	@Autowired
	private PdfToStringUtils pdfToStringUtils;
	
	@Autowired
	private TextToPurchaseOrder textToPurchaseOrder;
	
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
	@DisplayName("CARGAR ORDEN DE COMPRA N 365")
	@Order(1)
	void pdfToPurchaseOrder()throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-365.pdf");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text,2L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0013");
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00705.0035");
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02113.0002");
		assertEquals(purchaseOrderDto.getOrderNumber(), 365);
	assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(),295600.00);
	}
	
	@Test
	@DisplayName("ENCONTRAR ORDEN DE COMPRA N 365 CON ITEMS")
	@Order(2)
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
	 @Order(3)
	void loadPurchaseOrderToDepositControl()throws Exception{
		 List<PurchaseOrderToDepositReportDto> result = depositControlService.loadPurchaseOrderToDepositControl(1L,1L);
		 result.forEach(e -> System.out.println("Report result: " +e));
	 }
	 
	@Test
	 @DisplayName("CARGAR SUMINISTRO N 551")
	@Order(4)
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
		assertEquals(dto.getSupplyItems().get(1).getMeasureUnit(), "CADA UNO");
		assertThat(dto.getSupplyItems().get(2).getCode()).isEqualTo("5.1.4.03411.0002");
		assertThat(dto.getSupplyItems().get(2).getQuantity()).isEqualTo(2000);
		assertEquals(dto.getSupplyItems().get(2).getMeasureUnit(), "CADA UNO");
		assertThat(dto.getSupplyItems().get(3).getCode()).isEqualTo("5.1.4.03411.0001");
		assertThat(dto.getSupplyItems().get(3).getQuantity()).isEqualTo(1000);
		assertEquals(dto.getSupplyItems().get(3).getMeasureUnit(), "CADA UNO");
		assertThat(dto.getSupplyItems().get(4).getCode()).isEqualTo("5.1.4.03410.0001");
		assertThat(dto.getSupplyItems().get(4).getQuantity()).isEqualTo(4300);
		assertEquals(dto.getSupplyItems().get(4).getMeasureUnit(), "CADA UNO");
		assertThat(dto.getSupplyItems().get(5).getCode()).isEqualTo("5.1.4.03413.0001");
		assertThat(dto.getSupplyItems().get(5).getQuantity()).isEqualTo(4663);
		assertEquals(dto.getSupplyItems().get(5).getMeasureUnit(), "CADA UNO");
		assertThat(dto.getSupplyItems().get(6).getCode()).isEqualTo("5.1.4.03412.0002");
		assertThat(dto.getSupplyItems().get(6).getQuantity()).isEqualTo(2000);
		assertEquals(dto.getSupplyItems().get(6).getMeasureUnit(), "CADA UNO");
		assertThat(dto.getSupplyItems().get(7).getCode()).isEqualTo("5.1.4.03501.0001");
		assertThat(dto.getSupplyItems().get(7).getQuantity()).isEqualTo(5000);
		assertEquals(dto.getSupplyItems().get(7).getMeasureUnit(), "CADA UNO");
		assertThat(dto.getSupplyItems().get(8).getCode()).isEqualTo("5.1.4.03453.0001");
		assertThat(dto.getSupplyItems().get(8).getQuantity()).isEqualTo(7000);
		assertEquals(dto.getSupplyItems().get(8).getMeasureUnit(), "CADA UNO");
		assertThat(dto.getSupplyItems().get(9).getCode()).isEqualTo("5.1.4.03560.0002");
		assertThat(dto.getSupplyItems().get(9).getQuantity()).isEqualTo(1000);
		assertEquals(dto.getSupplyItems().get(9).getMeasureUnit(), "CADA UNO");
		
		//Assert item unit cost * item unit quantity equals item total cost 
		ListIterator<SupplyItemDto> it = dto.getSupplyItems().listIterator();
		it.forEachRemaining(item ->{
			assertEquals(item.getUnitCost().multiply(new BigDecimal(item.getQuantity())).doubleValue(),
					item.getTotalEstimatedCost().doubleValue());
		});
		
	}
	
	
	@Test
	@DisplayName("CARGAR SUMINISTRO N 223")
	@Order(5)
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
	@Order(6)
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
	@Order(7)
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
