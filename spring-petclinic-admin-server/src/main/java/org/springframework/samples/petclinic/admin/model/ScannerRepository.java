package org.springframework.samples.petclinic.admin.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ScannerRepository extends JpaRepository<Alert, Integer>{
	
//	List<Alert> findByLastName(String lastName);

}
