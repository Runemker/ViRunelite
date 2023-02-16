package net.runelite.client.plugins._Viheiser.oneclickrunenable;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins._Viheiser.viUtilities.api.interactions.MenuEntryInteraction;
import net.runelite.client.plugins._Viheiser.viUtilities.api.menuEntries.WidgetMenuEntries;

import javax.inject.Inject;

@PluginDescriptor(
	name = "One Click Run Enable",
		enabledByDefault = false,
		description = "Click anywhere to reenable run",
		tags = {"sundar", "pajeet", "viheiser"}
)
@Slf4j
public class OneClickRunEnablePlugin extends Plugin
{
	@Inject
	private OneClickRunEnableConfig config;
	@Inject
	public Client client;
	@Inject
	public MenuEntryInteraction menuEntryInteraction;
	@Inject
	public WidgetMenuEntries widgetMenuEntries;
	@Inject
	private ConfigManager configManager;
	private final String oneClickEnableRun = "One Click Enable Run";
	@Provides
	private OneClickRunEnableConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OneClickRunEnableConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	boolean clicked;
	@Subscribe
	private void onClientTick(ClientTick event)
	{
		if (isInvalidGameState())
			return;

		if (shouldEnableRun())
		{
			swapToRunMenuItem();
		}
	}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!event.getMenuOption().equals(oneClickEnableRun)) {
			return;
		}

		toggleRun();
		event.consume();
	}

	@Subscribe
	private void onGameTick(GameTick event) {
		if (isInvalidGameState())
			return;

		clicked = false;
	}

	private boolean isInvalidGameState() {
		return client.getLocalPlayer() == null
				|| client.getGameState() != GameState.LOGGED_IN
				|| client.isMenuOpen()
				|| isLoginButtonPresent();
	}

	private boolean isLoginButtonPresent() {
		int LOGIN_BUTTON_ID = 78;
		return client.getWidget(WidgetID.LOGIN_CLICK_TO_PLAY_GROUP_ID, LOGIN_BUTTON_ID) != null;
	}

	private void swapToRunMenuItem() {
		menuEntryInteraction.insertMenuItem(
				createSwappedMenuEntry()
		);
	}

	private boolean shouldEnableRun() {
		return client.getVarpValue(173) == 0
				&& client.getEnergy() >= Math.min(config.minimumRun(), 100)
				&& !clicked;
	}

	private void toggleRun(){
		menuEntryInteraction.invokeMenuAction(widgetMenuEntries.createToggleRunEntry());
	}

	private MenuEntry createSwappedMenuEntry(){
		MenuEntry menuEntry = client.createMenuEntry(0).setOption(oneClickEnableRun).setOption("")
				.setType(MenuAction.CC_OP).setParam0(0).setIdentifier(0).setParam1(0).setForceLeftClick(false);
		return menuEntry;
	}


}
