package com.bluesensenetworks.proximitysense.model;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.bluesensenetworks.proximitysense.ProximitySenseSDK;
import com.bluesensenetworks.proximitysense.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class ApiRequest<T> extends JsonRequest<T> {

	private static final String TAG = ApiRequest.class.getSimpleName();

	private Type responseDataType;
	private String requestBody;
	private ApiCredentials apiCredentials;

	public ApiRequest(Type responseDataType, ApiCredentials apiCredentials, int method, String url,
			String requestBody, Listener<T> listener, ErrorListener errorListener) {

		super(method, url, requestBody, listener, errorListener);

		if (apiCredentials == null) {
			throw new IllegalArgumentException("apiCredentials cannot be null.");
		}

		this.responseDataType = responseDataType;
		this.requestBody = requestBody;
		this.apiCredentials = apiCredentials;
	}

	@Override
	public Map<String, String> getHeaders() {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("X-Authorization-ClientId", apiCredentials.getClientId());
		headers.put("X-Authorization-Signature", Utils.generateHash(this.getUrl() + (requestBody == null ? "" : requestBody) + apiCredentials.getPrivateKey()).toLowerCase(Locale.US));
		headers.put("X-ProximitySense-SdkPlatformAndVersion", String.format("Android %s - %s", android.os.Build.VERSION.RELEASE, ProximitySenseSDK.PROXIMITYSENSESDK_VERSION));

		return headers;
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {

		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			T responseObject = gson.fromJson(jsonString, responseDataType);
			return Response.success(responseObject, HttpHeaderParser.parseCacheHeaders(response));
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected VolleyError parseNetworkError(VolleyError volleyError) {
		Log.w(TAG, "Error response", volleyError);

		return volleyError;
	}
}

@SuppressWarnings("serial")
class BsnApiError extends VolleyError {

	public BsnApiError(String message) {
		super(message);
	}
}
