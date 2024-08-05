/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   5 Aug 2024
 */
package org.bcms.ecsrmsrp.classes;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
public class Document {
	private String name;
	private String type;
	private String path;

}
