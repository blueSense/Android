package com.bluesensenetworks.proximitysense.le;

import android.content.Context;
import android.content.pm.PackageManager;

public class BleUtils {
	private static BleUtils instance;

	public static BleUtils getInstanceForApplication(Context context) {
		if (instance == null) {
			instance = new BleUtils(context);
		}

		return instance;
	}

	private Context context;

	public BleUtils(Context context) {
		this.context = context;
	}
	
	public boolean checkBleAvailability() throws BleNotAvailableException {
        if (android.os.Build.VERSION.SDK_INT < 18) {
            throw new BleNotAvailableException("Bluetooth LE is not supported by this device");
        }

		if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			throw new BleNotAvailableException("Bluetooth LE is not supported by this device");
		}		
		else {
			if (((android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled()){
				return true;
			}
		}	
		return false;
	}
}
