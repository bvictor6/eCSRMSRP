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
			return "<i class=\"fa fa-file-pdf-o\" aria-hidden=\"true\"></i>";
		}
		case ("application/x-tika-ooxml") : {
			return "<i class=\"fa fa-file-excel-o\" aria-hidden=\"true\"></i>";
		}
		case "application/vnd.ms-excel": {
			return "<i class=\"fa fa-file-excel-o\" aria-hidden=\"true\"></i>";
		}
		case "image/jpeg": {
			return "<i class=\"fa fa-file-image-o\" aria-hidden=\"true\"></i>";
		}
		case "image/png": {
			return "<i class=\"fa fa-file-image-o\" aria-hidden=\"true\"></i>";
		}
		case "text/plain": {
			return "<i class=\"fa fa-file-text-o\" aria-hidden=\"true\"></i>";
		}
		case "application/msword": {
			return "<i class=\"fa fa-file-word-o\" aria-hidden=\"true\"></i>";
		}
		case "application/x-ms-owner": {
			return "<i class=\"fa fa-file-word-o\" aria-hidden=\"true\"></i>";
		}
		default:
			return "<i class=\"fa fa-file-o\" aria-hidden=\"true\"></i>";
		}
	}

}
