/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   7 Aug 2024
 */
package org.bcms.ecsrmsrp.components;

import java.time.LocalDateTime;
import java.util.Optional;

import org.bcms.ecsrmsrp.classes.Constants;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SessionHandler 
{
	@Autowired UserRepository userRepository;
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
	
	public void getUserSessionValues(HttpServletRequest request) 
	{
		/*Enumeration<String> keys = request.getSession().getAttributeNames();
		while (keys.hasMoreElements())
		{
		  String key = (String)keys.nextElement();
		  logger.warn(key + " Our Session :: " + request.getSession().getAttribute(key).toString() );;
		}*/
		try {
			this.ecsrmID = request.getSession().getAttribute(Constants._SESSION_USER_ECSRM_ID).toString();
			this.userName = request.getSession().getAttribute(Constants._SESSION_USER_EMAIL).toString();		
		}catch (Exception e) {
			logger.error("Error setting session values: " + e.getLocalizedMessage());
			this.ecsrmID = "";
			this.userName = "";
		}
	}
	
	public void setUserSessionValues(HttpServletRequest request, String username) {
		logger.info(username + " Initialize session variables!");
		
		try 
		{
			Optional<User> user  = userRepository.findByUsername(username);
			if(user.isPresent()) 
			{
				User u = user.get();
				request.getSession().setAttribute(Constants._SESSION_USER_NAME, u.getUserProfile().getFirstname() + " " +u.getUserProfile().getLastname());
				request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, u.getUsername());
				request.getSession().setAttribute(Constants._SESSION_USER_IS_PRIMARY_DESIGNATOR, u.getIsPrimaryDesignator()); //
				request.getSession().setAttribute(Constants._SESSION_USER_SUPPLIER_CODE, u.getSupplierCode());
				request.getSession().setAttribute(Constants._SESSION_USER_USER_ID, u.getId());
				request.getSession().setAttribute(Constants._SESSION_USER_ECSRM_ID, u.getVendorProfile().getEcsrmId());
				request.getSession().setAttribute(Constants._SESSION_USER_SUPPLIER_NAME, u.getVendorProfile().getName());
				request.getSession().setMaxInactiveInterval(900);//15min
				//
				logger.info(username + " session variables initialized!");
		        //update user's last login time
				try 
				{
					logger.info(username + " update last login time!");
					u.setLastLogin(LocalDateTime.now());
					userRepository.save(u);
				}catch (Exception e) {
					logger.error(username + " - Error updating last login time!  - " +e.getLocalizedMessage());
				}
				
			}
		}catch (Exception e) {
			logger.error(username + " - Error initialising session values! - " + e.getLocalizedMessage());
		}
		
	}

}
