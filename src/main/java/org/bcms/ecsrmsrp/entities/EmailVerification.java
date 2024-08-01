/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   1 Aug 2024
 */
package org.bcms.ecsrmsrp.entities;

import java.time.LocalDateTime;
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
@Table(name = "srp_email_verification_token")
@Getter
@Setter
@NoArgsConstructor
public class EmailVerification extends BaseEntity {
	private String token;
	private Boolean isVerified;
	private UUID userId;
	private LocalDateTime dateVerified;

}
