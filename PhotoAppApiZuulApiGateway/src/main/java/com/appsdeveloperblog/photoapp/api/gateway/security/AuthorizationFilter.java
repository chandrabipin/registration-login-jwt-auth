package com.appsdeveloperblog.photoapp.api.gateway.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

/**
 * Filter to validate the jwt token
 * @author Bipin.Chandra
 *
 */
public class AuthorizationFilter extends BasicAuthenticationFilter{

	Environment env;
	
	@Autowired
	public AuthorizationFilter(AuthenticationManager authenticationManager, Environment env) {
		super(authenticationManager);
		this.env=env;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String authHeader = request.getHeader(env.getProperty("authorization.token.header.name"));
		//if the header does not have 'Bearer' in its name then simply move to next filter in the chain
		if(authHeader==null || !authHeader.startsWith(env.getProperty("authorization.token.header.prefix"))) {
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		if(authentication!=null)
			SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String authHeader = request.getHeader(env.getProperty("authorization.token.header.name"));
		if(authHeader==null) {
			return null;
		}
		String token = authHeader.replace(env.getProperty("authorization.token.header.prefix"), "").trim();
		
		//Get UserId from JWT token
		String userId = Jwts.parser()
				.setSigningKey(env.getProperty("token.secret"))
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		
		if(userId == null) {
			return null;
		}
		return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
	}
	
	

}
