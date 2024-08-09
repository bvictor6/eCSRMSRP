/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   9 Aug 2024
 */
package org.bcms.ecsrmsrp.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

/**
 * performs rest api requests to the backend ecsrm api bridge
 */
public class RestClientHandler {
	Logger logger = LoggerFactory.getLogger(getClass());
	RestClient restClient;
	
	public String getApiRequest(String params, String user) {
		String results = new String();
		logger.info(user +" :: processing API request for - " + params);
		try {
			restClient = RestClient.create();
			results = restClient.get()
					.uri(params)
					.retrieve()
					.body(String.class);
			logger.info(user + " :: API request response received for "+ params + " ::  " + results.toString() + " ");	
			
		}catch (Exception e) {
			logger.error(user +" :: error encountered processing API request for  "+ params + " :: " +e.getLocalizedMessage());
		}
		
		return results;
	}

}
