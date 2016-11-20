package it.slyce.messaging.message.messageItem;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.slyce.messaging.R;
import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.messageItem.externalUser.media.MessageExternalUserMediaViewHolder;
import it.slyce.messaging.message.messageItem.externalUser.text.MessageExternalUserTextViewHolder;
import it.slyce.messaging.message.messageItem.internalUser.media.MessageInternalUserViewHolder;
import it.slyce.messaging.message.messageItem.internalUser.text.MessageInternalUserTextViewHolder;
import it.slyce.messaging.message.messageItem.spinner.SpinnerViewHolder;
import it.slyce.messaging.utils.CustomSettings;

/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private static final String TAG = MessageRecyclerAdapter.class.getName();

    private MessageViewHolder viewHolder;

    private List<MessageItem> mMessageItems;

    private CustomSettings customSettings;

    private List<Integer> itemPositions = new ArrayList<>();

    private View.OnClickListener listener;

    private Context context;

    public MessageRecyclerAdapter(Context context, List<MessageItem> messageItems, CustomSettings customSettings) {
        mMessageItems = messageItems;
        this.context = context;
        this.customSettings = customSettings;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        viewHolder = null;

        MessageItemType messageItemType = MessageItemType.values()[viewType];
        switch (messageItemType) {


            case INCOMING_MEDIA:
                View scoutMediaView = inflater.inflate(R.layout.item_message_external_media, parent, false);
                viewHolder = new MessageExternalUserMediaViewHolder(scoutMediaView, customSettings);
                break;

            case INCOMING_TEXT:
                View scoutTextView = inflater.inflate(R.layout.item_message_external_text, parent, false);
                viewHolder = new MessageExternalUserTextViewHolder(scoutTextView, customSettings);
                break;

            case OUTGOING_MEDIA:
                View userMediaView = inflater.inflate(R.layout.item_message_user_media, parent, false);
                viewHolder = new MessageInternalUserViewHolder(userMediaView, customSettings);
                break;

            case OUTGOING_TEXT:
                View userTextView = inflater.inflate(R.layout.item_message_user_text, parent, false);
                viewHolder = new MessageInternalUserTextViewHolder(userTextView, customSettings);
                break;

            case SPINNER:
                View spinnerView = inflater.inflate(R.layout.item_spinner, parent, false);
                viewHolder = new SpinnerViewHolder(spinnerView, customSettings);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder messageViewHolder, int position) {
        if (messageViewHolder == null) {
            return;
        }

        FrameLayout externalFrameLayout = (FrameLayout) messageViewHolder.itemView.findViewById(R.id.message_scout_text_view_group_bubble);
        if (externalFrameLayout != null) {
            externalFrameLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_rounded_rectangle_white));
        }

        // Build the item
        MessageItem messageItem = getMessageItemByPosition(position);
        if (messageItem != null) {
            messageItem.buildMessageItem(messageViewHolder);
        }

        FrameLayout localFrameLayout = (FrameLayout) messageViewHolder.itemView.findViewById(R.id.message_user_text_view_group_bubble);
        if (localFrameLayout != null) {
            localFrameLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_rounded_rectangle_gray));
            for (int itemPosition : itemPositions) {
                if (position == itemPosition) {
                    localFrameLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_rounded_rectangle_error));
                    localFrameLayout.setOnClickListener(listener);
                    messageViewHolder.avatar.setOnClickListener(listener);
                    messageViewHolder.itemView.setOnClickListener(listener);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mMessageItems != null ? mMessageItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {

        // Get the item type
        Integer itemType = getMessageItemType(position);
        if (itemType != null) {
            return itemType;
        }

        return super.getItemViewType(position);
    }

    private MessageItem getMessageItemByPosition(int position) {
        if (mMessageItems != null && !mMessageItems.isEmpty()) {
            if (position >= 0 && position < mMessageItems.size()) {
                MessageItem messageItem = mMessageItems.get(position);
                if (messageItem != null) {
                    return messageItem;
                }
            }
        }

        return null;
    }

    private Integer getMessageItemType(int position) {
        MessageItem messageItem = getMessageItemByPosition(position);
        if (messageItem != null) {
            return messageItem.getMessageItemTypeOrdinal();
        }

        return null;
    }

    public void updateMessageItemDataList(List<MessageItem> messageItems) {
        mMessageItems = messageItems;
        notifyDataSetChanged();
    }

    public void sortListByDateAndNotify() {
        Collections.sort(mMessageItems, new Comparator<MessageItem>() {
            @Override
            public int compare(MessageItem o1, MessageItem o2) {
                long aDate = o1.getMessage().getDate();
                long bDate = o2.getMessage().getDate();
                if (aDate < bDate) {
                    return -1;
                } else if (aDate <= bDate) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        notifyDataSetChanged();
    }

    public void removeMessage(Message message) {
        for (int i=0; i < mMessageItems.size(); i++) {
            if (message.equals(mMessageItems.get(i).message)) {
                mMessageItems.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mMessageItems.size());
            }
        }
    }

    public void setMessageListener(Message message, int itemPosition, String avatarUrl, final View.OnClickListener listener) {
        message.setAvatarUrl(avatarUrl);
        itemPositions.add(itemPosition);
        this.listener = listener;
    }

}
