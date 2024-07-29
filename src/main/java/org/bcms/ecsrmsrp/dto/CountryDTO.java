/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 29, 2024
*/
package org.bcms.ecsrmsrp.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
@AllArgsConstructor
public class CountryDTO {
	private UUID id;
	private String name;
	private int phonecode;

}
