/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   4 Aug 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bcms.ecsrmsrp.components.SessionHandler;
import org.bcms.ecsrmsrp.dto.PurchaseOrderDTO;
import org.bcms.ecsrmsrp.services.PurchaseOrderService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
@Controller
@RequestMapping(path = "/purchaseOrders")
public class PurchaseOrderController {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired PurchaseOrderService purchaseOrderService;
	@Autowired SessionHandler sessionHandler;
	
	@GetMapping(path = "/index")
	public String index(Model model, HttpServletRequest request) {
		sessionHandler.setUserSessionValues(request);
		final String supplierID = sessionHandler.getEcsrmID();
		final String user = sessionHandler.getUserName();
		logger.info(user + " Purchase orders list access!");
		
		List<PurchaseOrderDTO> purchaseOrders  = new ArrayList<>();
		
		try {
			String results  = purchaseOrderService.getSupplierPurchaseOrders(supplierID, user);
			
			JSONArray jsonArray = new JSONArray(results);
			if(jsonArray.length()>0)
			{
				for(int i = 0 ; i < jsonArray.length(); i++)
				{
					PurchaseOrderDTO po = new PurchaseOrderDTO();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					//
					po.setContractNo(jsonObject.isNull("contractNo") ? "" : jsonObject.getString("contractNo"));
					po.setId(jsonObject.isNull("id") ? "" : jsonObject.getString("id"));
					po.setLocNumber(jsonObject.isNull("locNumber") ? "" : jsonObject.getString("locNumber"));
					po.setName(jsonObject.isNull("name") ? "" : jsonObject.getString("name"));
					po.setPoCollectedBy(jsonObject.isNull("poCollectedBy") ? "" : jsonObject.getString("poCollectedBy"));
					po.setPoCollectionComment(jsonObject.isNull("poCollectionComment") ? "" : jsonObject.getString("poCollectionComment"));
					po.setPoCollectionDate(jsonObject.isNull("poCollectionDate") ? LocalDate.now() : LocalDate.parse(jsonObject.getString("poCollectionDate")));
					po.setPoIssueDate(jsonObject.isNull("poIssueDate") ? LocalDate.now() : LocalDate.parse(jsonObject.getString("poIssueDate")));
					po.setPoNumber(jsonObject.isNull("poNumber") ? "" : jsonObject.getString("poNumber"));
					po.setStatus(jsonObject.isNull("status") ? "" : jsonObject.getString("status"));
					po.setTenderNo(jsonObject.isNull("tenderNo") ? "" : jsonObject.getString("tenderNo"));
					po.setType(jsonObject.isNull("type") ? "" : jsonObject.getString("type"));
					//
					purchaseOrders.add(po);
				}
				
				model.addAttribute("purchaseOrders", purchaseOrders);
				
			} else {
				logger.warn(user + " - no purchase orders returned for supplier - " + supplierID);
			}
			
		}catch (Exception e) {
			logger.error(user + " - encountered errors while requesting for supplier purchase orders as follows : " + e.getLocalizedMessage());
		}
		
		return "purchaseOrder/index";
	}
	
	@GetMapping(path = "/view")
	public String view(Model model, HttpServletRequest request) {
		
		return "purchaseOrder/view";
	}

}
