/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 29, 2024
*/
package org.bcms.ecsrmsrp.repositories;

import java.util.List;
import java.util.UUID;

import org.bcms.ecsrmsrp.dto.CountryDTO;
import org.bcms.ecsrmsrp.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 
 */
public interface CountryRepository extends JpaRepository<Country, UUID> {
	
	@Query("SELECT new org.bcms.ecsrmsrp.dto.CountryDTO(c.id,c.nicename,c.phonecode) "
			+ "FROM Country c")
	List<CountryDTO> countries();

}
