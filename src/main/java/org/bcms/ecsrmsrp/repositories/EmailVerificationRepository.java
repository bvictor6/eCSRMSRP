/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   1 Aug 2024
 */
package org.bcms.ecsrmsrp.repositories;

import java.util.Optional;
import java.util.UUID;

import org.bcms.ecsrmsrp.entities.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 */
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
	Optional<EmailVerification> findByIdAndTokenAndUserId(UUID id, String token, UUID uid);
}
