/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Sep 5, 2024
*/
package org.bcms.ecsrmsrp.components;

/**
 * 
 */
public class ApplicationTypeHandler {
	
	String documentType;
	
	public ApplicationTypeHandler(String documentType) {
		this.documentType = documentType;
	}
	
	public String getDocumentIcon() 
	{
		switch (documentType) {
		case "application/pdf": {
			return "fa fa-file-pdf-o text-danger";
		}
		case ("application/x-tika-ooxml") : {
			return "fa fa-file-excel-o text-success";
		}
		case "application/vnd.ms-excel": {
			return "fa fa-file-excel-o text-success";
		}
		case "image/jpeg": {
			return "fa fa-file-image-o text-default";
		}
		case "image/png": {
			return "fa fa-file-image-o text-default";
		}
		case "text/plain": {
			return "fa fa-file-text-o text-default";
		}
		case "application/msword": {
			return "fa fa-file-word-o text-primary";
		}
		case "application/x-ms-owner": {
			return "fa fa-file-word-o text-primary";
		}
		default:
			return "fa fa-file-o text-default";
		}
	}

}
