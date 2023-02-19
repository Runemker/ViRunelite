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
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins._Viheiser.ViFisher.enums.Fish;
import net.runelite.client.plugins._Viheiser.viUtilities.api.events.ProjectileSpawned;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.ChatMessageHandler;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities.DialogUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities.InventoryUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities.NpcUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities.PlayerUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.InvokeInteractions;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.WalkInteractions;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.InventoryEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.NpcMenuEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.WidgetMenuEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.calculations.CalculatorUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.ViUtilitiesPlugin;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.util.*;

@PluginDependency(ViUtilitiesPlugin.class)
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
    private PlayerUtils playerUtils;
    @Inject
    private InvokeInteractions invokeInteractions;
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
    private ViUtilitiesPlugin viUtilities;
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
    @Override
    protected void startUp() throws Exception
    {
        fish = config.fishType();
        keyManager.registerKeyListener(hotkeyListener);
    }

    @Override
    protected void shutDown() throws Exception
    {
        keyManager.unregisterKeyListener(hotkeyListener);
    }

    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggle())
    {
        @Override
        public void hotkeyPressed()
        {
            run = !run;
            if(run)
                initEverything("Starting ViFisher");
            else
                initEverything("Stopping ViFisher");
        }
    };

    private void initEverything(String message){
        timeOut = 0;
        chatMessageHandler.sendGameMessage(message);
    }

    boolean hasTimedOut;

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

        if(inventoryUtils.isFull() && !hasTimedOut){
            timeOut = 1 + tickDelay();
            hasTimedOut = true;
            return;
        }

        if(inventoryUtils.isFull() && hasTimedOut){
            handleDrop();
            hasTimedOut = false;
            return;
        }

        if(playerIsFishing() && !dialogUtils.levelUpMessageIsVisible() || playerIsMoving()) {
            return;
        }

        if(!hasTimedOut){
            timeOut = 1 + tickDelay();
            hasTimedOut = true;
            return;
        }

        if(hasTimedOut){
            interactWithFishingSpot();
            hasTimedOut = false;
        }

    }

    private void interactWithFishingSpot() {
        NPC fishSpot = npcUtils.findNearestNpc(fish.getNpcId());
        npcUtils.interactWithNpc(fishSpot, fish.getNpcAction(), useMouseClicks(), sleepDelay());
    }

    private boolean playerIsMoving() {
        if (playerUtils.isMoving()) {
            return true;
        }

        return false;
    }

    @Subscribe
    private void onProjectileSpawned(ProjectileSpawned event){
        if(event.getProjectile() != null){
            chatMessageHandler.sendGameMessage("Saw projectile!!!");
        }
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
        inventoryUtils.dropItems(fish.getItemId(), true, sleepDelay());
//        if (dropListIterator.hasNext()) {
//            menuEntryInteraction.invokeMenuAction(inventoryEntries.createDropItemEntry(dropListIterator.next()));
//            return;
//        }
//        if (!dropListIterator.hasNext()) {
//            dropping = false;
//        }
    }

    private boolean useMouseClicks(){
        return !config.useInvokes();
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
        long ticks = calculatorUtils.randomDelay(config.tickDelaysWeightedDistribution(), config.tickDelaysMin(),config.tickDelaysMax(),config.tickDelaysDeviation(),config.tickDelaysTarget());
        chatMessageHandler.sendGameMessage("Timing out for: " + (ticks + 1) + " ticks");
        return (int) ticks;
    }
}
