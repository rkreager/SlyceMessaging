package it.slyce.messaging.message.messageItem.master.text;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.regex.Pattern;

import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.TextMessage;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageItemType;
import it.slyce.messaging.message.messageItem.MessageViewHolder;
import it.slyce.messaging.utils.DateUtils;
import it.slyce.messaging.utils.PatternEditableBuilder;

/**
 * Created by matthewpage on 6/27/16.
 */
public class MessageTextItem extends MessageItem {
    private Context context;

    public MessageTextItem(TextMessage textMessage, Context context) {
        super(textMessage);
        this.context = context;
    }

    @Override
    public void buildMessageItem(
            MessageViewHolder messageViewHolder, final Activity activity) {

        if (message != null &&  messageViewHolder != null && messageViewHolder instanceof MessageTextViewHolder) {
            final MessageTextViewHolder messageTextViewHolder = (MessageTextViewHolder) messageViewHolder;

            // Get content
            String date = DateUtils.getTimestamp(message.getDate());
            String text = ((TextMessage)message).getText();
            this.avatarUrl = message.getAvatarUrl();
            this.initials = message.getInitials();
            this.messageId = message.getMessageId();

            // Populate views with content
            messageTextViewHolder.initials.setText(initials  != null ? initials : "");
            messageTextViewHolder.text.setText(text != null ? text : "");
            messageTextViewHolder.timestamp.setText(date != null ? date : "");
            messageTextViewHolder.username.setText(message.getDisplayName() != null ? message.getDisplayName() : "");

            Pattern urlDetect = Pattern.compile("((([A-Za-z]{3,9}:(?:\\/\\/))(?:[\\-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9\\.\\-]+|(?:[a-zA-Z0-9]+\\.[a-zA-Z0-9]+|[\\-;:&=\\+\\$,\\w]+@)[A-Za-z0-9\\.\\-]+)((?:\\/[\\+~%\\/\\.\\w\\-_]*)?\\??(?:[\\-\\+=&;%@\\.\\w_]*)#?(?:[\\.\\!\\/\\\\\\w]*)[A-Za-z0-9\\/\\-]+)?)");

            new PatternEditableBuilder()
                    .addPattern(urlDetect, 0x7f25DBC9,
                            new PatternEditableBuilder.SpannableClickedListener() {
                                @Override
                                public void onSpanClicked(String text) {
                                    Uri linkUri = Uri.parse(text);
                                    String scheme = linkUri.getScheme();
                                    String fullPath = linkUri.getEncodedSchemeSpecificPart();
                                    if (scheme == null) {
                                        linkUri = Uri.parse("http://" + fullPath);
                                    }

                                    if (scheme != null && scheme.equals("league")) {
                                        String[] paths = fullPath.split("/");
                                        String tag = paths[paths.length - 1];
                                        Intent intent = new Intent(context, activity.getClass());
                                        intent.putExtra(SLYCE_PRESENTER, "link");
                                        intent.putExtra(SLYCE_TAG, tag);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    } else {
                                        Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                                        urlIntent.setData(linkUri);
                                        urlIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(urlIntent);
                                    }
                                }
                            }).into(messageTextViewHolder.text);

            if (message.getBubbleDrawableId() != null) {
                messageTextViewHolder.customSettings.localBubbleBackgroundColor = message.getBubbleDrawableId();
            }

            messageTextViewHolder.bubble.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple text", ((TextMessage)MessageTextItem.this.message).getText());
                    clipboard.setPrimaryClip(clip);
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(150);
                    Toast.makeText(context, "Text copied", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            messageViewHolder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (messageTextViewHolder.customSettings.userClicksAvatarPictureListener != null) {
                        messageTextViewHolder.customSettings.userClicksAvatarPictureListener.userClicksAvatarPhoto(message.getUserId(), messageId);
                    }
                }
            });

            if (isFirstConsecutiveMessageFromSource) {
                Glide.with(context).load(avatarUrl).into(messageTextViewHolder.avatar);
            }

            messageTextViewHolder.avatar.setVisibility(isFirstConsecutiveMessageFromSource && !TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.INVISIBLE);
            messageTextViewHolder.avatarContainer.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            messageTextViewHolder.initials.setVisibility(isFirstConsecutiveMessageFromSource && TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.GONE);
            messageTextViewHolder.username.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.GONE);
            messageTextViewHolder.timestamp.setVisibility(isLastConsecutiveMessageFromSource ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public MessageItemType getMessageItemType() {
        if (message.getSource() == MessageSource.EXTERNAL_USER) {
            return MessageItemType.INCOMING_TEXT;
        } else {
            return MessageItemType.OUTGOING_TEXT;
        }
    }

    @Override
    public MessageSource getMessageSource() {
        return message.getSource();
    }
}
