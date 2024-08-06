/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   30 Jul 2024
 */
package org.bcms.ecsrmsrp.classes;

/**
 * 
 */
public interface Constants {
	//eCSRM Bridge API address
	String _ECSRM_BRIDGE_API = "http://127.0.0.1:8088/api/v1";
	/**
     * Session variables related to logged in user
     */
    String _SESSION_USER_NAME = "name";
    String _SESSION_USER_USERNAME = "userName";
    String _SESSION_USER_EMAIL = "email";
    String _SESSION_USER_DESC = "desc";
    String _SESSION_USER_LAST_LOGIN = "lastLogin";
    String _SESSION_USER_ROLE = "role";
    String _SESSION_USER_USER_ID = "userId";
    String _SESSION_USER_ECSRM_ID = "ecsrmId";
    
    String _SUCCESS_MSG = "success_msg";
    String _ERROR_MSG = "error_msg";
    
    

}
