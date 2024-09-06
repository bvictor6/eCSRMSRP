/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   6 Aug 2024
 */
package org.bcms.ecsrmsrp.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import org.bcms.ecsrmsrp.alfresco.GetNodeContent;
import org.bcms.ecsrmsrp.classes.Constants;
import org.bcms.ecsrmsrp.components.RestClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */
@Service
public class DocumentService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	RestClientHandler restClientHandler;
	@Autowired
	GetNodeContent getNodeContent;

	// @Cacheable(value = "contractDocuments", key = "#contractID")
	public String fetchContractDocuments(String supplierID, String contractID, String user) {
		logger.info(user + " :: request documents for - " + contractID + " for supplier - " + supplierID);

		String endpoint = Constants._ECSRM_BRIDGE_API + "/contracts/documents/" + contractID;

		return restClientHandler.getApiRequest(endpoint, user);
	}

	/**
	 * 
	 * @param id
	 * @param mime
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ResponseEntity<Resource> downloadDocument(String id, String mime, 
			HttpServletResponse response) throws IOException 
	{
		logger.warn("Downlod document with ID - " + id);
		Resource resource = getNodeContent.getNodeContent(id);

		if (resource == null) {
			return ResponseEntity.notFound().build();
		}

		String contentType = URLConnection.guessContentTypeFromName(resource.getFilename());
		if (contentType == null) {
			//contentType = "application/octet-stream";
			contentType = mime;
		}
		String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
		logger.info(contentType + "  -  " + headerValue);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);
	}

	/*
	 * public void downloadDocument1(String id, String mime, HttpServletResponse
	 * response) { Resource resource; try { resource =
	 * getNodeContent.getNodeContent("958f2bb0-2a41-41ba-b2b6-20d4b10ca18c"); File
	 * file = resource.getFile(); File file = null; try { file = new
	 * ClassPathResource("static/documents/" + name).getFile();
	 * logger.info(file.getAbsolutePath()); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } // if(file.exists()) {
	 * //get mimetype String mimetype =
	 * URLConnection.guessContentTypeFromName(file.getName()); if(mimetype == null)
	 * { mimetype = "application/octet-stream"; } response.setContentType(mimetype);
	 * response.setHeader("Content-Disposition",
	 * String.format("attachment; filename =\""+ file.getName() +"\""));
	 * response.setContentLength((int)file.length()); try { InputStream inputStream
	 * = new BufferedInputStream(new FileInputStream(file));
	 * FileCopyUtils.copy(inputStream, response.getOutputStream()); }catch
	 * (Exception e) { e.printStackTrace(); } }else {
	 * logger.error("File does not exist!"); } } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * }
	 */

}
