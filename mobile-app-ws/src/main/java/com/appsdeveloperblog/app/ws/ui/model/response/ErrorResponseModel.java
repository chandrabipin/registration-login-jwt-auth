package com.appsdeveloperblog.app.ws.ui.model.response;

import java.util.Date;

public class ErrorResponseModel {

	private Date timestamp;
	private String message;

	public ErrorResponseModel() {
	}

	public ErrorResponseModel(Date t, String m) {
		this.timestamp = t;
		this.message = m;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
