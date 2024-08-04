/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 31, 2024
*/
package org.bcms.ecsrmsrp.services;

import java.util.Optional;
import java.util.Random;

import org.bcms.ecsrmsrp.dto.EmailVerificationDTO;
import org.bcms.ecsrmsrp.entities.EmailVerification;
import org.bcms.ecsrmsrp.repositories.EmailVerificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 */
@Service
public class EmailVerificationService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired EmailVerificationRepository emailVerificationRepository;
	
	public String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendVerificationCode(String email) {
        //String code = generateVerificationCode();
        //emailService.sendEmail(email, "Verification Code", "Please use the following code to complete your login: " + code);
    }

    public EmailVerification verifyCode(EmailVerificationDTO data) {
    	Optional<EmailVerification> e = 
				emailVerificationRepository.findByIdAndTokenAndUserId(data.getId(), data.getToken(), data.getUid());
		if(e.isPresent() && !e.get().getIsVerified()) {
			return e.get();
		}else {
			return null;
		}
    }
       

}
