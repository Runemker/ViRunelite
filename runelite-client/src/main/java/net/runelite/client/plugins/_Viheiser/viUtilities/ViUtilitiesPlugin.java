package net.runelite.client.plugins._Viheiser.viUtilities;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins._Viheiser.viUtilities.api.extensions.MenuEntryExtension;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.ActionQueue;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.InvokeInteractions;
import net.runelite.client.plugins._Viheiser.viUtilities.events.ProjectileSpawnedHandler;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@PluginDescriptor(
	name = "ViUtilities",
	description = "Utils needed for Viheisers plugins",
	tags = {"viheiser", "utils"}
)
@Slf4j
public class ViUtilitiesPlugin extends Plugin
{
	@Inject
	private ViUtilitiesConfig config;
	@Inject
	public Client client;
	@Inject
	private ConfigManager configManager;
	private ExecutorService executorService;
	@Inject
	private EventBus eventBus;
	@Inject
	private ProjectileSpawnedHandler projectileSpawnedHandler;
	@Inject
	private ActionQueue actionQueue;
	@Inject
	private MenuEntryExtension menuEntryExtension;
	@Inject
	private InvokeInteractions invokeInteractions;
	@Getter
	@Setter
	private boolean iterating;

	@Provides
	private ViUtilitiesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ViUtilitiesConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		executorService = Executors.newSingleThreadExecutor();
		eventBus.register(projectileSpawnedHandler);
		eventBus.register(actionQueue);
	}

	@Override
	protected void shutDown() throws Exception
	{
		executorService.shutdown();
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public MenuEntryExtension getMenuEntryExtension() {return menuEntryExtension;}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event) {
		if (isWorldSwitcherWidget(event)) {
			clearMenuEntry();
		} else if (menuEntryExtension.hasMenuEntry()) {
			invokeMenuAction();
			menuEntryExtension.clearMenuEntry();
			event.consume();
		}
	}

	private boolean isWorldSwitcherWidget(MenuOptionClicked event) {
		// Either logging out or world-hopping which is handled by 3rd party plugins so let them have priority
		int widgetId = event.getWidgetId();
		return event.getMenuAction() == MenuAction.CC_OP &&
				(widgetId == WidgetInfo.WORLD_SWITCHER_LIST.getId() ||
						widgetId == 11927560 ||
						widgetId == 4522007 ||
						widgetId == 24772686);
	}

	private void clearMenuEntry() {
		menuEntryExtension.setMenuEntry(null);
	}

	private void invokeMenuAction() {
		invokeInteractions.invokeMenuAction(menuEntryExtension.buildMenuEntry());
	}

}
