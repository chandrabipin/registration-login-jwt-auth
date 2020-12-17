package com.appsdeveloperblog.app.ws.ui.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.ui.model.request.UpdateUserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRestResponseModel;


@RestController
@RequestMapping("users")
public class UserController {

	Map<String, UserRestResponseModel> usersMap;
	@Autowired
	UserService userService;// Dont forget to put @Service annotation on the impl class.

	@GetMapping
	public String getUser(@RequestParam(value = "page", defaultValue = "1", required = false) String page, 
			@RequestParam(value = "limit") String limit) {
		return "get User was called with page:"+page + " limit:" + limit;
	}
	
	/* Working code to return whole object
	@GetMapping(path="/{userId}", 
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public UserRest getUser(@PathVariable String userId) {
		UserRest user = new UserRest();
		user.setEmail("abc@abc.com");
		user.setFirstName("Bipin");
		user.setLastName("Chandra");
		return user;
	}*/
	
/*	@GetMapping(path="/{userId}", 
			produces = {MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE})// produces is same as 'Accept' in HTTP request
	public ResponseEntity<UserRestResponseModel> getUser(@PathVariable String userId) {
		UserRestResponseModel user = new UserRestResponseModel();
		user.setEmail("abc@abc.com");
		user.setFirstName("Bipin");
		user.setLastName("Chandra");
		//return new ResponseEntity(HttpStatus.OK);// only return status with no body
		return new ResponseEntity<UserRestResponseModel>(user, HttpStatus.OK);// return status with body
	}*/
	
	@GetMapping(path="/{userId}", 
			produces = {MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE})// produces is same as 'Accept' in HTTP request
	public ResponseEntity<UserRestResponseModel> getUser(@PathVariable String userId) {
		
		UserRestResponseModel response = null;
		//response.getEmail();// to test an exception handling
		
		//Throw custom exception
		//if (true) throw new UserServiceException("This is my custom exception");
		
		
		if(userId!=null) {
			response =  usersMap.get(userId);	
		}
		if (response==null){
			return new ResponseEntity<UserRestResponseModel>(HttpStatus.NO_CONTENT);//204 - no content
		} else {
			return new ResponseEntity<UserRestResponseModel>(response, HttpStatus.OK);
		}
		
	}

/*
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},// consumes is same as 'Content-Type' in Http request.
			produces ={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<UserRestResponseModel> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails) {//Convert the json request directly into java object
		UserRestResponseModel user = new UserRestResponseModel();
		user.setEmail(userDetails.getEmail());
		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		
		String userId = UUID.randomUUID().toString();
		user.setUserId(userId);
		
		if(usersMap==null) {
			usersMap = new HashMap<>();
		}
		usersMap.put(userId, user);
		return new ResponseEntity<UserRestResponseModel>(user, HttpStatus.CREATED);//201: return status with body
	}
*/
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},// consumes is same as 'Content-Type' in Http request.
			produces ={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<UserRestResponseModel> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails) {//Convert the json request directly into java object
		UserRestResponseModel user = userService.create(userDetails);
		if(usersMap==null) usersMap = new HashMap<String, UserRestResponseModel>();
		usersMap.put(user.getUserId(),user);

		return new ResponseEntity<UserRestResponseModel>(user, HttpStatus.CREATED);//201: return status with body
	}
	
	@PutMapping(path="/{userId}", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},// consumes is same as 'Content-Type' in Http request.
			produces ={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public UserRestResponseModel updateUser(@PathVariable String userId, @Valid @RequestBody UpdateUserDetailsRequestModel updateUserDetails) {
		UserRestResponseModel userDetails = usersMap.get(userId);
		userDetails.setFirstName(updateUserDetails.getFirstName());
		userDetails.setLastName(updateUserDetails.getLastName());
		return userDetails;
	}
	
	@DeleteMapping(path="/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable String userId) {// no consumes and produces since we are not sending or retrieving any data.
		usersMap.remove(userId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);// 204 since no content is returned for this htttp request
	}
}
