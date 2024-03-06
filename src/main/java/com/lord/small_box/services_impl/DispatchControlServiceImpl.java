package com.lord.small_box.services_impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lord.small_box.exceptions.ItemNotFoundException;
import com.lord.small_box.models.DispatchControl;
import com.lord.small_box.models.Organization;
import com.lord.small_box.repositories.DispatchControlRepository;
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

	@Override
	public String createDispatch(DispatchControl dispatchControl) {
		Organization org = organizationService.findById(dispatchControl.getOrganization().getId());
		dispatchControl.setOrganization(org);
		DispatchControl savedDispatchControl = dispatchControlRepository.save(dispatchControl);
		return "Se guardo el despacho: " + savedDispatchControl.getDate() + " " + savedDispatchControl.getType() + " "
				+ savedDispatchControl.getNumber();
	}

	@Override
	public List<DispatchControl> findAll() {
		return (List<DispatchControl>) dispatchControlRepository.findAll();
	}

	@Override
	public DispatchControl findById(Long id) {
		return dispatchControlRepository.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("No se encontro el despacho"));
	}

	@Override
	public String deleteById(Long id) {

		if (dispatchControlRepository.existsById(id)) {

			String result = dispatchControlRepository.findById(id).map(e ->e.getDate() + " " + e.getType() + " " + e.getNumber())
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
	public List<DispatchControl> findAllDispatchsByOrganizationByUserId(Long userId) {
		List<Organization> organizations = organizationService.findAllOrganizationsByUsers(userId);
		List<DispatchControl> dispatchControls = findAllDistpachControlsByOrganizationIn(organizations);
		return dispatchControls;
	}

}
