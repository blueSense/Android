package com.bluesensenetworks.proximitysense.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.altbeacon.beacon.Beacon;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bluesensenetworks.proximitysense.integrations.Integrations;
import com.bluesensenetworks.proximitysense.model.actions.ActionBase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ApiOperations {
	private static final String TAG = ApiOperations.class.getSimpleName();
	private static ApiOperations instance;
	private RequestQueue requestQueue;
	private ApiCredentials apiCredentials;
	private AppUser appUser = new AppUser();
	private String apiBaseUrl = "https://platform.proximitysense.com/api/v1/";

	private class PostResponseData {
		public String result;
	}

	public ApiOperations(Context context) {
		this(Volley.newRequestQueue(context));
	}

	public ApiOperations(RequestQueue queue) {
		this.requestQueue = queue;

		ActionBase.registerCommonActionTypes();
		Integrations.registerCommonActionTypes();
	}

	public static ApiOperations getInstanceForApplication(Context context) {
		if (instance == null) {
			instance = new ApiOperations(context.getApplicationContext());
		}

		return instance;
	}

	public String getBaseUrl() {
		return apiBaseUrl;
	}

	public void setBaseUrl(String apiBaseUrl) {
		this.apiBaseUrl = apiBaseUrl;
	}

	public ApiCredentials getApiCredentials() {
		return apiCredentials;
	}

	public void setApiCredentials(ApiCredentials apiCredentials) {
		this.apiCredentials = apiCredentials;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public void reportBeaconSightings(List<Beacon> beacons) {
		Log.i(TAG, "Report beacon sightings");

		List<Sighting> sightings = new ArrayList<Sighting>();
		for (Beacon beacon : beacons) {
			Sighting sighting = Sighting.createFromBeacon(beacon);
			sightings.add(sighting);
		}

		String url = apiBaseUrl + "ranging";

		requestQueue.add(new ApiAppRequest<String>(String.class, apiCredentials, appUser, Method.POST, url, buildRequestBody(sightings),
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
					}

				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to report beacon sightings: " + error.getMessage());
					}
				}));
	}

	public void requestAvailableActionResults(final ApiOperationListener<List<ActionBase>> responseListener) {
		requestQueue.add(new GetActionsRequest(apiBaseUrl, apiCredentials, appUser,
				new Listener<List<ActionBase>>() {
					@Override
					public void onResponse(List<ActionBase> actions) {
						if (actions != null) {
							Log.i(TAG, "Received " + actions.size() + " actions.");
							if (responseListener != null)
								responseListener.success(actions);
						}
					}
				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to retrieve actions: " + error.getMessage());
						if (responseListener != null && error != null) {
							responseListener.failure(error.networkResponse != null ? error.networkResponse.statusCode : 0, error.getLocalizedMessage());
						}
					}
				}));
	}

	public void updateAppUser() {
		Log.i(TAG, "Update AppUser");

		requestQueue.add(new ApiAppRequest<String>(String.class, apiCredentials, appUser, Method.POST,
				apiBaseUrl + "appUser", buildRequestBody(new AppUserRequestData(this.appUser)),
				new Listener<String>() {
					@Override
					public void onResponse(String s) {
					}
				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to send App User details: " + error.getMessage());
					}
				}));
	}

	private class AppUserRequestData {
		Map<String, String> userMetadata;

		public AppUserRequestData(AppUser appUser) {
			this.userMetadata = appUser.getUserMetadata();
		}
	}

	private static String buildRequestBody(Object requestData) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.toJson(requestData);
	}

	public <TResponseData> void requestForEndpoint(String endpoint,
	                                               final ApiOperationListener<TResponseData> responseListener, Type responseDataType) {

		requestQueue.add(new ApiAppRequest<TResponseData>(responseDataType, apiCredentials, appUser, Method.GET,
				apiBaseUrl + endpoint, null,
				new Listener<TResponseData>() {
					@Override
					public void onResponse(TResponseData responseData) {
						if (responseListener != null)
							responseListener.success(responseData);
					}
				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to retrieve data from platform: " + error.getMessage());
						if (responseListener != null && error != null) {
							responseListener.failure(error.networkResponse != null ? error.networkResponse.statusCode : 0, error.getLocalizedMessage());
						}
					}
				}));
	}

	public <TRequestData, TResponseData> void requestForEndpoint(String endpoint, TRequestData requestData,
	                                                             final ApiOperationListener<TResponseData> responseListener, final Type responseDataType) {

		requestQueue.add(new ApiAppRequest<TResponseData>(responseDataType, apiCredentials, appUser, Method.POST,
				apiBaseUrl + endpoint, buildRequestBody(requestData),
				new Listener<TResponseData>() {
					@Override
					public void onResponse(TResponseData responseData) {
						if (responseListener != null)
							responseListener.success(responseData);
					}
				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to send data to platform: " + error.getMessage());
						if (responseListener != null && error != null) {
							responseListener.failure(error.networkResponse != null ? error.networkResponse.statusCode : 0, error.getLocalizedMessage());
						}
					}
				}));
	}
}
