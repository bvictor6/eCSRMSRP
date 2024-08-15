/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   9 Aug 2024
 */
package org.bcms.ecsrmsrp.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * performs rest api requests to the backend ecsrm api bridge
 */
@Component
public class RestClientHandler {
	Logger logger = LoggerFactory.getLogger(getClass());
	RestClient restClient;
	
	/**
	 * 
	 * @param params - API end point being called
	 * @param user - logged in user initiating the request
	 * @return
	 */
	public String getApiRequest(String uri, String user) {
		String results = new String();
		logger.info(user +" :: processing API request for - " + uri);
		try {
			restClient = RestClient.create();
			results = restClient.get()
					.uri(uri)
					.retrieve()
					.body(String.class);
			logger.info(user + " :: API request response received for "+ uri + " ::  " + results.toString() + " ");	
			
		}catch (Exception e) {
			logger.error(user +" :: error encountered processing API request for  "+ uri + " :: " +e.getLocalizedMessage());
		}
		
		return results;
	}

}
