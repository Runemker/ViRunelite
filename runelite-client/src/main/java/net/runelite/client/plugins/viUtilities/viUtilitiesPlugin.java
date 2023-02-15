package net.runelite.client.plugins.viUtilities;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@PluginDescriptor(
	name = "viUtils",
	description = "Utils needed for Viheisers plugins",
	tags = {"viheiser", "utils"}
)
@Slf4j
public class viUtilitiesPlugin extends Plugin
{
	@Inject
	private viUtilitiesConfig config;
	@Inject
	public Client client;
	@Inject
	private ConfigManager configManager;
	@Provides
	private viUtilitiesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(viUtilitiesConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}
}
