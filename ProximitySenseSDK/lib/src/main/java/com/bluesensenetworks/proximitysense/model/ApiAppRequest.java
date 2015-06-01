package com.bluesensenetworks.proximitysense.model;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

class ApiAppRequest<T> extends ApiRequest<T> {

	protected AppUser appUser;

	public ApiAppRequest(Type responseDataType, ApiCredentials apiCredentials, AppUser appUser, int method, String url, String requestBody,
			Listener<T> listener, ErrorListener errorListener) {
		super(responseDataType, apiCredentials, method, url, requestBody, listener, errorListener);
		this.appUser = appUser;
	}

	@Override
	public Map<String, String> getHeaders() {
		HashMap<String, String> headers = (HashMap<String, String>) super.getHeaders();
		headers.put("X-ProximitySense-AppUserId", appUser.getAppSpecificId());

		return headers;
	}
}
