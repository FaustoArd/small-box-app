package com.lord.small_box.services_impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.DispatchControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.models.WorkTemplate;
import com.lord.small_box.repositories.DispatchControlRepository;
import com.lord.small_box.repositories.WorkTemplateRepository;
import com.lord.small_box.services.DispatchControlService;
import com.lord.small_box.services.OrganizationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DispatchControlServiceImpl implements DispatchControlService {

	@Autowired
	private final DispatchControlRepository dispatchControlRepository;

	@Autowired
	private final OrganizationService organizationService;
	
	@Autowired
	private final WorkTemplateRepository workTemplateRepository;
	
	private static final Logger log = LoggerFactory.getLogger(DispatchControlServiceImpl.class);

	@Override
	public String createDispatch(DispatchControl dispatchControl) {
		log.info("Create dispatch");
		Organization org = organizationService.findById(dispatchControl.getOrganization().getId());
		dispatchControl.setOrganization(org);
		DispatchControl savedDispatchControl = dispatchControlRepository.save(dispatchControl);
		return "Se guardo el despacho: " + savedDispatchControl.getDate() + " " + savedDispatchControl.getType() + " "
				+ savedDispatchControl.getDocNumber();
	}

	@Override
	public List<DispatchControl> findAll() {
		return (List<DispatchControl>) dispatchControlRepository.findAll();
	}

	@Override
	public DispatchControl findById(Long id) {
		log.info("Find dispatch by id");
		return dispatchControlRepository.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el despacho"));
	}

	@Override
	public String deleteById(Long id) {
		log.info("Delete dispatch by id");
		if (dispatchControlRepository.existsById(id)) {

			String result = dispatchControlRepository.findById(id).map(e ->e.getDate() + " " + e.getType() + " " + e.getDocNumber())
					.toString();
			dispatchControlRepository.deleteById(id);
			return "Se elimino el despacho: " + result;
		} else {
			throw new ItemNotFoundException("No se encontro el despacho");
		}
	}

	@Override
	public List<DispatchControl> findAllDistpachControlsByOrganizationIn(List<Organization> organizations) {
		return (List<DispatchControl>) dispatchControlRepository.findAllDistpachControlsByOrganizationIn(organizations);
	}

	

	@Override
	public List<DispatchControl> findAllDistpachControlsByOrganization(Long organizationId) {
		Organization org = organizationService.findById(organizationId);
		
		return (List<DispatchControl>)dispatchControlRepository.findAllDistpachControlsByOrganization(org);
	}

	@Override
	public String dispatchWorkTemplate(Long workTemplateId) {
		log.info("Dispatching Document");
		WorkTemplate workTemplate = workTemplateRepository.findById(workTemplateId)
				.orElseThrow(()-> new ItemNotFoundException("No se encontro el Documento"));
		Organization org = organizationService.findById(workTemplate.getOrganization().getId());
		DispatchControl dispatchControl = workTemplateToDispatch(workTemplate, org);
		DispatchControl savedDispatchControl = dispatchControlRepository.save(dispatchControl);
		return "Se despacho el documento: " + savedDispatchControl.getType() + " " + savedDispatchControl.getDocNumber();
	}
	
	private static DispatchControl workTemplateToDispatch(WorkTemplate workTemplate,Organization organization) {
		log.info("Map workTemplate do DispatchControl");
		DispatchControl dispatchControl = new DispatchControl();
		dispatchControl.setDate(workTemplate.getDate());
		dispatchControl.setType(workTemplate.getCorrespond());
		dispatchControl.setDocNumber(workTemplate.getCorrespondNumber());
		dispatchControl.setDescription(workTemplate.getText());
		dispatchControl.setToDependency(workTemplate.getDestinations()
				.stream().map(m -> m).collect(Collectors.joining(",")));
		dispatchControl.setOrganization(organization);
		return dispatchControl;
	}

	@Override
	public List<DispatchControl> findAllDispatchControlByOrganizationPagingAndSorting(Long organizationId,Integer pageNo,Integer pageSize,String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize,Sort.by(sortBy).descending());
		
		Organization org = organizationService.findById(organizationId);
		List<DispatchControl> pageResult = dispatchControlRepository.findAllDispatchControlsByOrganization(org, paging);
		return pageResult;
		
	}
}
