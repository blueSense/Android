package com.bluesensenetworks.proximitysense.model;

import org.altbeacon.beacon.Beacon;

import android.os.Parcel;
import android.os.Parcelable;

public class Sighting implements Parcelable {
	private String uuid;
	private int major;
	private int minor;
	private int rssi;
	private String proximity;

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

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public String getProximity() {
		return proximity;
	}

	public void setProximity(String proximity) {
		this.proximity = proximity;
	}

	public Sighting() {

	}

	public Sighting(Parcel in) {
		readFromParcel(in);
	}

	public static Sighting createFromBeacon(Beacon beacon) {
		if (beacon == null)
			return null;

		Sighting sighting = new Sighting();
		sighting.uuid = beacon.getId1().toString();
		sighting.major = beacon.getId2().toInt();
		sighting.minor = beacon.getId3().toInt();
		sighting.rssi = beacon.getRssi();
		sighting.proximity = convertToProximity(beacon.getDistance());
		return sighting;
	}

	private static String convertToProximity(double distance) {
		if (distance < 0) {
			return "Unknown";
		}
		if (distance < 0.5) {
			return "Immediate";
		}
		if (distance <= 4.0) {
			return "Near";
		}
		return "Far";
	}

	public static final Parcelable.Creator<Sighting> CREATOR = new Parcelable.Creator<Sighting>() {
		public Sighting createFromParcel(Parcel in) {
			return new Sighting(in);
		}

		public Sighting[] newArray(int size) {
			return new Sighting[size];
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
		dest.writeInt(rssi);
		dest.writeString(proximity);
	}

	protected void readFromParcel(Parcel in) {
		uuid = in.readString();
		major = in.readInt();
		minor = in.readInt();
		rssi = in.readInt();
		proximity = in.readString();
	}
}
