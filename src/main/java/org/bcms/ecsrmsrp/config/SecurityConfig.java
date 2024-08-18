/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.config;

import org.bcms.ecsrmsrp.components.LoginFailureHandler;
import org.bcms.ecsrmsrp.components.LoginSuccessHandler;
import org.bcms.ecsrmsrp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */
@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfig {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired LoginSuccessHandler loginSuccessHandler;
	@Autowired LoginFailureHandler loginFailureHandler;
	
	@Bean
	UserDetailsService userDetailService() {
		return new UserService();
	}
		
	// Password Encoding 
    @Bean
    PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }
    
    // Configuring HttpSecurity 
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
    {
        return http
        		//.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/profile/register", "/profile/save","/","/error",
                        		"/login","/assets/**", "/fa/**","/favicon.ico").permitAll()
                        .requestMatchers("/auth/success","/auth/error","/auth/verify/**","/profile/error","/profile/success").permitAll())
                .authorizeHttpRequests(requests -> requests
                		.requestMatchers("/**").authenticated())
                .authorizeHttpRequests(requests -> requests
                		.requestMatchers("/api-docs/**", "/swagger-ui/**", 
                				"/swagger-ui.html", "/actuator/**",
                				"/api-documentation").permitAll())
                .sessionManagement(session -> session    
                		.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                		.sessionAuthenticationErrorUrl("/login?error=true&reason=max session")
                		.invalidSessionUrl("/login?error=true&reason=invalid session")
                		.sessionFixation().migrateSession()//ensures that Spring Security uses cookies for session tracking and prevents URL rewriting, enhancing the security of your application.
                        .maximumSessions(2).maxSessionsPreventsLogin(true)
                        .expiredUrl("/login?error=true&reason=session expired"))
                .authenticationProvider(authenticationProvider())
                //.addFilterBefore(authFilter, 
                //		UsernamePasswordAuthenticationFilter.class)
                .formLogin((form) -> form                		
        				.loginPage("/login")
        				.loginProcessingUrl("/login")
        				.failureUrl("/login-error.html")
                        .defaultSuccessUrl("/auth/otp")
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
        				.permitAll()
        			)
        			.logout(
        					(logout) -> logout
        					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        					.logoutSuccessUrl("/login?logout=true&reason=You have been succesfully logged out!")
        					.invalidateHttpSession(true)
        					.deleteCookies("JSESSIONID")
        					.permitAll()
        					.addLogoutHandler(new LogoutHandler() {
								
								@Override
								public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
									try {
										request.logout();
									}catch (Exception e) {
										logger.error("Logout error: " + e.getLocalizedMessage());
									}
									
								}
							})
        				)
                .build(); 
    }
    
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
    
    @Bean
    AuthenticationProvider authenticationProvider() { 
	        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
	        authenticationProvider.setUserDetailsService(userDetailService()); 
	        authenticationProvider.setPasswordEncoder(passwordEncoder()); 
	        return authenticationProvider; 
	    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
	        return config.getAuthenticationManager(); 
    }

}
