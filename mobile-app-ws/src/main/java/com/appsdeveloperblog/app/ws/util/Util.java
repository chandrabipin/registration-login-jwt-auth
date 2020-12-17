package com.appsdeveloperblog.app.ws.util;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class Util {

	public String generateUUID() {
		return UUID.randomUUID().toString();
	}
}
