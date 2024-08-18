/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 18, 2024
 */
package org.bcms.ecsrmsrp.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@Entity
@Table(name = "srp_login_token")
@Getter
@Setter
@NoArgsConstructor
public class LoginToken extends BaseEntity {
	private String token;
	private Boolean isValid;
	private UUID userId;
}
