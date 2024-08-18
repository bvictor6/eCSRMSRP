/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 18, 2024
 */
package org.bcms.ecsrmsrp.repositories;

import java.util.UUID;

import org.bcms.ecsrmsrp.entities.LoginToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 */
public interface LoginTokenRepository extends JpaRepository<LoginToken, UUID> {

}
