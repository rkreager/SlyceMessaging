package it.slyce.messaging.message;

import android.content.Context;

import it.slyce.messaging.message.messageItem.MessageItem;

/**
 * Created by matthewpage on 6/21/16.
 */
public abstract class Message {
    long date;
    MessageSource source;
    String avatarUrl;
    String displayName;
    String userId;
    String initials;
    String messageId;
    int bubbleDrawableId;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public MessageSource getSource() {
        return source;
    }

    public void setSource(MessageSource source) {
        this.source = source;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getBubbleDrawableId() {
        return bubbleDrawableId;
    }

    public void setBubbleDrawableId(int bubbleDrawableId) {
        this.bubbleDrawableId = bubbleDrawableId;
    }

    public abstract MessageItem toMessageItem(Context context);

    /**
     * Auto generated equals and hash, based on selected properties
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (date != message.date) return false;
        if (!displayName.equals(message.displayName)) return false;
        return userId.equals(message.userId);

    }

    @Override
    public int hashCode() {
        int result = (int) (date ^ (date >>> 32));
        result = 31 * result + displayName.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }
}
