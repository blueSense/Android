package com.bluesensenetworks.proximitysense.integrations.treasurehunt;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluesensenetworks.proximitysense.model.actions.ActionBase;

public class TreasureHuntComplete extends ActionBase {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TreasureHuntComplete() {
	}

	public TreasureHuntComplete(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<TreasureHuntComplete> CREATOR = new Parcelable.Creator<TreasureHuntComplete>() {
		public TreasureHuntComplete createFromParcel(Parcel in) {
			return new TreasureHuntComplete(in);
		}

		public TreasureHuntComplete[] newArray(int size) {
			return new TreasureHuntComplete[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(message);
	}

	@Override
	protected void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		message = in.readString();
	}
}
