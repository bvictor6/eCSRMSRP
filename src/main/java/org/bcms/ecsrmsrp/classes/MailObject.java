/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Apr 16, 2024
*/
package org.bcms.ecsrmsrp.classes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@Getter
@Setter
@NoArgsConstructor
public class MailObject {
	@Email
    @NotNull
    @Size(min = 1, message = "Please, set an email address to send the message to it")
    private String to;
    private String recipientName;
    private String subject;
    private String text;
    private String senderName;
    private String templateEngine;
    private String appLoginUrl;
    private String mailCopyrightYear;
    private String verificationLink;

}
