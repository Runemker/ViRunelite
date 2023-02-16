package net.runelite.client.plugins._Viheiser.ViFisher;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins._Viheiser.ViFisher.enums.Fish;
import net.runelite.client.plugins._Viheiser.viUtilities.api.extensions.objects.viPlayer;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities.InventoryUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.MenuEntryInteraction;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.WalkInteractions;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.InventoryEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.NpcMenuEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.WidgetMenuEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.calculations.CalculatorUtils;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private CalculatorUtils calc;

    @Provides
    private ViFisherConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ViFisherConfig.class);
    }

    private Fish fish;
    boolean dropping = false;
    int previousSize = 0;
    List<Widget> dropList;
    ListIterator<Widget> dropListIterator;

    HashMap<Integer, Integer> dropOrder;
    private ExecutorService executorService;
    private boolean run;

    @Override
    protected void startUp() throws Exception
    {
        fish = config.fishType();
        keyManager.registerKeyListener(hotkeyListener);
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    protected void shutDown() throws Exception
    {
        keyManager.unregisterKeyListener(hotkeyListener);
        executorService.shutdown();
    }

    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggle())
    {
        @Override
        public void hotkeyPressed()
        {
            run = !run;
        }
    };

    @Subscribe
    private void onGameTick(GameTick event)
    {
        if (!run) return;

        if(!dropping){

        }
        startLoop();
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


    private boolean levelUpMessageIsVisible() {
        Widget levelUpMessage = client.getWidget(WidgetInfo.LEVEL_UP);
        Widget dialogSprite = client.getWidget(WidgetInfo.DIALOG_SPRITE);
        return widgetIsVisible(levelUpMessage) || widgetIsVisible(dialogSprite);
    }

    private boolean widgetIsVisible(Widget widget) {
        return widget != null && !widget.isHidden();
    }

    private void handleDrop() {
        if (dropListIterator.hasNext()) {
            menuEntryInteraction.invokeMenuAction(inventoryEntries.createDropItemEntry(dropListIterator.next()));
            return;
        }
        if (!dropListIterator.hasNext()) {
            dropping = false;
        }
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

    private void startLoop(){
        executorService.submit(() ->
        {
            while (run)
            {
                if (validState())
                {
                    run = false;
                    break;
                }

                if (dropping) {
                    handleDrop();
                } else {
                    run = false;
                }

                try
                {
                    Thread.sleep(calc.randomDelay(config.weightedDistribution(), config.min(), config.max(), config.deviation(), config.target()));
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validState() {
        return client.getLocalPlayer() == null || client.getGameState() != GameState.LOGGED_IN;
    }

}
