package com.bluesensenetworks.proximitysense.model.actions;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class RichContentAction extends ActionBase {
	private boolean sendNotification;
	private String notificationText;
	private boolean sendContent;
	private String content;
	private String contentUrl;
	private Bundle metadata;

	public boolean isSendNotification() {
		return sendNotification;
	}

	public void setSendNotification(boolean sendNotification) {
		this.sendNotification = sendNotification;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}

	public boolean isSendContent() {
		return sendContent;
	}

	public void setSendContent(boolean sendContent) {
		this.sendContent = sendContent;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public Bundle getMetadata() {
		return metadata;
	}

	public void setMetadata(Bundle metadata) {
		this.metadata = metadata;
	}

	public RichContentAction() {
	}

	public RichContentAction(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<RichContentAction> CREATOR = new Parcelable.Creator<RichContentAction>() {
		public RichContentAction createFromParcel(Parcel in) {
			return new RichContentAction(in);
		}

		public RichContentAction[] newArray(int size) {
			return new RichContentAction[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeByte((byte) (sendNotification ? 1 : 0));
		dest.writeString(notificationText);
		dest.writeByte((byte) (sendContent ? 1 : 0));
		dest.writeString(content);
		dest.writeString(contentUrl);
		dest.writeBundle(metadata);
	}

	@Override
	protected void readFromParcel(Parcel in) {
		super.readFromParcel(in);
		sendNotification = in.readByte() != 0;
		notificationText = in.readString();
		sendContent = in.readByte() != 0;
		content = in.readString();
		contentUrl = in.readString();
		metadata = in.readBundle();
	}
}
