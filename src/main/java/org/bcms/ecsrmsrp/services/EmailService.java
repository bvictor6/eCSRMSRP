/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 31, 2024
*/
package org.bcms.ecsrmsrp.services;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * 
 */
@Service
public class EmailService {
	private static final String NOREPLY_ADDRESS = "noreply@ecsrm.com";

	@Autowired SpringTemplateEngine thymeleafTemplateEngine;

	private JavaMailSender emailSender;
	
	public EmailService(JavaMailSender javaMailSender) {

		this.emailSender = javaMailSender;
	}

	public void sendMessageUsingThymeleafTemplate(String to, String subject,
												  Map<String, Object> templateModel)
			throws IOException, MessagingException {
		Context context = new Context();  //Thymeleaf context
		context.setVariables(templateModel);

		String htmlBody = thymeleafTemplateEngine.process("email", context);
		sendHtmlMessage(to, subject, htmlBody);
	}

	public void sendNotificationUsingThymeleafTemplate(String to, String subject,
												  Map<String, Object> templateModel)
			throws IOException, MessagingException {
		Context context = new Context();  //Thymeleaf context
		context.setVariables(templateModel);

		String htmlBody = thymeleafTemplateEngine.process("generic-email-template", context);
		sendHtmlMessage(to, subject, htmlBody);
	}


	private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
		try { // added try-catch blocks for error handling
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		    helper.setFrom(NOREPLY_ADDRESS);
		    helper.setTo(to);

		    helper.setSubject(subject);
			helper.setText(htmlBody, true);
			helper.addInline("attachment.png", new ClassPathResource("/logo.png"));
		    emailSender.send(message);
	    } catch (MessagingException e) {
		     throw new MessagingException("Failed to send email to " + to, e);
        }
    }

}
