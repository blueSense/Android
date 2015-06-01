package com.bluesensenetworks.proximitysense.integrations;

import com.bluesensenetworks.proximitysense.model.actions.ActionBase;

public class Integrations {
	public static void registerCommonActionTypes() {
		ActionBase.registerActionType("treasureHuntAchievement",
				"TreasureHuntAchievement");
		ActionBase.registerActionType("treasureHuntComplete",
				"TreasureHuntComplete");
	}
}
