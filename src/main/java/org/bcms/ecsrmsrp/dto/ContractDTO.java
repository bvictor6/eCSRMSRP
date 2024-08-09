/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   7 Aug 2024
 */
package org.bcms.ecsrmsrp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
public class ContractDTO {
	String id;
	String tenderNo;
	String contractNo;
	String description;
	String category;
	String state;
	String title;
	Double totalContractValue;
	Long terminationNoticePeriod;
	Long contractTerm;
	Boolean isActive;
	Boolean  isApproved;
	String contractType;

}
