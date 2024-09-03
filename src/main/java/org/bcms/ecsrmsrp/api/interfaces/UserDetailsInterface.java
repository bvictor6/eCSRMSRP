/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Sep 3, 2024
 */
package org.bcms.ecsrmsrp.api.interfaces;

import java.util.UUID;

/**
 * 
 */
public interface UserDetailsInterface {
	
	UUID getId();
	Boolean getIsEnabled();
	String getSupplierCode();
	String getUsername();
	String getFirstname();
	String getLastname();
	String getPhone();
	String getAddress();
	String getSupplier();
	String getContractNo();
	String getPhysicalAddress();
	String getSupplierId();

}
