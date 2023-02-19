package net.runelite.client.plugins._Viheiser.viprayerenabler;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.InvokeInteractions;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.WidgetMenuEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.ViUtilitiesPlugin;

import javax.inject.Inject;

@PluginDescriptor(
	name = "vi Prayer Enabler",
		enabledByDefault = false,
		description = "Prayer flicks prayer",
		tags = {"viheiser"}
)
@Slf4j
@PluginDependency(ViUtilitiesPlugin.class)
public class viPrayerEnablerPlugin extends Plugin
{
	@Inject
	private viPrayerEnablerConfig config;
	@Inject
	public Client client;
	@Inject
	private ConfigManager configManager;
	@Inject
	private InvokeInteractions invokeInteractions;
	@Inject
	private WidgetMenuEntries widgetMenuEntries;
	@Provides
	private viPrayerEnablerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(viPrayerEnablerConfig.class);
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
	private void onGameTick(GameTick event)
	{
		if (client.getLocalPlayer() == null
				|| client.getGameState() != GameState.LOGGED_IN
				|| client.isMenuOpen()
				|| client.getWidget(378,78) != null)//login button
			return;

		if(hasPrayerPoints() && config.enablePrayerFlick()) {
			invokeInteractions.invokeMenuAction(widgetMenuEntries.createTogglePrayer());
		}
	}

	private boolean hasPrayerPoints() {
		return client.getBoostedSkillLevel(Skill.PRAYER) > 0;
	}
}
