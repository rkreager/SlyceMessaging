package it.slyce.messaging.message.messageItem;

import android.app.Activity;

import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.utils.DateUtils;

/**
 * Created by John C. Hunchar on 5/12/16.
 */
public abstract class MessageItem {

    protected boolean isFirstConsecutiveMessageFromSource;
    protected boolean isLastConsecutiveMessageFromSource;

    protected String avatarUrl;
    protected String initials;
    protected Message message;
    protected String date;
    protected String messageId;

    public static String SLYCE_TAG = "SLYCE_TAG";
    public static String SLYCE_PRESENTER = "SLYCE_PRESENTER";

    public MessageItem(Message message) {
        this.message = message;
    }

    public abstract void buildMessageItem(
            MessageViewHolder messageViewHolder, Activity activity);

    public abstract MessageItemType getMessageItemType();

    public abstract MessageSource getMessageSource();

    public Message getMessage() {
        return message;
    }

    public int getMessageItemTypeOrdinal() {
        return getMessageItemType().ordinal();
    }

    public boolean isFirstConsecutiveMessageFromSource() {
        return isFirstConsecutiveMessageFromSource;
    }

    public boolean isLastConsecutiveMessageFromSource() {
        return isLastConsecutiveMessageFromSource;
    }

    public void setIsFirstConsecutiveMessageFromSource(boolean isFirstConsecutiveMessageFromSource) {
        this.isFirstConsecutiveMessageFromSource = isFirstConsecutiveMessageFromSource;
    }

    public void setIsLastConsecutiveMessageFromSource(boolean isLastConsecutiveMessageFromSource) {
        this.isLastConsecutiveMessageFromSource = isLastConsecutiveMessageFromSource;
    }

    public String getDate() {
        return date;
    }

    public void updateDate(long time) {
        this.date = DateUtils.getTimestamp(time);
    }
}
