# Android

SDK binary release, source code and code samples to integrate [ProximitySense](http://proximitysense.com) technology into Android.  
Copyright 2014 [Blue Sense Networks](http://bluesensenetworks.com)




## ProximitySenseSDK

The ProximitySenseSDK folder keeps the source code for the ProximitySenseSDK, which you need in order to get your apps ProximitySense enabled.
The most recent binary release is in the **release** folder, built and packaged as an AAR.

### Dependencies
  
ProximitySense SDK has the following dependencies:

- Google GSon library - https://github.com/google/gson
- Volley library - https://developer.android.com/training/volley/index.html
- Radius Networks' Android AltBeacon Library - https://github.com/AltBeacon/android-beacon-library

### Usage

Please look at the Samples folder for an example implementation. The project you should look for is "ProximitySenseQuickStart". 
It demonstrates how easy it is to integrate the ProximitySense SDK into an application of your own - the code is in MainActivity.java class.

Setting up your project:

1. add the ProximitySenseSDK-1.0.aar file to the folder where you hold your dependencies;
2. add the Volley library JAR file to the same folder - more info from here: https://developer.android.com/training/volley/index.html

3. add the following lines in your build.gradle file, in the dependencies node, like this:
``` java
    compile 'org.bluesensenetworks:ProximitySenseSDK:1+@aar'
    compile 'com.google.code.gson:gson:2.2.4'
    compile 'org.altbeacon:android-beacon-library:2+@aar'
```
Note: ProximitySense SDK has the following dependencies: 
Google GSon, Volley library and Radius Networks' Android AltBeacon Library

4. Don't forget to add the following 2 lines into your AndroidManifest.xml file, to allow access to the Bluetooth hardware on the device:
``` xml
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.INTERNET" />
```


# Samples

## ProximitySense Quick Start

A simple app to demonstrate integrating the ProximitySense in your own apps. Look at MainActivity.java for integration code.

### Building

1. Create an application in [ProximitySense](http://proximitysense.com)

2. Open MainActivity.java and replace the **APPLICATION_ID** and **PRIVATE_KEY** constants with the corresponding id and key from the application you just created.

The app displays the content of only one RichContentAction at a time, in order to make it as simple as possible to illustrate SDK integration.  


## BlueBar iBeacon Demo app

A simple app to display an offer banner when coming in Immediate proximity of a BlueBar iBeacon.
Features:
- Demonstrates simple use of iBeacons ranging functionality for Android based on Radius Networks' Android iBeacon Service Library (http://developer.radiusnetworks.com/ibeacon/android/);
- Does simple filtering on sightings to limit the effect of “bad” values

Known limitations:
- Takes into account only the closest beacon it detects