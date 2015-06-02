package com.bluesensenetworks.proximitysense;

import android.content.Context;

import com.bluesensenetworks.proximitysense.le.BleUtils;
import com.bluesensenetworks.proximitysense.model.ApiOperations;
import com.bluesensenetworks.proximitysense.model.RangingManager;

public class ProximitySenseSDK {
	public static final String PROXIMITYSENSESDK_VERSION = "1.0";

	public static ApiOperations getApi(Context context) {
		return ApiOperations.getInstanceForApplication(context);
	}

	public static RangingManager getRangingManager(Context context) {
		return RangingManager.getInstanceForApplication(context, ProximitySenseSDK.getApi(context));
	}

	public static BleUtils getBleUtils(Context context) {
		return BleUtils.getInstanceForApplication(context);
	}
}
