package com.appsdeveloperblog.photoapp.api.users.feign;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.appsdeveloperblog.photoapp.api.users.ui.model.AlbumResponseModel;

/**
 * This is the Fein client
 * @author Bipin.Chandra
 *
 */
@FeignClient(name="albums-ws", fallback=AlbumsFallback.class)
public interface AlbumsServiceFeignClient {

	/**
	 * This method will call the albums-ws's Controller class which has this matching GetMapping
	 */
	@GetMapping("/users/{id}/albums")
	public List<AlbumResponseModel> getAlbum(@PathVariable String id);
	
}

/**
 * 
 * @author Bipin.Chandra
 *
 */
@Component
class AlbumsFallback implements AlbumsServiceFeignClient{

	/**
	 * This method will be invoked if the call to the microservice fails
	 */
	@Override
	public List<AlbumResponseModel> getAlbum(String id) {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}
	
}