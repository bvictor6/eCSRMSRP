/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   1 Aug 2024
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
public class EmailVerificationDTO {
	private UUID uid;
	private UUID id;
	private String token;

}
