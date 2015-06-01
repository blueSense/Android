package com.bluesensenetworks.proximitysense.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Application implements Parcelable {
	private String name;
	@SerializedName("description")
	private String appDescription;
	private String clientId;
	private String privateKey;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public Application() {
	}

	public Application(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<Application> CREATOR = new Parcelable.Creator<Application>() {
		public Application createFromParcel(Parcel in) {
			return new Application(in);
		}

		public Application[] newArray(int size) {
			return new Application[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(appDescription);
		dest.writeString(clientId);
		dest.writeString(privateKey);
	}

	protected void readFromParcel(Parcel in) {
		name = in.readString();
		appDescription = in.readString();
		clientId = in.readString();
		privateKey = in.readString();
	}
}
