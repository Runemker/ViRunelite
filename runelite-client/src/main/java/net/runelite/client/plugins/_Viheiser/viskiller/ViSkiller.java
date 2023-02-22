package net.runelite.client.plugins._Viheiser.viskiller;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins._Viheiser.viskiller.enums.DropBehaviour;
import net.runelite.client.plugins._Viheiser.viskiller.enums.ViSkillerState;
import net.runelite.client.plugins._Viheiser.ViUtilities.ViUtilitiesPlugin;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.ChatMessageHandler;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.calculations.CalculatorUtils;
import net.runelite.client.plugins._Viheiser.viskiller.handlers.Banking;
import net.runelite.client.plugins._Viheiser.viskiller.handlers.Dropping;
import net.runelite.client.plugins._Viheiser.viskiller.handlers.Interacting;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

@PluginDependency(ViUtilitiesPlugin.class)
@PluginDescriptor(
        name = "ViSkiller",
        description = "A plugin that can handle a bunch of different skills.",
        tags = {"viheiser", "skiller"}
)
@Slf4j
public class ViSkiller extends Plugin {
    @Inject
    private ViSkillerConfig config;
    @Inject
    public Client client;
    @Inject
    private ConfigManager configManager;
    @Inject
    private KeyManager keyManager;
    @Inject
    private ChatMessageHandler chatMessageHandler;
    @Inject
    OverlayManager overlayManager;
    @Inject
    ViSkillerOverlay overlay;
    @Inject
    CalculatorUtils calculatorUtils;
    @Inject
    ViUtilitiesPlugin ViUtilities;
    @Inject
    private EventBus eventBus;
    @Inject
    private ViUtilitiesPlugin viUtilities;
    @Getter
    private boolean startThieving;
    @Getter
    @Setter
    private ViSkillerState state;
    @Getter
    Instant botTimer;
    int timeout;
    int loot;
    @Provides
    private ViSkillerConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ViSkillerConfig.class);
    }

    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggle())
    {
        @Override
        public void hotkeyPressed()
        {
            startThieving = !startThieving;
            toggled();
        }
    };

    private void toggled() {
        if(startThieving) {
            chatMessageHandler.sendGameMessage("Starting ViSkiller");
            botTimer = Instant.now();
            overlayManager.add(overlay);
        }
        else {
            chatMessageHandler.sendGameMessage("Stopping ViSkiller");
            botTimer = null;
            overlayManager.remove(overlay);
        }
        timeout = 0;
        loot = 0;
    }

    @Override
    protected void startUp() throws Exception
    {
        keyManager.registerKeyListener(hotkeyListener);
        eventBus.register(Dropping.class);
        eventBus.register(Banking.class);
        eventBus.register(Interacting.class);
    }
    @Override
    protected void shutDown() throws Exception
    {
        keyManager.unregisterKeyListener(hotkeyListener);
    }

    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged event){
        if(event.getContainerId() != WidgetInfo.INVENTORY.getId()) return;

        if(config.dropBehaviour().equals(DropBehaviour.AT_FULL_INVENTORY)){

        }
    }

    public long sleepDelay(){
        return calculatorUtils.randomDelay(
                config.sleepWeightedDistribution(),
                config.sleepMinDelay(),
                config.sleepMaxDelay(),
                config.sleepDeviation(),
                config.sleepTarget()
        );
    }

    public int tickDelay(){
        long ticks = calculatorUtils.randomDelay(
                config.tickDelaysWeightedDistribution(),
                config.tickDelaysMin(),
                config.tickDelaysMax(),
                config.tickDelaysDeviation(),
                config.tickDelaysTarget()
        );
        return (int) ticks;
    }

    public boolean useMouse(){
        return !config.useInvokes();
    }

    public List<Integer> getItemIds(){
        try{
            return viUtilities.stringToIntList(config.itemIdsToDrop());
        }
        catch (Exception ex){
            chatMessageHandler.sendGameMessage(
                    "Couldn't convert your item ids to a List. Make sure each id is separated by a comma. Like this: 2524,214,4326"
            );
        }

        return null;
    }

    public List<Integer> getInteractionIds(){
        try{
            return viUtilities.stringToIntList(config.interactionIds());
        }
        catch (Exception ex){
            chatMessageHandler.sendGameMessage(
                    "Couldn't convert your item ids to a List. Make sure each id is separated by a comma. Like this: 2524,214,4326"
            );
        }

        return null;
    }
}
