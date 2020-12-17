package com.appsdeveloperblog.photoapp.api.users.ui.controllers;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.photoapp.api.users.service.UsersService;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.UserResponseModel;

@RestController
@RequestMapping("/users")
public class UsersControllers {

	@Autowired
	Environment env;
	
	@Autowired
	UsersService usersService;
	
	@GetMapping("/status/check")
	public String status() {
		return "Working on port: " + env.getProperty("local.server.port");
	}
	
	@PostMapping(produces= {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON},
			consumes= {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userRequest) {
		
		//Here also we need to map object from source=>userRequest to destination=>userDto
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = modelMapper.map(userRequest, UserDto.class);
		
		UserDto createdUserDto = usersService.createUser(userDto);
		CreateUserResponseModel retVal = modelMapper.map(createdUserDto, CreateUserResponseModel.class);
		//Use Mapper again to map from userDto to CreateuserResponseModel
		
		return ResponseEntity.status(HttpStatus.CREATED).body(retVal);
	}
	
	/**
	 * . Eg. User Microservice will connect with PhotoAlbum microservice to get the photo details of this user.
	 * @param userId
	 * @return
	 */
    @GetMapping(value="/{userId}", produces= {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId){
    	UserDto userDto = usersService.getUserByUserId(userId);
    	UserResponseModel userRespModel = new ModelMapper().map(userDto, UserResponseModel.class);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(userRespModel);
    }
}
