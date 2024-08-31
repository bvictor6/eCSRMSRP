/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   4 Aug 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.bcms.ecsrmsrp.components.SessionHandler;
import org.bcms.ecsrmsrp.dto.DeliveryDTO;
import org.bcms.ecsrmsrp.dto.DeliveryProductDTO;
import org.bcms.ecsrmsrp.dto.DeliveryScheduleDTO;
import org.bcms.ecsrmsrp.services.DeliveryService;
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

/**
 * 
 */
@Controller
@RequestMapping(path = "/deliveries")
public class DeliveryController {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired DeliveryService deliveryService;
	@Autowired SessionHandler sessionHandler;
	
	@GetMapping(path = "/index")
	public String index(Model model, HttpServletRequest request) {
		sessionHandler.getUserSessionValues(request);
		final String supplierID = sessionHandler.getEcsrmID();
		final String user = sessionHandler.getUserName();
		logger.info(user + " supplier deliveries access!");
		
		List<DeliveryDTO> deliveries = new ArrayList<>();
		
		try
		{
			String results = deliveryService.getSupplierDeliveries(supplierID, user);
			
			JSONArray jsonArray = new JSONArray(results);
			if(jsonArray.length() > 0)
			{
				
				for(int i = 0; i < jsonArray.length(); i++)
				{
					DeliveryDTO delivery = new DeliveryDTO();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					//
					delivery.setContractNo(jsonObject.isNull("contractNo") ? "" : jsonObject.getString("contractNo"));
					delivery.setDeliveryDate(jsonObject.isNull("deliveryDate") ? LocalDate.now() : LocalDate.parse(jsonObject.getString("deliveryDate")));
					delivery.setDeliveryNo(jsonObject.isNull("deliveryNo") ? "" : jsonObject.getString("deliveryNo"));
					delivery.setDeliveryTime(jsonObject.isNull("deliveryTime") ? LocalTime.now() : LocalTime.parse(jsonObject.getString("deliveryTime")));
					delivery.setId(jsonObject.isNull("id") ? "" : jsonObject.getString("id"));
					delivery.setLocCollectionDate(jsonObject.isNull("locCollectionDate") ? LocalDate.now() : LocalDate.parse(jsonObject.getString("locCollectionDate")));
					delivery.setLocNumber(jsonObject.isNull("locNumber") ? "" : jsonObject.getString("locNumber"));
					delivery.setOrderDate(jsonObject.isNull("orderDate") ? LocalDate.now() : LocalDate.parse(jsonObject.getString("orderDate")));
					delivery.setStatus(jsonObject.isNull("status") ? "" : jsonObject.getString("status"));
					delivery.setSupplierId(jsonObject.isNull("supplierId") ? "" : jsonObject.getString("supplierId"));
					delivery.setType(jsonObject.isNull("type") ? "" : jsonObject.getString("type"));
					//
					deliveries.add(delivery);
				}
				
			} else {
				logger.warn(user + " - no delivery results returned for supplier " + supplierID);
			}
			
			model.addAttribute("deliveries", deliveries);
		}catch (Exception e) {
			logger.error(user + " - error retrieving supplier deliveries for " + supplierID + " :: " + e.getLocalizedMessage());
		}
		
				
		return "delivery/index";
	}
	
