package com.bluesensenetworks.proximitysense.model;

public interface ApiOperationListener<TResponseData> {
	void success(TResponseData responseData);

	void failure(String reason);
}
