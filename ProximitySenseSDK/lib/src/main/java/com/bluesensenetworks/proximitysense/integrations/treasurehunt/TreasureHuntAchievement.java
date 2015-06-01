package com.bluesensenetworks.proximitysense.integrations.treasurehunt;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluesensenetworks.proximitysense.model.actions.ActionBase;

public class TreasureHuntAchievement extends ActionBase {
	private TreasureHuntLocation location;

	public TreasureHuntLocation getLocation() {
		return location;
	}

	public void setLocation(TreasureHuntLocation location) {
		this.location = location;
	}

	public TreasureHuntAchievement() {
	}

	public TreasureHuntAchievement(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<TreasureHuntAchievement> CREATOR = new Parcelable.Creator<TreasureHuntAchievement>() {
		public TreasureHuntAchievement createFromParcel(Parcel in) {
			return new TreasureHuntAchievement(in);
		}

		public TreasureHuntAchievement[] newArray(int size) {
			return new TreasureHuntAchievement[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		location.writeToParcel(dest, flags);
	}

	@Override
	protected void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		location = new TreasureHuntLocation(in);
	}
}