	@GetMapping(path = "/view")
	public String view(Model model, HttpServletRequest request, @RequestParam("id") String id) {
		
		sessionHandler.getUserSessionValues(request);
		final String supplierID = sessionHandler.getEcsrmID();
		final String user = sessionHandler.getUserName();
		logger.info(user + " delivery products access!");
		
		List<DeliveryProductDTO> deliveryProducts = new ArrayList<>();
		
		try 
		{
			String results = deliveryService.getDeliveryProducts(id, user);
			
			JSONArray jsonArray = new JSONArray(results);
			if(jsonArray.length() > 0)
			{
				DeliveryProductDTO product = new DeliveryProductDTO();
				for(int i = 0; i < jsonArray.length(); i++)
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					product.setCurrency(jsonObject.isNull("currency") ? "BWP" : jsonObject.getString("currency"));
					product.setDeliveryId(jsonObject.isNull("deliveryId") ? "" : jsonObject.getString("deliveryId"));
					product.setId(jsonObject.isNull("id") ? "" : jsonObject.getString("id"));
					product.setLeadTime(jsonObject.isNull("leadTime") ? 0 : jsonObject.getInt("leadTime"));
					product.setLeadTimeDuration(jsonObject.isNull("leadTimeDuration") ? "" : jsonObject.getString("leadTimeDuration"));
					product.setProductCode(jsonObject.isNull("productCode") ? "" : jsonObject.getString("productCode"));
					product.setProductDescription(jsonObject.isNull("productDescription") ? "" : jsonObject.getString("productDescription"));
					product.setQuantity(jsonObject.isNull("quantity") ? 0 : jsonObject.getInt("quantity"));
					product.setQuantitySupplied(jsonObject.isNull("quantitySupplied") ? 0 : jsonObject.getInt("quantitySupplied"));
					product.setStatus(jsonObject.isNull("status") ? "" : jsonObject.getString("status"));
					product.setTotalProductCost(jsonObject.isNull("totalProductCost") ? 0.00 : jsonObject.optDoubleObject("totalProductCost"));
					product.setUnitOfIssue(jsonObject.isNull("unitOfIssue") ? "" : jsonObject.getString("unitOfIssue"));
					product.setUnitPrice(jsonObject.isNull("unitPrice") ? 0.00 : jsonObject.getDouble("unitPrice"));
					//
					deliveryProducts.add(product);
				}
				
			}else {
				logger.warn(user + " - no delivery products results returned for delivery " + id);
			}
			
			model.addAttribute("products", deliveryProducts);
			
		}catch (Exception e) {
			logger.error(user +" - error getting delivery products for delivery " + id + " :: " + e.getLocalizedMessage());
		}
		
		return "delivery/view";
	}
	
	@GetMapping(path = "/schedule")
	public String schedule(Model model, HttpServletRequest request) 
	{
		sessionHandler.getUserSessionValues(request);
		final String supplierID = sessionHandler.getEcsrmID();
		final String user = sessionHandler.getUserName();
		logger.info(user + " supplier deliveries scheduler access!");
		
		List<DeliveryScheduleDTO> schedule = new ArrayList<>();
		
		try
		{
			String results = deliveryService.getDeliverySchedule(supplierID, user);
			
			JSONArray jsonArray = new JSONArray(results);
			if(jsonArray.length() > 0)
			{
				
				for(int i = 0; i < jsonArray.length(); i++)
				{
					DeliveryScheduleDTO delivery = new DeliveryScheduleDTO();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					//
					delivery.setProductCode(jsonObject.isNull("productCode") ? "" : jsonObject.getString("productCode"));
					delivery.setProductDescription(jsonObject.isNull("productDescription") ? "" : jsonObject.getString("productDescription"));
					delivery.setDeliveryDate(jsonObject.isNull("deliveryDate") ? LocalDate.now() : LocalDate.parse(jsonObject.getString("deliveryDate")));
					delivery.setDeliveryNo(jsonObject.isNull("deliveryNo") ? "" : jsonObject.getString("deliveryNo"));
					delivery.setDeliveryTime(jsonObject.isNull("deliveryTime") ? LocalTime.now() : LocalTime.parse(jsonObject.getString("deliveryTime")));
					delivery.setId(jsonObject.isNull("id") ? "" : jsonObject.getString("id"));
					//
					schedule.add(delivery);
				}
				
			} else {
				logger.warn(user + " - no delivery results returned for supplier " + supplierID);
			}
			//
			model.addAttribute("deliveries", schedule);
		}catch (Exception e) {
			logger.error(user + " - error retrieving supplier deliveries for " + supplierID + " :: " + e.getLocalizedMessage());
		}
		
		return "/delivery/schedule";
	}

}
