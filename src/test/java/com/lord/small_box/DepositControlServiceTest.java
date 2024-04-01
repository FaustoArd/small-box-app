package com.lord.small_box;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNotNull;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.lord.small_box.dao.PurchaseOrderDao;
import com.lord.small_box.dao.PurchaseOrderItemDao;
import com.lord.small_box.dtos.PurchaseOrderDto;
import com.lord.small_box.dtos.SupplyDto;
import com.lord.small_box.mappers.PurchaseOrderMapper;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.models.PurchaseOrder;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;
import com.lord.small_box.services.OrganizationService;
import com.lord.small_box.services.DepositControlService;
import com.lord.small_box.text_analisys.TextToPurchaseOrder;
import com.lord.small_box.utils.PdfToStringUtils;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class DepositControlServiceTest {
	
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	
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
	@Order(1)
	void pdfToPurchaseOrder()throws Exception {
		String text = pdfToStringUtils.pdfToString("oc-365");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text,2L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0013");
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00705.0035");
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02113.0002");
		assertEquals(purchaseOrderDto.getOrderNumber(), 365);
		assertThat(purchaseOrderDto.getExecuterUnitOrganizationId()).isNotNull();
		assertThat(purchaseOrderDto.getDependencyOrganizacionId()).isNotNull();
		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(),295600.00);
	}
	
	@Test
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
	 @Order(3)
	void loadPurchaseOrderToDepositControl()throws Exception{
		 List<String> result = depositControlService.loadPurchaseOrderToDepositControl(1L);
		 result.forEach(e -> System.out.println("Report result: " +e));
	 }
	 
	@Test
	@Order(4)
	void loadSupply()throws Exception{
		String text = pdfToStringUtils.pdfToString("sum-551");
		System.err.println(text);
		SupplyDto dto = depositControlService.collectSupplyFromText(text,2L);
		Calendar cal = Calendar.getInstance();
		cal.set(2024, 0, 1);
		assertThat(dto.getId()).isNotNull();
		assertThat(dto.getDate()).isBetween(cal, Calendar.getInstance());
		assertThat(dto.getEstimatedTotalCost()).isGreaterThan(new BigDecimal(40000000));
		assertThat(dto.getEstimatedTotalCost().doubleValue()).isEqualTo(dto.getSupplyItems().stream()
				.mapToDouble(totalItem -> totalItem.getTotalEstimatedCost().doubleValue()).sum());
	}
	@Test
	@Order(6)
	void PdfToPurchaseOrder2()throws Exception {
		String text = pdfToStringUtils.pdfToString("oc 658 expte 177");
		PurchaseOrderDto purchaseOrderDto = depositControlService.collectPurchaseOrderFromText(text,2L);
		assertEquals(purchaseOrderDto.getItems().get(0).getCode(), "2.1.1.00788.0013");
		assertEquals(purchaseOrderDto.getItems().get(1).getCode(), "2.1.1.00705.0035");
		assertEquals(purchaseOrderDto.getItems().get(7).getCode(), "2.1.1.02113.0002");
		assertEquals(purchaseOrderDto.getOrderNumber(), 365);
		assertThat(purchaseOrderDto.getExecuterUnitOrganizationId()).isNotNull();
		assertThat(purchaseOrderDto.getDependencyOrganizacionId()).isNotNull();
		assertEquals(purchaseOrderDto.getPurchaseOrderTotal().doubleValue(),295600.00);
	}
	 @Test
	 @Order(7)
	void loadPurchaseOrderToDepositControl2()throws Exception{
		 List<String> result = depositControlService.loadPurchaseOrderToDepositControl(2L);
		 result.forEach(e -> System.out.println("Report result: " +e));
	 }
	

}
