package net.runelite.client.plugins.debugmenuentries;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@PluginDescriptor(
	name = "Debug Menu Entries",
		enabledByDefault = false,
		description = "See menu entry info in chat",
		tags = {"viheiser", "Debug", "menu", "entry", "entries"}
)
@Slf4j
public class DebugMenuEntriesPlugin extends Plugin
{
	@Inject
	private DebugMenuEntriesConfig config;
	@Inject
	public Client client;
	@Inject
	private ConfigManager configManager;
	@Inject
	private ChatMessageManager chatMessageManager;
	@Provides
	private DebugMenuEntriesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DebugMenuEntriesConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		if(config.sendToChat()) {
			sendGameMessage("---------MenuOptionClicked---------");
			sendGameMessage("getMenuOption: " + event.getMenuOption());
			sendGameMessage("getMenuTarget: " + event.getMenuTarget());
			sendGameMessage("getId: " + event.getId());
			sendGameMessage("getMenuAction: " + event.getMenuAction().getId());
			sendGameMessage("getParam0: " + event.getParam0());
			sendGameMessage("getParam1: " + event.getParam1());
			sendGameMessage("getItemId: " + event.getItemId());
			sendGameMessage("getItemOp: " + event.getItemOp());
			sendGameMessage("-----------------------------------");
		}

		System.out.println("getMenuOption: " + event.getMenuOption());
		System.out.println("getMenuTarget: " + event.getMenuTarget());
		System.out.println("getId: " + event.getId());
		System.out.println("getMenuAction: " + event.getMenuAction());
		System.out.println("getParam0: " + event.getParam0());
		System.out.println("getParam1: " + event.getParam1());
		System.out.println("getItemId: " + event.getItemId());
		System.out.println("getItemOp: " + event.getItemOp());
	}

	public void sendGameMessage(String message) {
		String chatMessage = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(message)
				.build();

		chatMessageManager
				.queue(QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(chatMessage)
						.build());
	}
}
