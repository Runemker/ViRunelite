package net.runelite.client.plugins._Viheiser.viUtilities;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@PluginDescriptor(
	name = "viUtilities",
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
	private ExecutorService executorService;

	@Getter
	@Setter
	private boolean iterating;

	@Provides
	private viUtilitiesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(viUtilitiesConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		executorService = Executors.newSingleThreadExecutor();
	}

	@Override
	protected void shutDown() throws Exception
	{
		executorService.shutdown();
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}
}
