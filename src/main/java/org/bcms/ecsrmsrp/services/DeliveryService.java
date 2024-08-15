/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 15, 2024
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
public class DeliveryService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired RestClientHandler restClientHandler;
	
	/**
	 * Get deliveries for a particular supplier
	 * @param supplierID
	 * @param user
	 * @return
	 */
	public String getSupplierDeliveries(String supplierID, String user) {
		logger.info(user +" :: request supplier deliveries for - " + supplierID);
		
		String endpoint  = Constants._ECSRM_BRIDGE_API + "/deliveries/supplier/"+supplierID;
		
		return restClientHandler.getApiRequest(endpoint, user);
	}
	
	/**
	 * Get delivery products for a particular delivery
	 * @param deliveryID
	 * @param user
	 * @return
	 */
	public String getDeliveryProducts(String deliveryID, String user) {
		
		logger.info(user +" :: request delivery products for - " + deliveryID);
		
		String endpoint  = Constants._ECSRM_BRIDGE_API + "/deliveries/products/"+deliveryID;
		
		return restClientHandler.getApiRequest(endpoint, user);
	}
	
	public String getDeliverySchedule(String supplierID, String user) {
		logger.info(user +" :: request supplier delivery schedule for - " + supplierID);
		
		String endpoint  = Constants._ECSRM_BRIDGE_API + "/deliveries/schedule/"+supplierID;
		
		return restClientHandler.getApiRequest(endpoint, user);
		
	}

}
