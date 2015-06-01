package com.bluesensenetworks.proximitysense.model;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpStatus;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.bluesensenetworks.proximitysense.model.actions.ActionBase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

class GetActionsRequest extends ApiAppRequest<List<ActionBase>> {

	public GetActionsRequest(ApiCredentials apiCredentials, AppUser appUser, Listener<List<ActionBase>> listener,
	                         ErrorListener errorListener) {
		super(new TypeToken<List<ActionBase>>() {
		}.getType(), apiCredentials, appUser, Method.GET, ApiOperations.API_ROOT + "decision", null, listener, errorListener);
	}

	@Override
	protected Response<List<ActionBase>> parseNetworkResponse(NetworkResponse response) {

		List<ActionBase> actions = null;

		if (response.statusCode == HttpStatus.SC_OK) {
			try {
				String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

				Gson gson = new GsonBuilder().registerTypeAdapter(ActionBase.class, new ActionBaseAdapter())
						.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

				actions = Arrays.asList(gson.fromJson(jsonString, ActionBase[].class));

			} catch (Exception e) {
				return Response.error(new ParseError(e));
			}
		}

		return Response.success(actions, HttpHeaderParser.parseCacheHeaders(response));
	}

	class ActionBaseAdapter implements JsonDeserializer<ActionBase> {
		private static final String MEMBER_TYPE = "type";
		private static final String MEMBER_INSTANCE = "result";

		@Override
		public ActionBase deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			JsonObject json = jsonElement.getAsJsonObject();
			String typeName = json.get(MEMBER_TYPE).getAsString();
			String className = ActionBase.getActionTypeClassName(typeName);

			Class<?> instanceType = null;
			try {
				instanceType = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new JsonParseException(e.getMessage());
			}

			return context.deserialize(json.get(MEMBER_INSTANCE), instanceType);
		}
	}
}
