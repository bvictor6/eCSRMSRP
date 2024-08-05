/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   6 Aug 2024
 */
package org.bcms.ecsrmsrp.classes;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
public class Product {
	private String code;
	private String description;
	private String category;
	private String type;

}
