package com.bluesensenetworks.proximitysense.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ApiCredentials implements Parcelable {
	private String clientId;
	private String privateKey;

	public String getClientId() {
		return clientId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public ApiCredentials(String clientId, String privateKey) {
		this.clientId = clientId;
		this.privateKey = privateKey;
	}

	public ApiCredentials(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<ApiCredentials> CREATOR = new Parcelable.Creator<ApiCredentials>() {
		public ApiCredentials createFromParcel(Parcel in) {
			return new ApiCredentials(in);
		}

		public ApiCredentials[] newArray(int size) {
			return new ApiCredentials[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(clientId);
		dest.writeString(privateKey);
	}

	protected void readFromParcel(Parcel in) {
		clientId = in.readString();
		privateKey=in.readString();
	}
}
