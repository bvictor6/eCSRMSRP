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
import org.bcms.ecsrmsrp.services.DocumentService;
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
	
	@GetMapping(path = "/index")
	public String index(Model model, HttpServletRequest request) {
		model.addAttribute("contract", "DS2/010/05/2023");
		model.addAttribute("loc", "CMS/LOC/435/2023-2024");
		model.addAttribute("issueDate", LocalDateTime.now());
		model.addAttribute("count", 7);
		return "loc/index";
	}
	
	@GetMapping(path = "/view")
	public String view(Model model, HttpServletRequest request) {
		
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
		//LOC Details
		model.addAttribute("contract", "DS2/010/05/2023");
		model.addAttribute("loc", "CMS/LOC/435/2023-2024");
		model.addAttribute("issueDate", LocalDateTime.now());
		
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
		
		return "loc/view";
	}
	
	@GetMapping(path = "/download")
	public void download(@RequestParam("name") String name, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Downloading ... "+ name);
		
		documentService.downloadDocument(response, name);		
	}

}
