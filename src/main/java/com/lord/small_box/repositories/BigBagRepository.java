package com.lord.small_box.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.BigBag;

public interface BigBagRepository extends JpaRepository<BigBag, Long> {

}
