package com.bluesensenetworks.proximitysense.model;

import com.bluesensenetworks.proximitysense.Utils;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class DetectedBeacon implements Parcelable {
	private static final String BLUEBAR_BEACON_NAME = "BlueBar Beacon";

	private String deviceName;
	private String deviceAddress;
	private String serialNumber;
	private int rssi;

	private BeaconConfiguration deviceConfiguration = new BeaconConfiguration();

	private boolean initialSetupDone;
	private boolean isInSync;
	private String friendlyName;
	private String pin;
	private boolean shouldRefreshBattery;
	private boolean notOwnedByUser;

	private BeaconConfiguration platformConfiguration = new BeaconConfiguration();

	private byte[] otaUpdateData;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String name) {
		this.deviceName = name;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String address) {
		this.deviceAddress = address;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public BeaconConfiguration getPlatformConfiguration() {
		return platformConfiguration;
	}

	public void setPlatformConfiguration(BeaconConfiguration configuration) {
		this.platformConfiguration = configuration;
	}

	public BeaconConfiguration getDeviceConfiguration() {
		return deviceConfiguration;
	}

	public void setDeviceConfiguration(BeaconConfiguration configuration) {
		this.deviceConfiguration = configuration;
	}

	public boolean getInitialSetupDone() {
		return initialSetupDone;
	}

	public void setInitialSetupDone(boolean initialSetupDone) {
		this.initialSetupDone = initialSetupDone;
	}

	public boolean getIsInSync() {
		return isInSync;
	}

	public void setIsInSync(boolean isInSync) {
		this.isInSync = isInSync;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public byte[] getOtaUpdateData() {
		return otaUpdateData;
	}

	public void setOtaUpdateData(byte[] otaUpdateData) {
		this.otaUpdateData = otaUpdateData;
	}

	public byte[] getFactoryPin() {
		if (this.serialNumber == null)
			return null;

		byte[] byteSerial = Utils.hexToBytes(this.serialNumber);
		if (byteSerial.length < 4)
			return null;

		byte[] pin = new byte[4];
		System.arraycopy(byteSerial, 2, pin, 0, 4);

		return pin;
	}

	public boolean getShouldRefreshBattery() {
		return shouldRefreshBattery;
	}

	public void setShouldRefreshBattery(boolean shouldRefreshBattery) {
		this.shouldRefreshBattery = shouldRefreshBattery;
	}

	public boolean getNotOwnedByUser() {
		return notOwnedByUser;
	}

	public void setNotOwnedByUser(boolean notOwnedByUser) {
		this.notOwnedByUser = notOwnedByUser;
	}

	public DetectedBeacon() {
	}

	private DetectedBeacon(Parcel in) {
		readFromParcel(in);
	}

	public boolean equals(Object o) {

		if (this == o) {

			return true;
		}

		if (!(o instanceof DetectedBeacon)) {

			return false;
		}

		DetectedBeacon c = (DetectedBeacon) o;

		return deviceAddress.equals(c.deviceAddress);
	}

	public static DetectedBeacon createFromScanData(BluetoothDevice device, int rssi, byte[] scanRecord) {
		if (device.getName() != null && device.getName().contains(BLUEBAR_BEACON_NAME)) {
			DetectedBeacon beacon = new DetectedBeacon();
			beacon.setDeviceName(device.getName());
			beacon.setDeviceAddress(device.getAddress());
			beacon.setSerialNumber(device.getName().substring(BLUEBAR_BEACON_NAME.length() + 1));
			beacon.setRssi(rssi);

			return beacon;
		}

		return null;
	}

	public static final Parcelable.Creator<DetectedBeacon> CREATOR = new Parcelable.Creator<DetectedBeacon>() {
		public DetectedBeacon createFromParcel(Parcel in) {
			return new DetectedBeacon(in);
		}

		public DetectedBeacon[] newArray(int size) {
			return new DetectedBeacon[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(deviceName);
		dest.writeString(deviceAddress);
		dest.writeString(serialNumber);
		dest.writeInt(rssi);
		deviceConfiguration.writeToParcel(dest, flags);
		platformConfiguration.writeToParcel(dest, flags);
		dest.writeByte((byte) (initialSetupDone ? 1 : 0));
		dest.writeByte((byte) (isInSync ? 1 : 0));
		dest.writeString(friendlyName);
		dest.writeString(pin);
		dest.writeByte((byte) (shouldRefreshBattery ? 1 : 0));
		dest.writeByte((byte) (notOwnedByUser ? 1 : 0));
	}

	public void readFromParcel(Parcel in) {
		deviceName = in.readString();
		deviceAddress = in.readString();
		serialNumber = in.readString();
		rssi = in.readInt();
		deviceConfiguration = new BeaconConfiguration(in);
		platformConfiguration = new BeaconConfiguration(in);
		initialSetupDone = in.readByte() != 0;
		isInSync = in.readByte() != 0;
		friendlyName = in.readString();
		pin = in.readString();
		shouldRefreshBattery = in.readByte() != 0;
		notOwnedByUser = in.readByte() != 0;
	}
}
