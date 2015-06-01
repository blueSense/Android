package com.bluesensenetworks.proximitysense.model;

class UpdateBeaconRequestData {
	String uuid;
	int major;
	int minor;
	int advertisementInterval;
	int signalStrength;
	int powerCalibration;
	int batteryLevel;
	boolean initialSetupDone;
	boolean isEnabled;

	public UpdateBeaconRequestData(DetectedBeacon beacon) {
		BeaconConfiguration beaconConfig = beacon.getDeviceConfiguration();

		this.uuid = beaconConfig.getUuid();
		this.major = beaconConfig.getMajor();
		this.minor = beaconConfig.getMinor();
		this.advertisementInterval = beaconConfig.getAdvertisementInterval();
		this.signalStrength = beaconConfig.getSignalStrength();
		this.powerCalibration = beaconConfig.getCalibrationValue();
		this.batteryLevel = beaconConfig.getBatteryLevel();
		this.initialSetupDone = beacon.getInitialSetupDone();
		this.isEnabled = beaconConfig.getIsEnabled();
	}
}
