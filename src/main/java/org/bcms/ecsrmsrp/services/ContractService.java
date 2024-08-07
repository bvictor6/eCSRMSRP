/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   7 Aug 2024
 */
package org.bcms.ecsrmsrp.services;

import org.bcms.ecsrmsrp.classes.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * 
 */
@Service
public class ContractService {
	Logger logger = LoggerFactory.getLogger(getClass());
	private RestClient restClient;
	
	public String supplierContracts(String supplierID, String user) {
		String results = new String();
		logger.info(user +" :: request supplier contracts for - " + supplierID);
		try {
			restClient = RestClient.create();
			results = restClient.get()
					.uri(Constants._ECSRM_BRIDGE_API + "/contracts/supplier/"+supplierID)
					.retrieve()
					.body(String.class);
			logger.info(user + " :: Contracts API Response for supplier "+ supplierID + " -- " + results.toString());	
			
		}catch (Exception e) {
			logger.error(user +" :: Contracts API Response for supplier "+ supplierID + " -- " +e.getLocalizedMessage());
		}
		
		return results;
	}
	
	public String supplierContract(String supplierID,String contractID, String user) {
		String results = new String();
		logger.info(user +" :: request contract for - " + contractID + " for supplier - " + supplierID);
		try {
			restClient = RestClient.create();
			results = restClient.get()
					.uri(Constants._ECSRM_BRIDGE_API + "/contracts/supplier/"+supplierID + "/"+contractID)
					.retrieve()
					.body(String.class);
			logger.info(user + " :: Contract API Response for "+ contractID + " for supplier - "+ supplierID + " -- " + results.toString());	
			
		}catch (Exception e) {
			logger.error(user +" :: Contracts API Response for "+ contractID + " for supplier - "+ supplierID + " -- " +e.getLocalizedMessage());
		}
		
		return results;
	}

}
