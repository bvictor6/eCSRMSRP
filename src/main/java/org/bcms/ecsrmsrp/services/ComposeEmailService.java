/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 18, 2024
 */
package org.bcms.ecsrmsrp.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.bcms.ecsrmsrp.classes.MailObject;
import org.bcms.ecsrmsrp.classes.Results;
import org.bcms.ecsrmsrp.enums.ResultStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

/**
 * 
 */
@Service
public class ComposeEmailService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired EmailService emailService;
	
	public Results composeEmailMessage(String to, String recipient, String subject, String body,
			String link, String template) {
		Results results = new Results();
		try 
		{
			MailObject mailObject = new MailObject();
            mailObject.setTo(to);
            mailObject.setRecipientName(recipient);
            mailObject.setSubject(subject);
            mailObject.setText(body);
            mailObject.setTemplateEngine(template);
            mailObject.setSenderName("eCSRM System.");
            mailObject.setVerificationLink(link);
            mailObject.setMailCopyrightYear(" "+LocalDate.now().getYear());

            logger.info("Send verification email to " + to);

            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("recipientName", mailObject.getRecipientName());
            templateModel.put("text", mailObject.getText());
            templateModel.put("senderName", mailObject.getSenderName());
            templateModel.put("copyrightYear", mailObject.getMailCopyrightYear());
            templateModel.put("verificationLink", mailObject.getVerificationLink());

            if (mailObject.getTemplateEngine().toString().trim() != null) 
            {
                logger.info("Send email for - " + mailObject.getTemplateEngine().toUpperCase());
                try 
                {
                    emailService.sendMessageUsingThymeleafTemplate(
                            mailObject.getTo(),
                            mailObject.getSubject(),
                            templateModel,
                            template);
                    logger.info(mailObject.getTemplateEngine().toUpperCase() + ": successfully sent email to " + mailObject.getTo() + " at " + LocalDateTime.now() + ", with subject " + mailObject.getSubject());
                    
                    results.setStatus(ResultStatus.SUCCESS);
                    results.setMessage("Account created succesfuly! A confirmation email has been sent to " + to + " for verification!");
                    return results;
                } catch (IOException e) {
                    logger.error(mailObject.getTemplateEngine().toUpperCase() + ": Cron job failed to send email to " + mailObject.getTo() + " at " + LocalDateTime.now() + ", with subject " + mailObject.getSubject() + " due to IOException " + e.getLocalizedMessage());
                    e.printStackTrace();
                    results.setStatus(ResultStatus.ERROR);
                    results.setMessage("Account created succesfuly! However we were unable to send a verification email for verification to " + mailObject.getTo() + "! " + e.getLocalizedMessage());
                    return results;
                } catch (MessagingException e) {
                    logger.error(mailObject.getTemplateEngine().toUpperCase() + ": Cron job failed to send email to " + mailObject.getTo() + " at " + LocalDateTime.now() + ", with subject " + mailObject.getSubject() + " due to MessagingException " + e.getLocalizedMessage());
                    e.printStackTrace();
                    results.setStatus(ResultStatus.ERROR);
                    results.setMessage("Account created succesfuly! However we were unable to send a verification email for verification to " + mailObject.getTo() + "! " + e.getLocalizedMessage());
                    return results;
                }
            }else {
            	results.setStatus(ResultStatus.ERROR);
                results.setMessage("Email not sent, Template not found! ");
                return results;
            }
			
		}catch (Exception e) {
			logger.error("Error composing email message to " + to + " :: " + e.getLocalizedMessage());
			results.setStatus(ResultStatus.ERROR);
			results.setMessage("Error composing email message : " + e.getLocalizedMessage());
			return results;
		}
		
	}

}
