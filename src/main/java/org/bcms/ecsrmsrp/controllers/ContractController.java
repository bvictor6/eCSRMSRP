/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   3 Aug 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.bcms.ecsrmsrp.classes.Document;
import org.bcms.ecsrmsrp.classes.Product;
import org.bcms.ecsrmsrp.components.SessionHandler;
import org.bcms.ecsrmsrp.dto.ContractDTO;
import org.bcms.ecsrmsrp.dto.DashboardContractDTO;
import org.bcms.ecsrmsrp.services.ContractService;
import org.bcms.ecsrmsrp.services.DocumentService;
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
@RequestMapping(path = "/contracts")
public class ContractController {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired DocumentService documentService;
	@Autowired ContractService contractService;
	@Autowired SessionHandler sessionHandler;
	
	@GetMapping(path = "/index")
	public String index(Model model, HttpServletRequest request) {		
		sessionHandler.UserSessionValues(request);
		final String supplierID = sessionHandler.getEcsrmID();
		final String user = sessionHandler.getUserName();
		List<ContractDTO> contracts = new ArrayList<>();
		
		logger.info(user + " contracts access!");
		
		try 
		{
			String results = contractService.supplierContracts(supplierID, user);
			JSONArray jsonArray = new JSONArray(results);
			if(jsonArray.length()>0) 
			{
				for(int i = 0; i < jsonArray.length(); i++) 
				{
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					ContractDTO contract = new ContractDTO();
					contract.setContractNo(jsonObject.getString("contractNo"));
					contract.setId(jsonObject.getString("id"));
					contract.setCategory(jsonObject.getString("category"));
					contract.setContractTerm(jsonObject.getLong("contractTerm"));
					contract.setContractType(jsonObject.getString("contractType"));
					contract.setDescription(jsonObject.getString("description"));
					contract.setIsActive(jsonObject.getBoolean("isActive"));
					contract.setIsApproved(jsonObject.getBoolean("isApproved"));
					contract.setState(jsonObject.getString("state"));
					contract.setTenderNo(jsonObject.getString("tenderNo"));
					contract.setTerminationNoticePeriod(jsonObject.getLong("terminationNoticePeriod"));
					contract.setTitle(jsonObject.getString("title"));
					contract.setTotalContractValue(jsonObject.getDouble("totalContractValue"));
					//
					contracts.add(contract);
				}
			}else {
				logger.info(user + " Contract details results contained errors - " + results);
			}
			
		}catch (Exception e) {
			logger.error(user + " :: errors encountered while retrieving contract details; - " + e.getLocalizedMessage());
		}
		
		//
		model.addAttribute("contracts", contracts);
		return "contract/index";
	}
	
	@GetMapping(path = "/view")
	public String view(Model model, HttpServletRequest request, @RequestParam("id") String id) {	
		sessionHandler.UserSessionValues(request);
		final String supplierID = sessionHandler.getEcsrmID();
		final String user = sessionHandler.getUserName();
		
		logger.info(user +" request contract details for contract id " + id);
		
		try 
		{
			String results = contractService.supplierContract(supplierID,id, user);
			logger.info("Response: " + results);
			
			JSONObject jsonObject = new JSONObject(results);
			
			//Contract Details
			model.addAttribute("contractNo", jsonObject.getString("contractNo"));
			model.addAttribute("tenderNo", jsonObject.getString("tenderNo"));
			model.addAttribute("contractType", jsonObject.getString("contractType"));
			model.addAttribute("category", jsonObject.getString("category"));
			model.addAttribute("totalContractValue", jsonObject.getDouble("totalContractValue"));
			model.addAttribute("isApproved", jsonObject.getBoolean("isApproved"));
			model.addAttribute("contractDate", LocalDate.parse("2022-10-17"));
			model.addAttribute("count", 7);
			
		}catch (Exception e) {
			logger.error(user + " :: errors encountered while retrieving contract details for contract - " +id + ", supplier - "+supplierID +" :: " + e.getLocalizedMessage());
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
		
		//Product Details
		List<Product> products  = new ArrayList<>();
		Product product = new Product();
		product.setCode("PAR013001");
		product.setCategory("DRUG");
		product.setDescription("CMS-PARACETAMOL 1000MG TABLETS, 500'S");
		product.setType("Purchased");
		products.add(product);
		
		Product product1 = new Product();
		product1.setCode("PAR012001");
		product1.setCategory("DRUG");
		product1.setDescription("CMS-Para amino salicylic acid Grau 4g schts, 30's");
		product1.setType("Purchased");
		products.add(product1);
		
		Product product2 = new Product();
		product2.setCode("PAR011001");
		product2.setCategory("DRUG");
		product2.setDescription("CMS-Paroxetine 20mg Tablets, 30m's");
		product2.setType("Purchased");
		products.add(product2);
		
		model.addAttribute("products", products);
		
		return "/contract/view";
	}
	
	@GetMapping(path = "/download")
	public void download(@RequestParam("name") String name, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Downloading ... "+ name);
		
		documentService.downloadDocument(response, name);		
	}

}
