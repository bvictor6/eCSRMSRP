/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   22 Jul 2024
 */
package org.bcms.ecsrmsrp.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/**
 * 
 */

@Configuration
public class OpenAPIConfiguration {
	
	@Bean
	OpenAPI configureOpenAPI() {
		Server server = new Server();
		server.setUrl("http://localhost:8088");
	    server.setDescription("API Development Server");

        Contact myContact = new Contact();
        myContact.setName("Victor Bruna");
        myContact.setEmail("bvictor@ymail.com");

        Info information = new Info()
               .title("Botswana eCSRMSRP API's")
               .version("1.0")
               .description("This API exposes endpoints for the eCSRMSRP portal.")
               .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
		
	}

}
