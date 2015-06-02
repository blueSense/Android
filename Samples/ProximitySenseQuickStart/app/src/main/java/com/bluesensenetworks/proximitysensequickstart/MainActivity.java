package com.bluesensenetworks.proximitysensequickstart;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.bluesensenetworks.proximitysense.ProximitySenseSDK;
import com.bluesensenetworks.proximitysense.model.ApiCredentials;
import com.bluesensenetworks.proximitysense.model.ApiOperations;
import com.bluesensenetworks.proximitysense.model.RangingListener;
import com.bluesensenetworks.proximitysense.model.RangingManager;
import com.bluesensenetworks.proximitysense.model.actions.ActionBase;
import com.bluesensenetworks.proximitysense.model.actions.RichContentAction;


public class MainActivity extends ActionBarActivity  implements RangingListener {

    private static final int ENABLE_BT_REQUEST_ID = 1;

    // Overwrite with your ProximitySense Application Id and Private Key
    private static final String APPLICATION_ID = "YOUR APP ID";
    private static final String PRIVATE_KEY = "YOUR APP PRIVATE KEY";
    private static final String BEACONS_UUID = "A0B13730-3A9A-11E3-AA6E-0800200C9A66"; // Blue Sense Networks' factory beacon UUID

    ApiOperations api;
    RangingManager rangingManager;
    WebView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = (WebView) findViewById(R.id.action_webView);

        initProximitySenseSDK();
    }

    private void initProximitySenseSDK() {

        api = ProximitySenseSDK.getApi(this);
        api.setApiCredentials(new ApiCredentials(APPLICATION_ID, PRIVATE_KEY));

        rangingManager = ProximitySenseSDK.getRangingManager(this);
        rangingManager.setRangingListener(this);
        rangingManager.startForUuid(BEACONS_UUID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if BLE is available and Bluetooth is enabled
        verifyBluetooth();

        rangingManager.setBackgroundMode(false);
        rangingManager.startForUuid(BEACONS_UUID);
    }

    private void verifyBluetooth() {
        try {
            if (!ProximitySenseSDK.getBleUtils(this).checkBleAvailability()) {
                // BT is not turned on - ask user to make it enabled
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST_ID);
            }
        } catch (RuntimeException e) {
            Toast.makeText(this, "Sorry, Bluetooth LE is not available on this device.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // user didn't want to turn on BT
        if (requestCode == ENABLE_BT_REQUEST_ID) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Please enable Bluetooth to receive micro-location updates.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();

//		// RangingManager will make less frequent BLE scans when application is
//		// in background in order to preserve battery life
        rangingManager.setBackgroundMode(true);
    }

    @Override
    public void didReceiveAction(ActionBase action) {
        if (action instanceof RichContentAction) {
            RichContentAction richContentAction = (RichContentAction) action;
            content.getSettings().setBlockNetworkImage(false);
            content.loadUrl(richContentAction.getContentUrl());
        }
    }
}
