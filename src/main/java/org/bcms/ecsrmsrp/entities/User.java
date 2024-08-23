/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   25 Jul 2024
 */
package org.bcms.ecsrmsrp.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "srp_user")
public class User extends BaseEntity implements Serializable
{
	private static final long serialVersionUID = 8145258977829552990L;
	@Column(unique = true, nullable = false)
	private String username;
	private String password;
	private Boolean termsAccepted;
	private Boolean isVerified;
	private Boolean isLocked;
	private Boolean isActive;
	@Column(insertable = false, updatable = true)
	private LocalDateTime lastLogin;
	private String twoFactorSecret;
	private Boolean twoFactorEnabled;
	private Boolean isPrimaryDesignator;
	
	/*@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "role_id")
	private Role role;*/
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "vendor_profile_id", referencedColumnName = "id")
	private VendorProfile vendorProfile;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_profile_id", referencedColumnName = "id")
	private UserProfile userProfile;
}
