package com.appsdeveloperblog.photoapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;

public interface UsersService extends UserDetailsService{//UserDetailsService is from Spring

	public UserDto createUser(UserDto userdetails);
	public UserDto getUserDetailsByEmail(String email);
	public UserDto getUserByUserId(String userId);
}
