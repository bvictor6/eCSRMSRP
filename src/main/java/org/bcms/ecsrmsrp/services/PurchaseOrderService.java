/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 14, 2024
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
public class PurchaseOrderService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired RestClientHandler restClientHandler;
	
	public String getSupplierPurchaseOrders(String supplierID, String user) {
		
		logger.info(user +" :: request supplier LOC's for - " + supplierID);
		
		String endpoint  = Constants._ECSRM_BRIDGE_API + "/suppliers/purchase-order/"+supplierID;
		
		return restClientHandler.getApiRequest(endpoint, user);
	}

}
