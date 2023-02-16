package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities;

import net.runelite.api.ChatMessageType;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChatMessageHandler {
    @Inject
    private ChatMessageManager chatMessageManager;

    public void sendGameMessage(String message) {
        String chatMessage = buildChatMessage(message);
        QueuedMessage queuedMessage = buildQueuedMessage(chatMessage);
        chatMessageManager.queue(queuedMessage);
    }

    private String buildChatMessage(String message) {
        return new ChatMessageBuilder()
                .append(ChatColorType.HIGHLIGHT)
                .append(message)
                .build();
    }

    private QueuedMessage buildQueuedMessage(String chatMessage) {
        return QueuedMessage.builder()
                .type(ChatMessageType.CONSOLE)
                .runeLiteFormattedMessage(chatMessage)
                .build();
    }
}
