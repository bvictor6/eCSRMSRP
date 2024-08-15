/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 13, 2024
 */
package org.bcms.ecsrmsrp.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
public class LocDTO {
	private String id;
	private LocalDate locIssueDate;
	private String locNumber;
	private String status;
	private String contractId;
	private String contractNo; 

}
