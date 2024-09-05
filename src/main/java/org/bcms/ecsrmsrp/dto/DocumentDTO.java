/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Sep 5, 2024
*/
package org.bcms.ecsrmsrp.dto;

import org.bcms.ecsrmsrp.enums.DocumentType;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
public class DocumentDTO {
	private String transactionId;
	private String contractNo;
	private String id;
	private String name;
	private String version;
	private String dmsId;
	private String path;
	private Long size;
	private String mimeType;
	private DocumentType docType;
}
