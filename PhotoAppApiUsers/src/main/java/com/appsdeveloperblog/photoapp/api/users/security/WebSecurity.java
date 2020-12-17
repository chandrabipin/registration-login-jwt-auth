package com.appsdeveloperblog.photoapp.api.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.photoapp.api.users.service.UsersService;

@EnableGlobalMethodSecurity(prePostEnabled=true)//pre and post auth will be anabled
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{

	private UsersService usersService;
	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	private Environment env;
	
	// Constructor base autowire is preferable over field based autowire
	// https://stackoverflow.com/questions/40620000/spring-autowire-on-properties-vs-constructor
	@Autowired
	public WebSecurity(Environment env, UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.env=env;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.usersService = usersService;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		//http.authorizeRequests().antMatchers("/users/**").permitAll();
		//http.authorizeRequests().antMatchers("/**").hasIpAddress(env.getProperty("zuulapigateway.ip"))
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/users").hasIpAddress(env.getProperty("zuulapigateway.ip"))// no auth is req
		.antMatchers("/h2-console/**").permitAll() // no auth is req
		.anyRequest().authenticated() // all other url will need to be authenticated
		.and()
		.addFilter(getAuthenticationFilter())
		.addFilter(new AuthorizationFilter(authenticationManager(), env));
		http.headers().frameOptions().disable();//This is done for h2-console ui.
	}

	private AuthenticationFilter getAuthenticationFilter() throws Exception {
		AuthenticationFilter filter = new AuthenticationFilter(usersService, env, authenticationManager());
		//filter.setAuthenticationManager(authenticationManager());
		filter.setFilterProcessesUrl("/users/login");
		return filter;
	}

	// Let Springboot framework know which userservice to load user details and which service is used to encode the password.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
	}

}
