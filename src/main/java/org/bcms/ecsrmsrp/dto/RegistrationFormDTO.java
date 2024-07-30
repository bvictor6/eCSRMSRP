/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor                    
public class RegistrationFormDTO {
	private String name;
	private String contractNo;
	private String physicalAddress;
	private String email;
	private String phone;
	private String mobile;
	private String postalAddress;
	private String fax;
	private String city;
	private String country;
	private String firstName;
	private String lastName;
	private String designation;
	private String password;
	private Boolean terms;

}
