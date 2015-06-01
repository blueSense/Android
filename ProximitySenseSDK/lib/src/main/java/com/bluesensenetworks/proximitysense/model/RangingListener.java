package com.bluesensenetworks.proximitysense.model;

import com.bluesensenetworks.proximitysense.model.actions.ActionBase;

public interface RangingListener {
	void didReceiveAction(ActionBase action);
}
