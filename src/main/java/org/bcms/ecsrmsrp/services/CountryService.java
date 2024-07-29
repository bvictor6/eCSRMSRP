/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 29, 2024
*/
package org.bcms.ecsrmsrp.services;

import java.util.List;

import org.bcms.ecsrmsrp.dto.CountryDTO;
import org.bcms.ecsrmsrp.repositories.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 */
@Service
public class CountryService {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired CountryRepository countryRepository;
	
	public List<CountryDTO> getCountries(){
		logger.info("Fetching all country list...");
		return countryRepository.countries();
	}

}
