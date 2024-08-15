/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   4 Aug 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bcms.ecsrmsrp.classes.Document;
import org.bcms.ecsrmsrp.classes.Product;
import org.bcms.ecsrmsrp.components.SessionHandler;
import org.bcms.ecsrmsrp.dto.LocDTO;
import org.bcms.ecsrmsrp.dto.LocProductDTO;
import org.bcms.ecsrmsrp.services.DocumentService;
import org.bcms.ecsrmsrp.services.LocService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
		sessionHandler.setUserSessionValues(request);
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
		sessionHandler.setUserSessionValues(request);
		final String supplierID = sessionHandler.getEcsrmID();
		final String user = sessionHandler.getUserName();
		logger.info(user + " View LOC - " + id);
		
		LocDTO loc = new LocDTO();
		List<LocProductDTO> products = new ArrayList<>();
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
				
				//Product Details				
				try 
				{
					String locProducts = locService.getLocProducts(jsonObject.isNull("id") ? "" : jsonObject.getString("id"), supplierID, user);
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
							p.setSerialNumber(o.isNull("serialNumber") ? "" : o.getString("serialNumber"));
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
					}
					model.addAttribute("products", products);
					
				}catch (Exception e) {
					logger.error(user + " :: error retrieving LOC products for LOC - "+ (jsonObject.isNull("id") ? "" : jsonObject.getString("id")) + " with error :: " + e.getLocalizedMessage());
				}
			}else {
				model.addAttribute("loc", null);
				logger.info(user  + " No LOC details set!");
			}
			
		}catch (Exception e) {
			logger.error(user + " :: error viewing LOC details for - "+ id + " with error :: " + e.getLocalizedMessage());
		}
		
		List<Document> documents  = new ArrayList<>();
		Document document = new Document();
		document.setName("ONCE_OFF_CONTRACT");
		document.setType("Contract Document");
		document.setPath("ONCE_OFF_CONTRACT.pdf");
		documents.add(document);
		
		Document document1 = new Document();
		document1.setName("FRAMEWORK_CONTRACT");
		document1.setType("Contract Document");
		document1.setPath("FRAMEWORK_CONTRACT.pdf");
		documents.add(document1);
		
		Document document2 = new Document();
		document2.setName("LOC_TEMPLATE");
		document2.setType("LOC Document");
		document2.setPath("LOC_TEMPLATE.pdf");
		documents.add(document2);
		
		model.addAttribute("documents", documents);
						
		return "loc/view";
	}
	
	@GetMapping(path = "/download")
	public void download(@RequestParam("name") String name, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Downloading ... "+ name);
		
		documentService.downloadDocument(response, name);		
	}

}
