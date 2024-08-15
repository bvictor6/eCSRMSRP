/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 14, 2024
 */
package org.bcms.ecsrmsrp.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
public class LocProductDTO {
	String id;
	Double totalBwpValue;
	Double totalBwp;
	int remainingQuantity;
	int receivedQuantity;
	LocalDate expectedDeliveryDate;
	String serialNumber;
	String unit;
	Double unitPrice;
	Double totalProductCost;
	String packSize;
	String description;
	String skuCode;
	String skuType;
	String name;
	String currency;

}
