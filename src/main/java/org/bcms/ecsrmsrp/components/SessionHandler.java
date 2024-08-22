/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   7 Aug 2024
 */
package org.bcms.ecsrmsrp.components;

import java.util.Enumeration;

import org.bcms.ecsrmsrp.classes.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Access user session variables from this class
 */
@Component
@Data
@NoArgsConstructor
public class SessionHandler {
	Logger logger = LoggerFactory.getLogger(getClass());
	String name;
	String userName;
	String email;
	String desc;
	String lastLogin;
	String role;
	String userID;
	String ecsrmID;
	String supplierName;
	
	public void setUserSessionValues(HttpServletRequest request) {
		Enumeration<String> keys = request.getSession().getAttributeNames();
		while (keys.hasMoreElements())
		{
		  String key = (String)keys.nextElement();
		  logger.warn(key + " Our Session :: " + request.getSession().getAttribute(key).toString() );;
		}
		
		this.ecsrmID = request.getSession().getAttribute(Constants._SESSION_USER_ECSRM_ID).toString();
		this.userName = request.getSession().getAttribute(Constants._SESSION_USER_EMAIL).toString();		
	}

}
