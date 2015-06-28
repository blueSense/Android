package com.bluesensenetworks.proximitysensequickstart;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.bluesensenetworks.proximitysense.ProximitySenseSDK;
import com.bluesensenetworks.proximitysense.model.ApiOperations;
import com.bluesensenetworks.proximitysense.model.AppUser;
import com.bluesensenetworks.proximitysense.model.RangingListener;
import com.bluesensenetworks.proximitysense.model.RangingManager;
import com.bluesensenetworks.proximitysense.model.actions.ActionBase;
import com.bluesensenetworks.proximitysense.model.actions.RichContentAction;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements RangingListener {

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

        ProximitySenseSDK.initialize(this, APPLICATION_ID, PRIVATE_KEY);
        api = ProximitySenseSDK.getApi();

        // AppUser represents the person that is currently using the app
        AppUser appUser = api.getAppUser();
        // Set the id of the current user. Should be set to whatever you use to uniquely identify your users - can be facebook id, email, guid etc.
        appUser.setAppSpecificId("My User Id");
        // Pass extra information about your user, such as name, email, photo url, date of birth etc.
        Map<String, String> userMetadata = new HashMap<String, String>();
        userMetadata.put("name", "John Snow");
        userMetadata.put("email", "captain@thewatch.org");
        userMetadata.put("company", "The Watch");
        userMetadata.put("position", "Captain");

        appUser.setUserMetadata(userMetadata);
        api.updateAppUser(); // Don't forget to persist the extra user data

        // Start listening for beacons
        rangingManager = ProximitySenseSDK.getRangingManager();
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

        if (APPLICATION_ID == "YOUR APP ID" || PRIVATE_KEY == "YOUR APP PRIVATE KEY")
        {
            ProximitySenseSDK.getRangingManager().stop();
            Toast.makeText(this, "APPLICATION_ID and PRIVATE_KEY are not set! Read how to set them correctly in MainActivity.java.", Toast.LENGTH_LONG).show();
        }
        // check if BLE is available and Bluetooth is enabled
        verifyBluetooth();

        rangingManager.setBackgroundMode(false);
        rangingManager.startForUuid(BEACONS_UUID);
    }

    private void verifyBluetooth() {
        try {
            if (!ProximitySenseSDK.getBleUtils().checkBleAvailability()) {
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

		// RangingManager will make less frequent BLE scans when application is
		// in background in order to preserve battery life
        rangingManager.setBackgroundMode(true);
    }

    @Override
    public void didReceiveAction(ActionBase action) {
        if (action instanceof RichContentAction) {
            RichContentAction richContentAction = (RichContentAction) action;
            content.getSettings().setBlockNetworkImage(false);
            content.loadUrl(richContentAction.getContentUrl());

            Map<String, String> metadata = richContentAction.getMetadata();
            Toast.makeText(this, "Metadata: " + metadata, Toast.LENGTH_LONG)
                    .show();

        }
    }
}
