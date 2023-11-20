package com.lord.small_box.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lord.small_box.models.Container;

public interface ContainerRepository extends JpaRepository<Container, Integer> {

}
