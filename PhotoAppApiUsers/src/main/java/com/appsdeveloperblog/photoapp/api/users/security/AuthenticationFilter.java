package com.appsdeveloperblog.photoapp.api.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.appsdeveloperblog.photoapp.api.users.service.UsersService;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.LoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * This is our own custom filter class
 * @author Bipin.Chandra
 *
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UsersService usersService;
	private Environment env;
	
	public AuthenticationFilter() {}
	
	@Autowired
	public AuthenticationFilter(UsersService usersService, Environment env, AuthenticationManager authenticationManager) {
		this.usersService=usersService;
		this.env=env;
		super.setAuthenticationManager(authenticationManager);
	}
	
	//This is where we compare if userid and password is correct
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			LoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getEmail(), 
							creds.getPassword(), 
							new ArrayList<>()));
		}catch (Exception e) {
		}
		// TODO Auto-generated method stub
		return super.attemptAuthentication(request, response);
	}

	// If auth is success spring will call this method
	// We will use this methos to gnerate the JWT and set it in http response
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String userName = ((User)authResult.getPrincipal()).getUsername();
		UserDto userDto = usersService.getUserDetailsByEmail(userName);//userName~email
		
		//Add jwt dependency
		String token = Jwts.builder()
				.setSubject(userDto.getEmail())
				.setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(env.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))//token.secret is the key
				.compact();
		//Any client applictaion will make use of this Token and UserId for further requests
		response.addHeader("Token", token);
		response.addHeader("UserId", userDto.getUserId());

	}
	
	

}
