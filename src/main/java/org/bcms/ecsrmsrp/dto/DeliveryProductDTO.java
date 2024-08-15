/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 15, 2024
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
public class DeliveryProductDTO {
	String id;
	String productCode;
	String productDescription;
	int quantity;
	int quantitySupplied;
	String unitOfIssue;
	int leadTime;
	Double unitPrice;
	Double totalProductCost;
	String status;
	String leadTimeDuration;
	String currency;
	String deliveryId;

}
