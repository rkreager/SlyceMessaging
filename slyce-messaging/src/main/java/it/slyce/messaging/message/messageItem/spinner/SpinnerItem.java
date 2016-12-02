package it.slyce.messaging.message.messageItem.spinner;

import android.app.Activity;

import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageItemType;
import it.slyce.messaging.message.messageItem.MessageViewHolder;

/**
 * Created by matthewpage on 7/5/16.
 */
public class SpinnerItem extends MessageItem {
    public SpinnerItem() {
        super(null);
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder, Activity activity) {

    }

    @Override
    public MessageItemType getMessageItemType() {
        return MessageItemType.SPINNER;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.EXTERNAL_USER;
    }
}
