package com.bluesensenetworks.proximitysense.integrations.treasurehunt;

import android.os.Parcel;
import android.os.Parcelable;

public class TreasureHuntLocation implements Parcelable {
	private boolean isMandatoryToWin;
	private boolean discovered;

	private String zoneId;

	private String message;
	private String locationName;
	private String locationDescription;
	private String imageUrl;
	private String latitude;
	private String longitude;
	private String address;

	public boolean isMandatoryToWin() {
		return isMandatoryToWin;
	}

	public void setMandatoryToWin(boolean isMandatoryToWin) {
		this.isMandatoryToWin = isMandatoryToWin;
	}

	public boolean isDiscovered() {
		return discovered;
	}

	public void setDiscovered(boolean discovered) {
		this.discovered = discovered;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public TreasureHuntLocation() {
	}

	public TreasureHuntLocation(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<TreasureHuntLocation> CREATOR = new Parcelable.Creator<TreasureHuntLocation>() {
		public TreasureHuntLocation createFromParcel(Parcel in) {
			return new TreasureHuntLocation(in);
		}

		public TreasureHuntLocation[] newArray(int size) {
			return new TreasureHuntLocation[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (isMandatoryToWin ? 1 : 0));
		dest.writeByte((byte) (discovered ? 1 : 0));
		dest.writeString(zoneId);
		dest.writeString(message);
		dest.writeString(locationName);
		dest.writeString(locationDescription);
		dest.writeString(imageUrl);
		dest.writeString(latitude);
		dest.writeString(longitude);
		dest.writeString(address);
	}

	protected void readFromParcel(Parcel in) {
		isMandatoryToWin = in.readByte() != 0;
		discovered = in.readByte() != 0;
		zoneId = in.readString();
		message = in.readString();
		locationName = in.readString();
		locationDescription = in.readString();
		imageUrl = in.readString();
		latitude = in.readString();
		longitude = in.readString();
		address = in.readString();
	}
}
