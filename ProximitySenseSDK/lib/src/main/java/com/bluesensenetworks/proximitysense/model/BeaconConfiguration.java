package com.bluesensenetworks.proximitysense.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BeaconConfiguration implements Parcelable {

	private String uuid = "";
	private int major;
	private int minor;
	private int signalStrength;
	private int calibrationValue;
	private int advertisementInterval;
	private int batteryLevel;
	private boolean isEnabled;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public int getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}

	public int getCalibrationValue() {
		return calibrationValue;
	}

	public void setCalibrationValue(int calibrationValue) {
		this.calibrationValue = calibrationValue;
	}

	public int getAdvertisementInterval() {
		return advertisementInterval;
	}

	public void setAdvertisementInterval(int advertisementInterval) {
		this.advertisementInterval = advertisementInterval;
	}

	public int getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public BeaconConfiguration() {

	}

	public BeaconConfiguration(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<BeaconConfiguration> CREATOR = new Parcelable.Creator<BeaconConfiguration>() {
		public BeaconConfiguration createFromParcel(Parcel in) {
			return new BeaconConfiguration(in);
		}

		public BeaconConfiguration[] newArray(int size) {
			return new BeaconConfiguration[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(uuid);
		dest.writeInt(major);
		dest.writeInt(minor);
		dest.writeInt(signalStrength);
		dest.writeInt(calibrationValue);
		dest.writeInt(advertisementInterval);
		dest.writeInt(batteryLevel);
		dest.writeByte((byte) (isEnabled ? 1 : 0));
	}

	public void readFromParcel(Parcel in) {
		uuid = in.readString();
		major = in.readInt();
		minor = in.readInt();
		signalStrength = in.readInt();
		calibrationValue = in.readInt();
		advertisementInterval = in.readInt();
		batteryLevel = in.readInt();
		isEnabled = in.readByte() != 0;
	}
}
