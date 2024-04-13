package com.lord.small_box;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lord.small_box.models.Deposit;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.OrganizationResponsible;
import com.lord.small_box.repositories.DepositRepository;
import com.lord.small_box.repositories.OrganizationRepository;
import com.lord.small_box.repositories.OrganizationResponsibleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadTestData {
	
	@Autowired
	private final OrganizationRepository organizationRepository;
	
	@Autowired
	private final OrganizationResponsibleRepository organizationResponsibleRepository;
	
	@Autowired
	private DepositRepository depositRepository;
	
	
	public void loadData() {
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
		org1.setOrganizationName("Secretaria de desarrollo social");
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

		Organization secDesSocial = organizationRepository.save(org1);
		Organization dirAdmDesp = organizationRepository.save(org2);
		organizationRepository.save(org3);
		organizationRepository.save(org4);
		organizationRepository.save(org5);
		
		Deposit deposit = Deposit.builder().name("AVELLANEDA")
				.streetName("AVELLANEDA").houseNumber("2212")
				.organization(dirAdmDesp).build();
		depositRepository.save(deposit);
	}

}
