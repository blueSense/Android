package com.bluesensenetworks.proximitysense.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import com.bluesensenetworks.proximitysense.model.actions.ActionBase;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

public class RangingManager {
	private static final String TAG = RangingManager.class.getSimpleName();

	private static RangingManager instance;

	public static RangingManager getInstanceForApplication(Context context, ApiOperations blueBarApi) {
		if (instance == null) {
			instance = new RangingManager(context, blueBarApi);
		}

		return instance;
	}

	private Context context;
	private ApiOperations blueBarApi;
	private RangingListener rangingListener;
	private BeaconManager beaconManager;
	private Region rangingRegion;

	private Handler timer;
	private Runnable pollTask;
    private RangingManagerStatus status = RangingManagerStatus.Stopped;

	private BeaconConsumer beaconConsumer = new BeaconConsumer() {
		@Override
		public void onBeaconServiceConnect() {

			beaconManager.setRangeNotifier(new RangeNotifier() {
				@Override
				public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//					Log.i(TAG, "Ranged " + beacons.size() + " beacons in region " + region.getId1());
					if (beacons.size() > 0)
						blueBarApi.reportBeaconSightings(new ArrayList<Beacon>(beacons));
				}
			});

			startRanging();
		}

		@Override
		public boolean bindService(Intent intent, ServiceConnection conn, int arg2) {
			return context.getApplicationContext().bindService(intent, conn, arg2);
		}

		@Override
		public Context getApplicationContext() {
			return context.getApplicationContext();
		}

		@Override
		public void unbindService(ServiceConnection conn) {
			context.getApplicationContext().unbindService(conn);
		}
	};

	private ApiOperationListener<List<ActionBase>> actionsListener = new ApiOperationListener<List<ActionBase>>() {
		@Override
		public void success(List<ActionBase> actions) {
			for (ActionBase action : actions) {
				if (rangingListener != null)
					rangingListener.didReceiveAction(action);
			}
		}

		@Override
		public void failure(int statusCode, String reason) {
            if (statusCode == 401) // Unauthorized
            {
                stop();
            }
		}
	};

	private void startRanging() {
        if (status != RangingManagerStatus.Starting)
            return;

		if (rangingRegion != null) {
			Log.i(TAG, "Starting ranging for region " + rangingRegion.getId1());
			try {
				beaconManager.startRangingBeaconsInRegion(rangingRegion);
			} catch (RemoteException e) {
				Log.e(TAG, "Error starting ranging", e);
			}
		}

        status = RangingManagerStatus.Started;
    }

	public RangingListener getRangingListener() {
		return this.rangingListener;
	}

	public void setRangingListener(RangingListener rangingListener) {
		this.rangingListener = rangingListener;
	}

	public RangingManager(Context context, ApiOperations blueBarApi) {
		this.context = context;
		this.blueBarApi = blueBarApi;

		// configure AndroidBeaconLibrary to detect iBeacons
		beaconManager = BeaconManager.getInstanceForApplication(context.getApplicationContext());
		beaconManager.getBeaconParsers().add(
                new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
	}

	public void startForUuid(String uuid) {

        status = RangingManagerStatus.Starting;

		rangingRegion = new Region("BlueBar Beacon Region", Identifier.parse(uuid), null, null);

		if (beaconManager.isBound(beaconConsumer)) {
			startRanging();
		} else {
			Log.i(TAG, "Binding BeaconManager");
			// ranging will be started when beacon service is connected
			beaconManager.bind(beaconConsumer);
		}

		// poll platform for available actions
		// TODO: adjust polling frequency when in background mode!
		timer = new Handler();
		pollTask = new Runnable() {
			@Override
			public void run() {
				blueBarApi.requestAvailableActionResults(actionsListener);
				timer.postDelayed(this, 1000);
			}
		};
		timer.postDelayed(pollTask, 1000);
	}

	public void stop() {
        status = RangingManagerStatus.Stopping;

        try {
            if (beaconManager.isBound(beaconConsumer)) {
                beaconManager.stopRangingBeaconsInRegion(rangingRegion);
            }
		} catch (RemoteException e) {
			Log.e(TAG, "Error stopping ranging", e);
		}
		rangingRegion = null;

		if (timer != null) {
			timer.removeCallbacks(pollTask);
			timer = null;
		}

        status = RangingManagerStatus.Stopped;
	}

	public void setBackgroundMode(boolean backgroundMode) {
		beaconManager.setBackgroundMode(backgroundMode);
	}
}
