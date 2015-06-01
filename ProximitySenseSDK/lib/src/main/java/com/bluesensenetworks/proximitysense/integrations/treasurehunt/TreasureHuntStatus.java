package com.bluesensenetworks.proximitysense.integrations.treasurehunt;

import java.util.List;

public class TreasureHuntStatus {
	private String winMessage;

	private List<TreasureHuntLocation> locations;

	public String getWinMessage() {
		return winMessage;
	}

	public void setWinMessage(String winMessage) {
		this.winMessage = winMessage;
	}

	public List<TreasureHuntLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<TreasureHuntLocation> locations) {
		this.locations = locations;
	}
}
