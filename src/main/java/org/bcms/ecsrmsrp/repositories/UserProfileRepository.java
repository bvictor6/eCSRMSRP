/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 31, 2024
*/
package org.bcms.ecsrmsrp.repositories;

import java.util.UUID;

import org.bcms.ecsrmsrp.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

}
