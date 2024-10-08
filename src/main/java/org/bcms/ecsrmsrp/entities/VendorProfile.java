/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   25 Jul 2024
 */
package org.bcms.ecsrmsrp.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@Entity
@Table(name = "srp_vendor_profile")
@Getter
@Setter
@NoArgsConstructor
public class VendorProfile extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -9015035884431135895L;
	private UUID ecsrmId;
	private String name;
	private String physicalAddress;
	private String postalAddress;
	private String email;
	private String phone;
	private String fax;
	private String mobileNo;	
	private String tenderNo;
	private String contractNo;

	@OneToOne(mappedBy = "vendorProfile")
	private User user;
	
	/*@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "contact_person_title_id")
	private Title title;*/
	
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private Country country;
}
