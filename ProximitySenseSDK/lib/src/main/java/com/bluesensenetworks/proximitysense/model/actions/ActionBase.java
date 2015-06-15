package com.bluesensenetworks.proximitysense.model.actions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluesensenetworks.proximitysense.model.Sighting;

public abstract class ActionBase implements Parcelable {
	private String appSpecificId;
	private Date createdOn;
	private ZoneEventDetails zoneEvent;
	private Sighting sighting;

	public String getAppSpecificId() {
		return appSpecificId;
	}

	public void setAppSpecificId(String appSpecificId) {
		this.appSpecificId = appSpecificId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public ZoneEventDetails getZoneEvent() {
		return zoneEvent;
	}

	public void setZoneEvent(ZoneEventDetails zoneEvent) {
		this.zoneEvent = zoneEvent;
	}

	public Sighting getSighting() {
		return sighting;
	}

	public void setSighting(Sighting sighting) {
		this.sighting = sighting;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(appSpecificId);
		dest.writeSerializable(createdOn);
		zoneEvent.writeToParcel(dest, flags);
		sighting.writeToParcel(dest, flags);
	}

	protected void readFromParcel(Parcel in) {
		appSpecificId = in.readString();
		createdOn = (Date) in.readSerializable();
		zoneEvent = new ZoneEventDetails(in);
		sighting = new Sighting(in);
	}

	private static Map<String, String> actionTypes = new HashMap<String, String>();

	public static void registerActionType(String name, String className) {
		actionTypes.put(name, className);
	}

	public static String getActionTypeClassName(String actionTypeName) {
		if (actionTypes.containsKey(actionTypeName))
			return actionTypes.get(actionTypeName);

		return "";
	}

	public static void registerCommonActionTypes() {
		registerActionType("richContent", "com.bluesensenetworks.proximitysense.model.actions.RichContentAction");
	}
}
