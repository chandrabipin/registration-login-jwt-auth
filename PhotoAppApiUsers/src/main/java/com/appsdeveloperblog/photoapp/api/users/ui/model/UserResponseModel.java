package com.appsdeveloperblog.photoapp.api.users.ui.model;

import java.util.List;


public class UserResponseModel {
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	//return list of albums in the response for this user
	private List<AlbumResponseModel> albumList;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<AlbumResponseModel> getAlbumList() {
		return albumList;
	}
	public void setAlbumList(List<AlbumResponseModel> albumList) {
		this.albumList = albumList;
	}
}
