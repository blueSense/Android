package com.bluesensenetworks.proximitysense.model.actions;

import android.os.Parcel;
import android.os.Parcelable;

public class ZoneEventDetails implements Parcelable {
	private String zoneName;
	private String eventType;

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public ZoneEventDetails() {
	}

	public ZoneEventDetails(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<ZoneEventDetails> CREATOR = new Parcelable.Creator<ZoneEventDetails>() {
		public ZoneEventDetails createFromParcel(Parcel in) {
			return new ZoneEventDetails(in);
		}

		public ZoneEventDetails[] newArray(int size) {
			return new ZoneEventDetails[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(zoneName);
		dest.writeString(eventType);
	}

	protected void readFromParcel(Parcel in) {
		zoneName = in.readString();
		eventType = in.readString();
	}
}
