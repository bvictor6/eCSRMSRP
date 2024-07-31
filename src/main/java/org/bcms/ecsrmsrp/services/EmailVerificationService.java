/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 31, 2024
*/
package org.bcms.ecsrmsrp.services;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 */
@Service
public class EmailVerificationService {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	public String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendVerificationCode(String email) {
        //String code = generateVerificationCode();
        //emailService.sendEmail(email, "Verification Code", "Please use the following code to complete your login: " + code);
    }

    public boolean verifyCode(String userEmail, String userEnteredCode, String sentCode) {
        return sentCode.equals(userEnteredCode);
    }

}
