/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 15, 2024
 */
package org.bcms.ecsrmsrp.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
public class DeliveryDTO {
	String id;
	String contractNo;
	LocalDate deliveryDate;
	String deliveryNo;
	LocalTime deliveryTime;
	String locNumber;
	LocalDate locCollectionDate;
	LocalDate orderDate;
	String status; 
	String type;
	String supplierId;

}
