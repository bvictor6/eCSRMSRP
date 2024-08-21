/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 31, 2024
*/
package org.bcms.ecsrmsrp.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@Entity
@Table(name = "srp_user_profile")
@Setter
@Getter
@NoArgsConstructor
public class UserProfile extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 507581543849659985L;
	private String firstname;
	private String lastname;
	private String phone;
	private String address;
	
	@OneToOne(mappedBy = "userProfile")
	private User user;

}
