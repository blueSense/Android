package com.bluesensenetworks.bluebaribeacondemo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IBeaconConsumer {

	private static final int ENABLE_BT_REQUEST_ID = 1;

	private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);

	private Queue<IBeacon> filterBuffer = new LinkedList<IBeacon>();

	private ImageButton imageOffer;
	private int currentProximityState = IBeacon.PROXIMITY_UNKNOWN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iBeaconManager.bind(this);
		initializeControls();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		iBeaconManager.unBind(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (iBeaconManager.isBound(this))
			iBeaconManager.setBackgroundMode(this, true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		verifyBluetooth();
		if (iBeaconManager.isBound(this))
			iBeaconManager.setBackgroundMode(this, false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// user didn't want to turn on BT
		if (requestCode == ENABLE_BT_REQUEST_ID) {
			if (resultCode == Activity.RESULT_CANCELED) {
				Toast.makeText(this, "Bluetooth needs to be turned ON for us to work.", Toast.LENGTH_LONG).show();
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onIBeaconServiceConnect() {
		iBeaconManager.setRangeNotifier(new RangeNotifier() {
			@Override
			public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
				if (iBeacons.size() > 0) {
					IBeacon foundBeacon = iBeacons.iterator().next();

					filterBuffer.add(foundBeacon);
					if (filterBuffer.size() < 10) {
						currentProximityState = IBeacon.PROXIMITY_FAR;
						executeProximityRules();
						return;
					}
					filterBuffer.remove();

					int minImmediateProximitySightings = 0;
					for (IBeacon beacon : filterBuffer) {
						if (beacon.getProximity() == IBeacon.PROXIMITY_IMMEDIATE)
							minImmediateProximitySightings++;
					}

					currentProximityState = minImmediateProximitySightings > 5 ? IBeacon.PROXIMITY_IMMEDIATE : IBeacon.PROXIMITY_FAR;
					executeProximityRules();
				}
			}
		});

		String blueBarUuid = "A0B13730-3A9A-11E3-AA6E-0800200C9A66";
		try {
			iBeaconManager.startRangingBeaconsInRegion(new Region("BlueBar Beacon Region", blueBarUuid, null, null));
		} catch (RemoteException e) {
		}
	}

	private void executeProximityRules() {
		runOnUiThread(new Runnable() {
			public void run() {

				switch (currentProximityState) {
				case IBeacon.PROXIMITY_FAR:
					imageOffer.setImageResource(R.drawable.welcome);
					break;
				case IBeacon.PROXIMITY_IMMEDIATE:
					imageOffer.setImageResource(R.drawable.offer);
					break;
				default:
					break;
				}
			}
		});
	}

	private void verifyBluetooth() {
		try {
			if (!IBeaconManager.getInstanceForApplication(this).checkAvailability()) {
				// BT is not turned on - ask user to make it enabled
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST_ID);
			}
		} catch (RuntimeException e) {
			Toast.makeText(this, "BLE Hardware is required but not available.", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	private void initializeControls() {
		imageOffer = (ImageButton) MainActivity.this.findViewById(R.id.imageOffer);
		imageOffer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentProximityState == IBeacon.PROXIMITY_IMMEDIATE) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://BlueSenseNetworks.com"));
					startActivity(i);
				}
			}
		});
	}
}
