/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 24, 2024
*/
package org.bcms.ecsrmsrp.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
@Controller
public class CustomErrorController {
	
	public String getErrorPath() {
		return "/error";
	}
	
	@GetMapping("/error")
	public String handleError(HttpServletRequest request) 
	{		
		// get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // TODO: log error details here

        if (status != null) 
        {
            int statusCode = Integer.parseInt(status.toString());

            // display specific error page
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/403";
            } else if(statusCode == HttpStatus.BAD_REQUEST.value()) {
            	return "error/400";
            } else if(statusCode == HttpStatus.SERVICE_UNAVAILABLE.value()) {
            	return "error/503";
            }else if(statusCode == HttpStatus.UNAUTHORIZED.value()) {
            	return "error/401";
            }
        }
        // display generic error
		return "error/500";		
		
	}

}
