/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 16, 2024
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
public class DeliveryScheduleDTO {
	String id;
	String productCode;
	String productDescription;
	LocalDate deliveryDate;
	String deliveryNo;
	LocalTime deliveryTime;

}
