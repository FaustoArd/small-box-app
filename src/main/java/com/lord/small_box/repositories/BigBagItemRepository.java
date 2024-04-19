package com.lord.small_box.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.BigBag;
import com.lord.small_box.models.BigBagItem;

public interface BigBagItemRepository extends JpaRepository<BigBagItem, Long> {

	public List<BigBagItem> findByBigBag(BigBag bigBag);
}

