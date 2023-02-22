package net.runelite.client.plugins._Viheiser.vibankstander;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins._Viheiser.ViUtilities.ViUtilitiesPlugin;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.ChatMessageHandler;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.calculations.CalculatorUtils;
import net.runelite.client.plugins._Viheiser.vibankstander.enums.ToolType;
import net.runelite.client.plugins._Viheiser.vibankstander.enums.ViBankstanderState;
import net.runelite.client.plugins._Viheiser.vibankstander.handlers.StateHandler;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;

@PluginDependency(ViUtilitiesPlugin.class)
@PluginDescriptor(
        name = "ViBankstander",
        description = "A plugin that can handle a bunch of different skills.",
        tags = {"viheiser", "bankstander", "bankstand"}
)
@Slf4j
public class ViBankstanderPlugin extends Plugin {
    @Inject
    private ViBankstanderConfig config;
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
    ViBankstanderOverlay overlay;
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
    private ViBankstanderState state;
    @Getter
    Instant botTimer;
    int timeout;
    int itemsMade;
    @Provides
    private ViBankstanderConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ViBankstanderConfig.class);
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
            chatMessageHandler.sendGameMessage("Starting ViBankstander");
            botTimer = Instant.now();
            overlayManager.add(overlay);
        }
        else {
            chatMessageHandler.sendGameMessage("Stopping ViBankstander");
            botTimer = null;
            overlayManager.remove(overlay);
        }
        timeout = 0;
        itemsMade = 0;
    }

    @Override
    protected void startUp() throws Exception
    {
        keyManager.registerKeyListener(hotkeyListener);
        eventBus.register(StateHandler.class);
    }
    @Override
    protected void shutDown() throws Exception
    {
        keyManager.unregisterKeyListener(hotkeyListener);
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

    public List<Integer> getItemIds(){
        try{
            return viUtilities.stringToIntList(config.itemIds());
        }
        catch (Exception ex){
            chatMessageHandler.sendGameMessage(
                    "Couldn't convert your item ids to a List. Make sure each id is separated by a comma. Like this: 2524,214,4326"
            );
        }

        return null;
    }

    public int getToolId(){
        if(config.toolType().equals(ToolType.CUSTOM))
            return config.toolId();

        return config.toolType().getItemId();
    }

    public boolean useMouse(){
        return !config.useInvokes();
    }
}
