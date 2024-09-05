/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   7 Aug 2024
 */
package org.bcms.ecsrmsrp.services;

import org.bcms.ecsrmsrp.classes.Constants;
import org.bcms.ecsrmsrp.components.RestClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 */
@Service
public class ContractService 
{
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired RestClientHandler restClientHandler;
	
	public String supplierContracts(String supplierID, String user) {
		logger.info(user +" :: request supplier contracts for - " + supplierID);
		
		String endpoint  = Constants._ECSRM_BRIDGE_API + "/contracts/supplier/"+supplierID;
		
		return restClientHandler.getApiRequest(endpoint, user);
	}
	
	public String supplierContract(String supplierID,String contractID, String user) {
		logger.info(user +" :: request contract for - " + contractID + " for supplier - " + supplierID);
				
		String endpoint  = Constants._ECSRM_BRIDGE_API + "/contracts/supplier/"+supplierID + "/"+contractID;
		
		return restClientHandler.getApiRequest(endpoint, user);
	}
	
	public String contractProducts(String contractID, String user) {
		
		logger.info(user +" :: request contract products for - " + contractID);
		
		String endpoint  = Constants._ECSRM_BRIDGE_API + "/contracts/products/"+contractID ;
		
		return restClientHandler.getApiRequest(endpoint, user);
	}

}
