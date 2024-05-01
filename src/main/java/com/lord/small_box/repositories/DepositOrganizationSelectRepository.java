package com.lord.small_box.repositories;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lord.small_box.models.AppUser;
import com.lord.small_box.models.DepositOrganizationSelect;

public interface DepositOrganizationSelectRepository extends JpaRepository<DepositOrganizationSelect, Long>{

	List<DepositOrganizationSelect> findByUserAndOrganizationId(AppUser user,Long organizationId,Sort sort);
}
