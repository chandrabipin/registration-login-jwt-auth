package com.appsdeveloperblog.photoapp.api.users.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.photoapp.api.users.data.UserEntity;
import com.appsdeveloperblog.photoapp.api.users.data.repository.UsersRepository;
import com.appsdeveloperblog.photoapp.api.users.feign.AlbumsServiceFeignClient;
import com.appsdeveloperblog.photoapp.api.users.service.UsersService;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;

@Service
public class UsersServiceImpl implements UsersService{
	
	UsersRepository usersRepo;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	//RestTemplate restTemplate; //Using Feign Client and hence commenting RestTemplate
	AlbumsServiceFeignClient albumsServiceFeignClient;
	Environment env;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public UsersServiceImpl() {
		
	}

	@Autowired // For autowire to work we need to create Bean for this. We created Bean in the Main class.
	public UsersServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder,
			UsersRepository usersRepo,
			//RestTemplate restTemplate,
			AlbumsServiceFeignClient albumsServiceFeignClient,
			Environment env) {
		this.bCryptPasswordEncoder=bCryptPasswordEncoder;
		this.usersRepo=usersRepo;
		//this.restTemplate=restTemplate;
		this.albumsServiceFeignClient=albumsServiceFeignClient;
		this.env=env;
	}

	public UserDto createUser(UserDto userDto) {
		userDto.setUserId(UUID.randomUUID().toString());
		userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		//Before we save user entity to database, we need to copy the user Dto object to the entity object. 
		// And to do that we need a model mapper.
		// fields from source object must match with the fields os the destination object.
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		//Then save the user entity to the database using UsersRepository
		usersRepo.save(userEntity);
		
		// Use Mapper to map source as userEntity to destination as userDto
		UserDto retVal = modelMapper.map(userEntity, UserDto.class);
		
		return retVal;
	}

	@Override // UserDetails and User are Spring Framework class
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = usersRepo.findByEmail(username);// username here is email
		if(userEntity == null ) throw new UsernameNotFoundException(username);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = usersRepo.findByEmail(email);// username here is email
		if(userEntity == null ) throw new UsernameNotFoundException(email);
		
		return new ModelMapper().map(userEntity, UserDto.class);
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = usersRepo.findByUserId(userId);
		if(userEntity == null ) throw new UsernameNotFoundException("User not found");
		
		//Copy User entity to userDto
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//Uncomment this if useing RestTemplate
/*
		//Use microservice name used in Eureka. This way we dont need to hardcode the ip and port of that microservice
		//It will get the details from Eureka Discovery Service
		//Then RestTemplate will use this value to load balance the request to the Albums microservice
		String albumsUrl=String.format(env.getProperty("albums.url"), userId);
		
		//Use RestTemplate to communicate with the Albums microservice
		ResponseEntity<List<AlbumResponseModel>> albumListReponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>(){});
		List<AlbumResponseModel> albumList = albumListReponse.getBody();
*/
		logger.info("Before calling album microservice");
		List<AlbumResponseModel> albumList=null;
		try {
			albumList = albumsServiceFeignClient.getAlbum(userId);
		} catch (FeignException e) {
			logger.error(e.getLocalizedMessage());
		}
		logger.info("After calling album microservice");
		
		userDto.setAlbumList(albumList);
		return userDto;
	}

}
