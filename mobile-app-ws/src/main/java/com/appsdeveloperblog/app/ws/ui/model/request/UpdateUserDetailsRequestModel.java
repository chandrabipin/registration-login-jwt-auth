package com.appsdeveloperblog.app.ws.ui.model.request;

import javax.validation.constraints.NotNull;

public class UpdateUserDetailsRequestModel {
	@NotNull(message="First Name can not be null")
	private String firstName;
	@NotNull(message="Last Name can not be null")
	private String lastName;
	
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
}
