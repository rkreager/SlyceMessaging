package it.slyce.messaging.listeners;

import it.slyce.messaging.message.Message;

/**
 * Created by matthewpage on 6/28/16.
 */
public interface UserClicksAvatarPictureListener {
    public void userClicksAvatarPhoto(String userId, Message message);
}
