package com.lord.small_box.services_impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
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
import com.lord.small_box.repositories.WorkTemplateDestinationRepository;
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

	@Autowired
	private final WorkTemplateDestinationRepository workTemplateDestinationRepository;

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

			String result = dispatchControlRepository.findById(id)
					.map(e -> e.getDate() + " " + e.getType() + " " + e.getDocNumber()).toString();
			dispatchControlRepository.deleteById(id);
			return "Se elimino el despacho: " + result;
		} else {
			throw new ItemNotFoundException("No se encontro el despacho");
		}
	}

	@Override
	public List<DispatchControl> findAllDistpachControlsByOrganizationIn(List<Organization> organizations) {
		log.info("Find all distpatch controls by organizations list");
		return (List<DispatchControl>) dispatchControlRepository.findAllDistpachControlsByOrganizationIn(organizations);
	}

	@Override
	public List<DispatchControl> findAllDistpachControlsByOrganization(Long organizationId) {
		log.info("Find all distpatch controls by organization");
		Organization org = organizationService.findById(organizationId);
		Sort sort = Sort.by("date").descending();
		return (List<DispatchControl>) dispatchControlRepository.findAllDistpachControlsByOrganization(org,sort);
	}

	@Override
	public String dispatchWorkTemplate(Long workTemplateId) {
		log.info("Dispatching workTemplate to distpach");
		WorkTemplate workTemplate = workTemplateRepository.findById(workTemplateId)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el Documento"));
		Organization org = organizationService.findById(workTemplate.getOrganization().getId());
		DispatchControl dispatchControl = workTemplateToDispatch(workTemplate, org);
		DispatchControl savedDispatchControl = dispatchControlRepository.save(dispatchControl);
		return "Se despacho el documento: " + savedDispatchControl.getType() + " "
				+ savedDispatchControl.getDocNumber();
	}

	private static DispatchControl workTemplateToDispatch(WorkTemplate workTemplate, Organization organization) {
		log.info("Mapping DorkTemplate to DispatchControl");
		DispatchControl dispatchControl = new DispatchControl();
		dispatchControl.setDate(workTemplate.getDate());
		dispatchControl.setType(workTemplate.getCorrespond());
		dispatchControl.setDocNumber(workTemplate.getCorrespondNumber());
		dispatchControl.setDescription(workTemplate.getText());
		dispatchControl
				.setToDependency(workTemplate.getDestinations().stream().map(m -> m).collect(Collectors.joining(",")));
		dispatchControl.setOrganization(organization);
		return dispatchControl;
	}

	@Override
	public List<DispatchControl> findAllDispatchControlByOrganizationPagingAndSorting(Long organizationId,
			Integer pageNo, Integer pageSize, String sortBy) {
		log.info("Looking dispatchs by organizacion. Paging and sorting.");
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

		Organization org = organizationService.findById(organizationId);
		List<DispatchControl> pageResult = dispatchControlRepository.findAllDispatchControlsByOrganization(org, paging);
		return pageResult;

	}

	@Override
	public List<DispatchControl> findAllDispatchControlByOrgByExamplePagingAndSorting(Long organizationId,
			String strExample, Integer pageNo, Integer pageSize, String sortBy) {
		strExample = strExample.replace("\"", "").replace("example", "").replace(":", "").replace("{", "").replace("}", "");
		log.info("Looking dispatchs by organizacion and example. Paging and sorting. Example: " + strExample);

		StringMatcher match = StringMatcher.CONTAINING;
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(match).withIgnoreCase();
		Organization org = organizationService.findById(organizationId);
		DispatchControl dispatchControl = exampleMatchToDispatchObject(strExample);
		dispatchControl.setOrganization(org);
		Example<DispatchControl> example = Example.of(dispatchControl, matcher);
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Page<DispatchControl> responsePage = dispatchControlRepository.findAll(example, paging);
		return responsePage.getContent();

	}
	private final String patternDateDash = "^(([0-9]{2})*(-){1}){2}([0-9]{4})";
	private final String patternDateNoDash = "^(([0-9]{2})*){2}([0-9]{4})";
	private final String patternType = "(MEMO)*(EXP)*(SUM)*(CORR EXP)*(CORR. EXP)*(CORR MEMO)*(COMP)*(MOD PRESUP)*(NOTA)*";
	private final String patternDocCumber = "[0-9]{1,6}/[0-9]{2}";
	private final String patternDesc = "^(([a-z])+(\s)*?)*";

	@Override
	public DispatchControl exampleMatchToDispatchObject(String example) {
		// pattern = pattern.replace("\"", "");
		System.out.println("Example: " + example);
		String patternToDependency = dispatchControlRepository.findAll().stream()
				.map(e -> "(" + e.getToDependency() + ")*").distinct().collect(Collectors.joining(""));
		

		if (Pattern.matches(patternDateDash, example) || Pattern.matches(patternDateNoDash, example)) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			try {
				cal.setTime(sdf.parse(example));
				return DispatchControl.builder().date(cal).build();
			} catch (ParseException e) {
				return DispatchControl.builder().date(null).build();
			}
		} else if (Pattern.matches(patternType, example.toUpperCase())) {
			return DispatchControl.builder().type(example).build();
		} else if (Pattern.matches(patternDocCumber, example)) {
			return DispatchControl.builder().docNumber(example).build();
		} else if (Pattern.matches(patternDesc, example.toLowerCase())) {
			return DispatchControl.builder().description(example).build();
		} else if (Pattern.matches(patternToDependency, example.toUpperCase())) {
			return DispatchControl.builder().toDependency(example.toUpperCase()).build();
		}
		return new DispatchControl();

	}

	private static DispatchControl mapToDispatchControl(String example) {
		log.info("Mapping String example to DispatchControl");
		Calendar cal = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		// cal.setTime(sdf.parse(example));
		DispatchControl dispatchControl = DispatchControl.builder().description(example).build();
		log.info("Map generated: " + example);
		return dispatchControl;

	}

}
