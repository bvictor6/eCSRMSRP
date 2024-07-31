/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   25 Jul 2024
 */
package org.bcms.ecsrmsrp.repositories;

import java.util.UUID;

import org.bcms.ecsrmsrp.entities.VendorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 */
public interface VendorProfileRepository extends JpaRepository<VendorProfile, UUID> {
	VendorProfile findByEmail(String email);
}
