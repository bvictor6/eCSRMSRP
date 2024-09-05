/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Sep 5, 2024
*/
package org.bcms.ecsrmsrp.components;

import org.bcms.ecsrmsrp.enums.DocumentType;

/**
 * 
 */
public class ApplicationTypeHandler {
	
	String documentType;
	
	public ApplicationTypeHandler(String documentType) {
		this.documentType = documentType;
	}
	
	public DocumentType getDocumentType() 
	{
		switch (documentType) {
		case "application/pdf": {
			return DocumentType.PDF;
		}
		case ("application/x-tika-ooxml") : {
			return DocumentType.EXCEL;
		}
		case "image/jpeg": {
			return DocumentType.JPG;
		}
		case "image/png": {
			return DocumentType.PNG;
		}
		case "text/plain": {
			return DocumentType.TXT;
		}
		case "application/msword": {
			return DocumentType.WORD;
		}
		case "application/x-ms-owner": {
			return DocumentType.WORD;
		}
		default:
			return DocumentType.OTHER;
		}
	}

}
