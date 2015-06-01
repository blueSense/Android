package com.bluesensenetworks.proximitysense.model;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

class LoginRequest extends JsonRequest<ApiCredentials> {

	private static final String KEY_CLIENT_ID = "clientId";
	private static final String KEY_PRIVATE_KEY = "privateKey";

	public LoginRequest(String username, String password, Listener<ApiCredentials> listener, ErrorListener errorListener) {

		super(Method.POST, ApiOperations.API_ROOT + "auth/BeginSession",
				BuildRequestBody(username, password), listener, errorListener);
	}

	@Override
	protected Response<ApiCredentials> parseNetworkResponse(NetworkResponse response) {

		ApiCredentials session = null;

		if (response.statusCode == HttpStatus.SC_OK) {
			try {
				String jsonString = new String(response.data,
						HttpHeaderParser.parseCharset(response.headers));

				JSONObject json = new JSONObject(jsonString);

				if (json.has(KEY_CLIENT_ID) && json.has(KEY_PRIVATE_KEY))
					session = new ApiCredentials(json.getString(KEY_CLIENT_ID),
							json.getString(KEY_PRIVATE_KEY));

			} catch (UnsupportedEncodingException e) {
				return Response.error(new ParseError(e));
			} catch (JSONException je) {
				return Response.error(new ParseError(je));
			}
		}

		return Response.success(session,
				HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected VolleyError parseNetworkError(VolleyError volleyError) {
		if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
			return new BsnApiError("Bad credentials");
		}

		return volleyError;
	}

	private static String BuildRequestBody(String username, String password) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);

		return new JSONObject(params).toString();
	}
}
