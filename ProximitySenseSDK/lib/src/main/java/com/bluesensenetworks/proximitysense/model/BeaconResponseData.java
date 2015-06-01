package com.bluesensenetworks.proximitysense.model;

public class BeaconResponseData {
	private boolean initialSetupDone;
	private boolean isEnabled;
	private boolean isInSync;
	private String name;
	private String pin;
	private String uuid;
	private int major;
	private int minor;
	private int advertisementInterval;
	private int signalStrength;
	private int powerCalibration;
	private int batteryLevel;
	private boolean shouldRefreshBattery;
	private boolean notOwnedByUser;

	public boolean getInitialSetupDone() {
		return initialSetupDone;
	}

	public void setInitialSetupDone(boolean initialSetupDone) {
		this.initialSetupDone = initialSetupDone;
	}

	public boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public boolean getIsInSync() {
		return isInSync;
	}

	public void setInSync(boolean isInSync) {
		this.isInSync = isInSync;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

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

	public int getAdvertisementInterval() {
		return advertisementInterval;
	}

	public void setAdvertisementInterval(int advertisementInterval) {
		this.advertisementInterval = advertisementInterval;
	}

	public int getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}

	public int getPowerCalibration() {
		return powerCalibration;
	}

	public void setPowerCalibration(int powerCalibration) {
		this.powerCalibration = powerCalibration;
	}

	public int getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
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

	public DetectedBeacon writeToBeacon(DetectedBeacon beacon) {
		if (beacon == null)
			beacon = new DetectedBeacon();

		beacon.setInitialSetupDone(this.getInitialSetupDone());
		beacon.setIsInSync(this.getIsInSync());
		beacon.setFriendlyName(this.getName());
		beacon.setPin(this.getPin());
		beacon.setShouldRefreshBattery(this.getShouldRefreshBattery());
		beacon.setNotOwnedByUser(this.getNotOwnedByUser());

		BeaconConfiguration platformConfiguration = new BeaconConfiguration();
		platformConfiguration.setUuid(this.getUuid());
		platformConfiguration.setMajor(this.getMajor());
		platformConfiguration.setMinor(this.getMinor());
		platformConfiguration.setSignalStrength(this.getSignalStrength());
		platformConfiguration.setCalibrationValue(this.getPowerCalibration());
		platformConfiguration.setAdvertisementInterval(this.getAdvertisementInterval());
		platformConfiguration.setBatteryLevel(this.getBatteryLevel());
		platformConfiguration.setIsEnabled(this.getIsEnabled());

		beacon.setPlatformConfiguration(platformConfiguration);

		return beacon;
	}
}
