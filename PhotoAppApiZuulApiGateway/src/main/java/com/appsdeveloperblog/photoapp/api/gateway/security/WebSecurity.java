package com.appsdeveloperblog.photoapp.api.gateway.security;

import javax.ws.rs.HttpMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	
	Environment env;

	@Autowired
	public WebSecurity(Environment env) {
		this.env=env;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();

		http.authorizeRequests()
		//allow endpoints for zuul's springboot actuator
		.antMatchers(env.getProperty("api.zuul.actuator.url.path")).permitAll()
		//allow endpoints for all other microservices springboot actuator
		.antMatchers(env.getProperty("api.users.actuator.url.path")).permitAll()
		//public url for h2-console
		.antMatchers(env.getProperty("api.h2console.url.path")).permitAll()
		//public url for registration
		.antMatchers(HttpMethod.POST, env.getProperty("api.registration.url.path")).permitAll()
		//protected url for login
		.antMatchers(HttpMethod.POST, env.getProperty("api.login.url.path")).permitAll()
		// allow all other requests only if authenticated
		.anyRequest().authenticated()
		//register the AuthorizationFilter
		.and()
		.addFilter(new AuthorizationFilter(authenticationManager(), env));

		//Disabling any caching on zuul api gateway
		// zuul api gateway is stateless
		// zuul api gateway will never create a session
		//http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	
}
