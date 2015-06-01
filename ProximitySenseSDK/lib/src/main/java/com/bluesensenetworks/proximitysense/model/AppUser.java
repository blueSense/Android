package com.bluesensenetworks.proximitysense.model;

import java.util.Map;

public class AppUser {
	private String appSpecificId = "Default - Id not set";
	private Map<String, String> userMetadata;

	public String getAppSpecificId() {
		return appSpecificId;
	}

	public void setAppSpecificId(String appSpecificId) {
		this.appSpecificId = appSpecificId;
	}

	public Map<String, String> getUserMetadata() {
		return userMetadata;
	}

	public void setUserMetadata(Map<String, String> userMetadata) {
		this.userMetadata = userMetadata;
	}

	public AppUser() {
	}

	public AppUser(String appSpecificId) {
		this.appSpecificId = appSpecificId;
	}

	public AppUser(String appSpecificId, Map<String, String> userMetadata) {
		this.appSpecificId = appSpecificId;
		this.userMetadata = userMetadata;
	}
}
