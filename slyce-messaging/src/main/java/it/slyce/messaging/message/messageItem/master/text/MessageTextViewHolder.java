package it.slyce.messaging.message.messageItem.master.text;

import android.view.View;
import android.widget.FrameLayout;

import it.slyce.messaging.message.messageItem.MessageViewHolder;
import it.slyce.messaging.utils.CustomSettings;
import it.slyce.messaging.view.text.FontTextView;

/**
 * Created by matthewpage on 6/27/16.
 */
public abstract class MessageTextViewHolder extends MessageViewHolder {
    public FontTextView text;
    public FrameLayout bubble;

    public MessageTextViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);
    }
}