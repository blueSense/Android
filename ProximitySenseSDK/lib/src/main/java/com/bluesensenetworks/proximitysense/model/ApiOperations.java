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
import com.google.gson.reflect.TypeToken;

public class ApiOperations {
	protected static final String INTENT_ACTION_RECEIVED = "com.bluesensenetworks.proximitysense.INTENT_ACTION_RECEIVED";
	protected static final String EXTRA_ACTION = "com.bluesensenetworks.proximitysense.EXTRA_ACTION";
	private static final String TAG = ApiOperations.class.getSimpleName();
	private static ApiOperations instance;
	private Context context;
	private RequestQueue requestQueue;
	private ApiCredentials apiCredentials;
	private AppUser appUser = new AppUser();
	private String apiBaseUrl = "https://platform.proximitysense.com/api/v1/";

	private class PostResponseData {
		public String result;
	}

	public ApiOperations(Context context) {
		this(context, Volley.newRequestQueue(context));
	}

	public ApiOperations(Context context, RequestQueue queue) {
		this.context = context;
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

	public void requestAuthKeyPairForUser(String username, String password, final ApiOperationListener<ApiCredentials> responseListener) {
		Log.i(TAG, "Login request, username: " + username);

		requestQueue.add(new LoginRequest(apiBaseUrl, username, password, new Listener<ApiCredentials>() {

			@Override
			public void onResponse(ApiCredentials apiCredentials) {
				setApiCredentials(apiCredentials);
				if (responseListener != null) {
					responseListener.success(apiCredentials);
				}
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Failed to authenticate user: " + error.getMessage());
				if (responseListener != null) {
					responseListener.failure(error.getLocalizedMessage());
				}
			}
		}));
	}

	public void requestBeaconDetails(final DetectedBeacon beacon, final ApiOperationListener<DetectedBeacon> responseListener) {
		Log.i(TAG, "Request beacon configuration, serialNumber: " + beacon.getSerialNumber());
		String url = apiBaseUrl + "configuration?includeAll=true&serialNumber=" + beacon.getSerialNumber();

		ApiRequest<BeaconResponseData> getBeaconRequest = new ApiRequest<>(BeaconResponseData.class, apiCredentials, Method.GET, url, null,
				new Listener<BeaconResponseData>() {
					@Override
					public void onResponse(BeaconResponseData response) {
						response.writeToBeacon(beacon);
						if (responseListener != null) {
							responseListener.success(beacon);
						}
					}
				}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Failed to retrieve beacon configuration from platform: " + error.getMessage());
				if (responseListener != null) {
					responseListener.failure(error.getLocalizedMessage());
				}
			}
		});
		requestQueue.add(getBeaconRequest);
	}

	public void updateBeaconDetails(DetectedBeacon beacon) {
		Log.i(TAG, "Update beacon configuration, serialNumber: " + beacon.getSerialNumber());
		String url = apiBaseUrl + "configuration?serialNumber=" + beacon.getSerialNumber();

		requestQueue.add(new ApiRequest<PostResponseData>(PostResponseData.class, apiCredentials, Method.POST, url, buildRequestBody(new UpdateBeaconRequestData(beacon)),
				new Listener<PostResponseData>() {
					@Override
					public void onResponse(PostResponseData response) {
						Log.i(TAG, "Successfully updated beacon configuration on platform.");
					}
				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to update beacon configuration on platform: " + error.getMessage());
					}
				}));
	}

	public void registerBeacon(final DetectedBeacon beacon, final ApiOperationListener<DetectedBeacon> responseListener) {
		Log.i(TAG, "Register not owned beacon, serialNumber: " + beacon.getSerialNumber());
		String url = apiBaseUrl + "beaconRegistration?serialNumber=" + beacon.getSerialNumber();

		requestQueue.add(new ApiRequest<PostResponseData>(PostResponseData.class, apiCredentials, Method.POST, url, null, new Listener<PostResponseData>() {
			@Override
			public void onResponse(PostResponseData response) {
				if (responseListener != null) {
					responseListener.success(beacon);
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Failed to register beacon: " + error.getMessage());
				if (responseListener != null) {
					responseListener.failure(error.getLocalizedMessage());
				}
			}
		}));
	}

	public void reportBeaconSightings(List<Beacon> beacons) {
		Log.i(TAG, "Report beacon sightings");

		List<Sighting> sightings = new ArrayList<Sighting>();
		for (Beacon beacon : beacons) {
			Sighting sighting = Sighting.createFromBeacon(beacon);
			sightings.add(sighting);
			Log.i(TAG,
					String.format("Sighting of beacon %s:%s:%s at %s proximity", sighting.getUuid(),
							sighting.getMajor(), sighting.getMinor(), sighting.getProximity()));
		}

		String url = apiBaseUrl + "ranging?willPoll=true";

		requestQueue.add(new ApiAppRequest<String>(String.class, apiCredentials, appUser, Method.POST, url, buildRequestBody(sightings),
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.i(TAG, "Successfully reported beacon sightings to platform.");
					}

				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to report beacon sightings to platform: " + error.getMessage());
					}
				}));
	}

	public void requestAvailableActionResults(final ApiOperationListener<List<ActionBase>> responseListener) {
		Log.i(TAG, "Request decision");

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
						Log.e(TAG, "Failed to retrieve actions from platform: " + error.getMessage());
						if (responseListener != null) {
							responseListener.failure(error.getLocalizedMessage());
						}
					}
				}));
	}

	public void requestApplications(final ApiOperationListener<List<Application>> responseListener) {
		Log.i(TAG, "Request applications");

		requestQueue.add(new ApiRequest<List<Application>>(new TypeToken<List<Application>>() {
		}.getType(), apiCredentials, Method.GET, apiBaseUrl + "applications", null,
				new Listener<List<Application>>() {
					@Override
					public void onResponse(List<Application> applications) {
						Log.i(TAG, "Received " + applications.size() + " applications.");
						if (responseListener != null)
							responseListener.success(applications);
					}
				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to retrieve applications from platform: " + error.getMessage());
						if (responseListener != null) {
							responseListener.failure(error.getLocalizedMessage());
						}
					}
				}));
	}

	public void requestProfile(final ApiOperationListener<UserProfile> responseListener) {
		Log.i(TAG, "Request user profile");

		requestQueue.add(new ApiAppRequest<UserProfile>(UserProfile.class, apiCredentials, appUser, Method.GET,
				apiBaseUrl + "profile", null,
				new Listener<UserProfile>() {
					@Override
					public void onResponse(UserProfile userProfile) {
						if (responseListener != null)
							responseListener.success(userProfile);
					}
				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Failed to retrieve user profile from platform: " + error.getMessage());
						if (responseListener != null)
							responseListener.failure(error.getLocalizedMessage());
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
						Log.e(TAG, "Failed to send App User details to platform: " + error.getMessage());
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
						if (responseListener != null)
							responseListener.failure(error.getLocalizedMessage());
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
						if (responseListener != null)
							responseListener.failure(error.getLocalizedMessage());
					}
				}));
	}
}