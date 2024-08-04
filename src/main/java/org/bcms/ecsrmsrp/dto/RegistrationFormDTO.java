/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.dto;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor                    
public class RegistrationFormDTO {
	private UUID ecsrmId;
	private String name;
	private String contractNo;
	private String tenderNo;
	private String physicalAddress;
	private String email;
	private String phone;
	private String mobile;
	private String postalAddress;
	private String fax;
	private String city;
	private String country;
	private String firstname;
	private String lastname;
	private String designation;
	private String username;
	private String password;
	private Boolean terms;
	private String contactPersonPhone;
	private String contactPersonAddress;

}
