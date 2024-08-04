/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   4 Aug 2024
 */
package org.bcms.ecsrmsrp.classes;

import org.bcms.ecsrmsrp.enums.ResultStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
public class Results {
	private ResultStatus status;
	private String message;
	

}
