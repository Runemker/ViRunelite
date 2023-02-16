package net.runelite.client.plugins._Viheiser.ViFisher;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins._Viheiser.ViFisher.enums.Fish;
import net.runelite.client.plugins._Viheiser.viUtilities.api.extensions.objects.viPlayer;
import net.runelite.client.plugins._Viheiser.viUtilities.api.objects.DelayWrapper;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.ChatMessageHandler;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities.DialogUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities.InventoryUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities.NpcUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.MenuEntryInteraction;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.WalkInteractions;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.InventoryEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.NpcMenuEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.WidgetMenuEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.calculations.CalculatorUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.viUtilitiesPlugin;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.util.*;

import static net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.Sleeping.sleep;

@PluginDependency(viUtilitiesPlugin.class)
@PluginDescriptor(
        name = "ViFisher",
        description = "power fishes",
        tags = {"viheiser", "fish"}
)
@Slf4j
public class ViFisherPlugin extends Plugin
{
    @Inject
    private ViFisherConfig config;
    @Inject
    public Client client;
    @Inject
    private WidgetMenuEntries widgetEntries;
    @Inject
    private ConfigManager configManager;
    @Inject
    private ClientThread clientThread;
    @Inject
    private viPlayer viPlayer;
    @Inject
    private MenuEntryInteraction menuEntryInteraction;
    @Inject
    private WalkInteractions walkInteractions;
    @Inject
    private NpcMenuEntries npcMenuEntries;
    @Inject
    private WidgetMenuEntries widgetMenuEntries;
    @Inject
    private InventoryEntries inventoryEntries;
    @Inject
    private InventoryUtils inventoryUtils;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    private KeyManager keyManager;
    @Inject
    private CalculatorUtils calculatorUtils;
    @Inject
    private NpcUtils npcUtils;
    @Inject
    private DialogUtils dialogUtils;
    @Inject
    private viUtilitiesPlugin viUtilities;
    @Inject
    private ChatMessageHandler chatMessageHandler;

    @Provides
    private ViFisherConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ViFisherConfig.class);
    }

    private Fish fish;
    boolean dropping = false;
    int previousSize = 0;
    int timeOut = 0;
    List<Widget> dropList;
    ListIterator<Widget> dropListIterator;

    HashMap<Integer, Integer> dropOrder;
    private boolean run;
    private DelayWrapper delayWrapper;
    @Override
    protected void startUp() throws Exception
    {
        fish = config.fishType();
        keyManager.registerKeyListener(hotkeyListener);
        delayWrapper = new DelayWrapper(config.weightedDistribution(), config.minDelay(), config.maxDelay(), config.deviation(), config.target());
    }

    @Override
    protected void shutDown() throws Exception
    {
        keyManager.unregisterKeyListener(hotkeyListener);
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event){
        updateDelayWrapper();
    }

    private void updateDelayWrapper(){
        if(delayWrapper == null){
            delayWrapper = new DelayWrapper(config.weightedDistribution(), config.minDelay(), config.maxDelay(), config.deviation(), config.target());
        } else {
            delayWrapper.update(config.weightedDistribution(), config.minDelay(), config.maxDelay(), config.deviation(), config.target());
        }
    }

    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggle())
    {
        @Override
        public void hotkeyPressed()
        {
            run = !run;
            if(run)
                initEverything();
            else
                chatMessageHandler.sendGameMessage("Stopping ViFisher");
        }
    };

    private void initEverything(){
        timeOut = 0;
        chatMessageHandler.sendGameMessage("Starting ViFisher");
    }

    @Subscribe
    private void onGameTick(GameTick event)
    {
        if (!run || isInvalidGameState() || viUtilities.isIterating()) {
            return;
        }

        if(timeOut > 0){
            timeOut--;
            return;
        }

        if(inventoryUtils.isFull()){
            handleDrop();
        }

        if(playerIsFishing() && !dialogUtils.levelUpMessageIsVisible() || playerIsMoving()) {
            return;
        }

        interactWithFishingSpot();
        timeOut = tickDelay();
    }

    private void interactWithFishingSpot() {
        sleep(sleepDelay());
        NPC fishSpot = npcUtils.findNearestNpc(fish.getNpcId());
        npcUtils.invokeMenuOption(fishSpot, "Lure");
    }

    private boolean playerIsMoving() {
        if (viPlayer.isMoving()) {
            return true;
        }

        return false;
    }

    private boolean playerIsFishing() {
        Player player = client.getLocalPlayer();
        if (player.getInteracting() == null)
            return false;

        if (fish.getNpcName().contains(player.getInteracting().getName())) {
            return true;
        }

        return false;
    }

    private void handleDrop() {
        inventoryUtils.dropItems(fish.getItemId(), true, delayWrapper);
//        if (dropListIterator.hasNext()) {
//            menuEntryInteraction.invokeMenuAction(inventoryEntries.createDropItemEntry(dropListIterator.next()));
//            return;
//        }
//        if (!dropListIterator.hasNext()) {
//            dropping = false;
//        }
    }

    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.INVENTORY.getId()) {
            return;
        }
        createDropList();
    }

    @Subscribe
    private void onWidgetLoaded(WidgetLoaded event) {
        if (event.getGroupId() != WidgetInfo.LEVEL_UP.getGroupId())
            return;
    }

    //has to be called on client thread
    private void createDropList()
    {
        int size = inventoryUtils.getUsedItemSlots();
        if (size == 28)
        {
            updateDropList();
        }

        previousSize = size;
    }

    private int getItemIndex(int i) {
        return config.customDrop() ? dropOrder.get(i) : i;
    }

    private void updateDropList() {
        dropList = inventoryUtils.getInventoryItems(fish.getItemId());

        if (dropList == null || dropList.isEmpty()) {
            dropping = false;
            return;
        }

        dropListIterator = dropList.listIterator();
        dropping = true;
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

    private long sleepDelay(){
        return calculatorUtils.randomDelay(config.weightedDistribution(), config.minDelay(),config.maxDelay(),config.deviation(),config.target());
    }

    private int tickDelay(){
        return (int) calculatorUtils.randomDelay(config.tickDelaysWeightedDistribution(), config.tickDelaysMin(),config.tickDelaysMax(),config.tickDelaysDeviation(),config.tickDelaysTarget());
    }
}
