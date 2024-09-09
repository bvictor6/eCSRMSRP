/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   4 Aug 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bcms.ecsrmsrp.classes.Document;
import org.bcms.ecsrmsrp.components.ApplicationTypeHandler;
import org.bcms.ecsrmsrp.components.SessionHandler;
import org.bcms.ecsrmsrp.dto.DocumentDTO;
import org.bcms.ecsrmsrp.dto.LocDTO;
import org.bcms.ecsrmsrp.dto.LocProductDTO;
import org.bcms.ecsrmsrp.services.DocumentService;
import org.bcms.ecsrmsrp.services.LocService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */

@Controller
@RequestMapping(path = "/loc")
public class LocController {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired DocumentService documentService;
	@Autowired LocService locService;
	@Autowired SessionHandler sessionHandler;
	
	@GetMapping(path = "/index")
	public String index(Model model, HttpServletRequest request) {
		sessionHandler.getUserSessionValues(request);
		final String supplierID = sessionHandler.getEcsrmID();
		final String user = sessionHandler.getUserName();
		logger.info(user + " LOC list access!");
		
		List<LocDTO> locs = new ArrayList<>();
		
		try {
			String results = locService.getSupplierLocList(supplierID, user);
			
			JSONArray jsonArray = new JSONArray(results);
			if(!jsonArray.isEmpty()) 
			{
				for(int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					LocDTO loc = new LocDTO();
					//
					loc.setContractId(jsonObject.isNull("contractId") ? "" : jsonObject.getString("contractId"));
					loc.setContractNo(jsonObject.isNull("contractNo") ? "" : jsonObject.getString("contractNo"));
					loc.setId(jsonObject.isNull("id") ? "" : jsonObject.getString("id"));
					loc.setLocIssueDate(jsonObject.isNull("locIssueDate") ? LocalDate.now() : LocalDate.parse(jsonObject.getString("locIssueDate")));
					loc.setLocNumber(jsonObject.isNull("locNumber") ? "" : jsonObject.getString("locNumber"));
					loc.setStatus(jsonObject.isNull("status") ? "" : jsonObject.getString("status"));
					//
					locs.add(loc);
				}
			}
			
			model.addAttribute("locs", locs);
			
		}catch (Exception e) {
			logger.info(user + " :: error returned while requesting for LOC list - "+ e.getLocalizedMessage());
		}
				
		return "loc/index";
	}
	
	@GetMapping(path = "/view")
	public String view(Model model, HttpServletRequest request, @RequestParam("id") String id) 
	{
		sessionHandler.getUserSessionValues(request);
		final String supplierID = sessionHandler.getEcsrmID();
		final String user = sessionHandler.getUserName();
		logger.info(user + " View LOC - " + id);
		
		LocDTO loc = new LocDTO();
		//LOC Details
		try 
		{
			String results = locService.getSupplierLocDetails(id, supplierID, user);
			JSONObject jsonObject = new JSONObject(results);
			//
			if(!jsonObject.isEmpty()) 
			{
				loc.setContractId(jsonObject.isNull("contractId") ? "" : jsonObject.getString("contractId"));
				loc.setContractNo(jsonObject.isNull("contractNo") ? "" : jsonObject.getString("contractNo"));
				loc.setId(jsonObject.isNull("id") ? "" : jsonObject.getString("id"));
				loc.setLocIssueDate(jsonObject.isNull("locIssueDate") ? LocalDate.now() : LocalDate.parse(jsonObject.getString("locIssueDate")));
				loc.setLocNumber(jsonObject.isNull("locNumber") ? "" : jsonObject.getString("locNumber"));
				loc.setStatus(jsonObject.isNull("status") ? "" : jsonObject.getString("status"));
				//
				model.addAttribute("loc", loc);
				logger.info(user + " LOC details set!");				
				
			}else {
				model.addAttribute("loc", null);
				logger.info(user  + " No LOC details set!");
			}
			
		}catch (Exception e) {
			logger.error(user + " :: error viewing LOC details for - "+ id + " with error :: " + e.getLocalizedMessage());
		}
		
		//Product Details				
		model.addAttribute("products", fetchLocProducts(id, user, supplierID));
		//fetch LOC documents		
		model.addAttribute("documents", fetchLocDocuments(id, user, supplierID));
						
		return "loc/view";
	}
	
