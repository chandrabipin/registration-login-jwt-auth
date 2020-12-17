package com.appsdeveloperblog.app.ws.service;

import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRestResponseModel;

public interface UserService {
	
	UserRestResponseModel create(UserDetailsRequestModel userDetails);

}
