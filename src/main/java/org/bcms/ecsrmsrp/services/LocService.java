/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 13, 2024
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
public class LocService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired RestClientHandler restClientHandler;
	
	public String getSupplierLocList(String supplierID, String user) {
		
		logger.info(user +" :: request supplier LOC's for - " + supplierID);
		
		String endpoint  = Constants._ECSRM_BRIDGE_API + "/loc/supplier/"+supplierID;
		
		return restClientHandler.getApiRequest(endpoint, user);
	}
	
	public String getSupplierLocDetails(String id, String supplierID, String user) {
		
		logger.info(user +" :: request supplier LOC details for - " + id + " - supplier - " + supplierID);
		
		String endpoint  = Constants._ECSRM_BRIDGE_API + "/loc/" + id + "/" + supplierID;
		
		return restClientHandler.getApiRequest(endpoint, user);
	}
	
	public String getLocProducts(String locID, String supplierID, String user) {
		
		logger.info(user +" :: request LOC products for - " + locID + " - supplier - " + supplierID);
		
		String endpoint  = Constants._ECSRM_BRIDGE_API + "/loc/products/" + locID;
		
		return restClientHandler.getApiRequest(endpoint, user);
	}


}
