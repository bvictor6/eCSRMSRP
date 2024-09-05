/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   25 Jul 2024
 */
package org.bcms.ecsrmsrp.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bcms.ecsrmsrp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 */
public interface UserRepository extends JpaRepository<User, UUID> {
	//User findByUsername(String username);
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findByUsernameAndTwoFactorToken(String username, String token);
	
	List<User> findBySupplierCode(String supplierCode);

}
