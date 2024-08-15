/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   9 Aug 2024
 */
package org.bcms.ecsrmsrp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
public class ContractProductDTO {
	String id;
	String contractId;
	String productId;
	int quantity;
	Double totalProductCost;
	Double grandTotal;
	int quantitySupplied;
	int remainingQuantity;
	String unit;
	String locProductId;
	String code;
	String description;
	String batchNo;
	String category;
	String skuCode;
	String skuDescription;
	String skuType;
	String startDate;
	String midTermDate;
	String currency;
	String serialNumber;
	Double unitPrice;
	String packSize;
}
