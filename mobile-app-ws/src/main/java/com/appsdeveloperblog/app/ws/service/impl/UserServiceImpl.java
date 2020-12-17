package com.appsdeveloperblog.app.ws.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRestResponseModel;
import com.appsdeveloperblog.app.ws.util.Util;

@Service // without this annotation this will not be autowired.
public class UserServiceImpl implements UserService{

	Map<String, UserRestResponseModel> usersMap;

	@Autowired
	Util util;

	@Override
	public UserRestResponseModel create(UserDetailsRequestModel userDetails) {
		UserRestResponseModel user = new UserRestResponseModel();
		user.setEmail(userDetails.getEmail());
		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		
		String userId = util.generateUUID();
		user.setUserId(userId);
		
		if(usersMap==null) {
			usersMap = new HashMap<>();
		}
		usersMap.put(userId, user);
		
		return user;
	}

}
