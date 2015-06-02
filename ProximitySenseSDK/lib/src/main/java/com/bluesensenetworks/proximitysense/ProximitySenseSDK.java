package com.bluesensenetworks.proximitysense;

import android.content.Context;

import com.bluesensenetworks.proximitysense.le.BleUtils;
import com.bluesensenetworks.proximitysense.model.ApiCredentials;
import com.bluesensenetworks.proximitysense.model.ApiOperations;
import com.bluesensenetworks.proximitysense.model.RangingManager;

public final class ProximitySenseSDK {
	public static final String PROXIMITYSENSESDK_VERSION = "1.0";
    private static Context context;

    public static ApiOperations getApi() {
		return ApiOperations.getInstanceForApplication(context);
	}

	public static RangingManager getRangingManager() {
		return RangingManager.getInstanceForApplication(context, ProximitySenseSDK.getApi());
	}

	public static BleUtils getBleUtils() {
		return BleUtils.getInstanceForApplication(context);
	}

    private static void initializeInternal(Context context){
        ProximitySenseSDK.context = context.getApplicationContext();
    }

	public static void initialize(Context context, ApiCredentials apiCredentials) {
        initializeInternal(context);
		getApi().setApiCredentials(apiCredentials);
	}

	public static void initialize(Context context, String applicationId, String privateKey) {
		initialize(context, new ApiCredentials(applicationId, privateKey));
	}
}