	/**
	 * Download LOC document
	 * @param id
	 * @param mime
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping(path = "/download")
	public ResponseEntity<Resource> download(@RequestParam("id") String id, @RequestParam("mime") String mime,
			HttpServletRequest request, HttpServletResponse response) {
		logger.info("Downloading ... "+ id);
		
		try {
			return documentService.downloadDocument(id, mime, response);
		} catch (Exception e) {
			logger.error("Error downloading document with id - " + id);
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}		
	}
	
	/**
	 * 
	 * @param id
	 * @param user
	 * @param supplierId
	 * @return
	 */
	private List<LocProductDTO> fetchLocProducts(String id, String user, String supplierId)
	{
		List<LocProductDTO> products = new ArrayList<>();
		try 
		{
			String locProducts = locService.getLocProducts(id, supplierId, user);
			JSONArray jsonArray = new JSONArray(locProducts);
			if(jsonArray.length()>0)
			{
				for(int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject o = jsonArray.getJSONObject(i);
					LocProductDTO p = new LocProductDTO();
					//
					p.setCurrency(o.isNull("currency") ? "" : o.getString("currency"));
					p.setDescription(o.isNull("description") ? "" : o.getString("description"));
					p.setExpectedDeliveryDate(o.isNull("expectedDeliveryDate") ? LocalDate.now() : LocalDate.parse(o.getString("expectedDeliveryDate")));
					p.setId(o.isNull("id") ? "" : o.getString("id"));
					p.setName(o.isNull("name") ? "" : o.getString("name"));
					p.setPackSize(o.isNull("packSize") ? "" : o.getString("packSize"));
					p.setReceivedQuantity(o.isNull("receivedQuantity") ? 0 : o.getInt("receivedQuantity"));
					p.setRemainingQuantity(o.isNull("remainingQuantity") ? 0 : o.getInt("remainingQuantity"));
					p.setSerialNumber(o.isNull("serialNumber") ? "1" : o.getString("serialNumber"));
					p.setSkuCode(o.isNull("skuCode") ? "" : o.getString("skuCode"));
					p.setSkuType(o.isNull("skuType") ? "" : o.getString("skuType"));
					p.setTotalBwp(o.isNull("totalBwp") ? 0.00 : o.getDouble("totalBwp"));
					p.setTotalBwpValue(o.isNull("totalBwpValue") ? 0.00 : o.getDouble("totalBwpValue"));
					p.setTotalProductCost(o.isNull("totalProductCost") ? 0.00 : o.getDouble("totalProductCost"));
					p.setUnit(o.isNull("unit") ? "" : o.getString("unit"));
					p.setUnitPrice(o.isNull("unitPrice") ? 0.00 : o.getDouble("unitPrice"));
					//
					products.add(p);
				}
			}else {
				logger.warn(user + " LOC products request returned empty results!");
				products.add(null);
			}
			return 	products;		
		}catch (Exception e) {
			logger.error(user + " :: error retrieving LOC products for LOC - "+ id + " with error :: " + e.getLocalizedMessage());
			products.add(null);
			return products;
		}
	}
	
	
	/**
	 * 
	 * @param id
	 * @param user
	 * @param supplierId
	 * @return
	 */
	private List<DocumentDTO> fetchLocDocuments(String id, String user, String supplierId) 
	{
		try 
		{
			logger.info(user + " - fetch documents for LOC " + id);
			String documents  = documentService.fetchLocDocuments(supplierId, id, user);
			JSONArray jsonArray = new JSONArray(documents);
			
			if(jsonArray.length() > 0)
			{
				List<DocumentDTO> locDocuments = new ArrayList<>();
				for(int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject o = jsonArray.getJSONObject(i);
					DocumentDTO d = new DocumentDTO();
					//
					d.setLocNumber(o.isNull("locNumber")? "" : o.getString("locNumber"));
					d.setDmsId(o.isNull("dmsId")? "" : 
						o.getString("dmsId").substring(0, o.getString("dmsId").indexOf(";")-1)
						);
					d.setId(o.isNull("id")? "" : o.getString("id"));
					d.setName(o.isNull("name")? "" : o.getString("name"));
					d.setMimeType(o.isNull("mimeType")? "" : o.getString("mimeType"));
					d.setPath(o.isNull("path")? "" : o.getString("path"));
					d.setSize(o.isNull("size")? 0 : o.getLong("size")/1024);
					d.setTransactionId(o.isNull("transactionId")? "" : o.getString("transactionId"));
					d.setVersion(o.isNull("version")? "" : o.getString("version"));
					//
					ApplicationTypeHandler applicationTypeHandler = new ApplicationTypeHandler(o.isNull("mimeType")? "" : o.getString("mimeType"));
					d.setIcon(applicationTypeHandler.getDocumentIcon());
					//
					locDocuments.add(d);
				}
				return locDocuments;
			}else {
				return null;
			}
			
		}catch (Exception e) {
			logger.error("Error encountered while fetching documents for LOC id " + id + " ,for supplier " + supplierId + " :: "+e.getLocalizedMessage());
			return null;
		}
	}

}
