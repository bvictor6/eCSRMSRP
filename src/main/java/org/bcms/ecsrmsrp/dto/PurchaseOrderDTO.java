/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 14, 2024
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
public class PurchaseOrderDTO {
	String id;
	String status;
	String poNumber;
	LocalDate poIssueDate;
	LocalDate poCollectionDate;
	String poCollectedBy;
	String poCollectionComment;
	String locNumber;
	String type;
	String tenderNo; 
	String contractNo;
	String name;

}
