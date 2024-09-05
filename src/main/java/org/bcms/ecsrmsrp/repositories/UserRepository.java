/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   25 Jul 2024
 */
package org.bcms.ecsrmsrp.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bcms.ecsrmsrp.api.interfaces.UserDetailsInterface;
import org.bcms.ecsrmsrp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 
 */
public interface UserRepository extends JpaRepository<User, UUID> {
	//User findByUsername(String username);
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findByUsernameAndTwoFactorToken(String username, String token);
	
	List<User> findBySupplierCode(String supplierCode);

	@Query(nativeQuery = true, value = "SELECT u.id, u.is_enabled,u.supplier_code,u.username,"
			+ "	p.firstname,p.lastname,p.phone,p.address,"
			+ "	v.name supplier,v.contract_no,v.physical_address,v.ecsrm_id supplier_id"
			+ "	FROM public.srp_user u"
			+ "	inner join srp_user_profile p on p.id=u.user_profile_id"
			+ "	inner join srp_vendor_profile v on v.id=u.vendor_profile_id"
			+ " WHERE u.supplier_code=:code AND u.is_enabled=:status")
	List<UserDetailsInterface> findBySupplierCodeAndIsEnabled(@Param("code") String code, @Param("status") Boolean status);

}
