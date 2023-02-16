package net.runelite.client.plugins._Viheiser.viOneClickFishing;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins._Viheiser.viUtilities.api.interactions.MenuEntryInteraction;
import net.runelite.client.plugins._Viheiser.viOneClickFishing.bank.BankBase;
import net.runelite.client.plugins._Viheiser.viOneClickFishing.bank.ShiloVillage;
import net.runelite.client.plugins._Viheiser.viOneClickFishing.enums.Actions;
import net.runelite.client.plugins._Viheiser.viOneClickFishing.enums.Fish;
import net.runelite.client.plugins._Viheiser.viOneClickFishing.enums.Method;
import net.runelite.client.plugins._Viheiser.viUtilities.api.extensions.objects.viPlayer;
import net.runelite.client.plugins._Viheiser.viUtilities.api.interactions.WalkInteractions;
import net.runelite.client.plugins._Viheiser.viUtilities.api.menuEntries.InventoryEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.api.menuEntries.NpcMenuEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.api.menuEntries.WidgetMenuEntries;

import javax.inject.Inject;
import java.util.*;

@PluginDescriptor(
        name = "viOneClickFishing",
        description = "viOneClickFishing",
        tags = {"viheiser", "one click"}
)
@Slf4j
public class viOneClickFishingPlugin  extends Plugin
{
    @Inject
    private viOneClickFishingConfig config;
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
    private ChatMessageManager chatMessageManager;
    Random random = new Random();

    private final String oneClickText = "1Click Fishing";
    @Provides
    private viOneClickFishingConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(viOneClickFishingConfig.class);
    }

    private Fish fish;

    private Method method;

    private BankBase bankSpot = null;
    boolean dropping = false;
    int previousSize = 0;
    List<Widget> dropList;
    ListIterator<Widget> dropListIterator;

    HashMap<Integer, Integer> dropOrder;


    @Override
    protected void startUp() throws Exception
    {
        fish = config.fishType();
        method = config.methodType();
        clientThread.invokeLater(() -> createDropList());

        updateBankSpot();
    }

    @Override
    protected void shutDown() throws Exception
    {
    }

    @Subscribe
    private void onMenuOptionClicked(MenuOptionClicked event) {
        if (!event.getMenuOption().equals(oneClickText))
            return;

        if (config.stopMisclick() && !dropping && shouldConsume()) {
            event.consume();
        }

        handleClick();
        event.consume();
    }

    private boolean shouldConsume() {
        if (levelUpMessageIsVisible()) {
            return true;
        }

        if (playerIsFishing()) {
            return true;
        }

        if (playerIsMoving()) {
            return true;
        }

        return false;
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

    private void updateBankSpot() {
        if (!config.methodType().equals(Method.BANK))
            return;

        switch (config.bankSpot()) {
            case SHILO_VILLAGE:
                bankSpot = new ShiloVillage(client);
                break;
        }
    }

    private void handleClick() {
        if (dropping && method.equals(Method.DROP)) {
            handleDrop();
        } else {
            handleFishing();
        }
    }

    private void handleFishing() {
        if (shouldOpenInventory()) {
            menuEntryInteraction.invokeMenuAction(widgetMenuEntries.openInventory());
            return;
        }

        if (bankIsOpen() && inventoryContainsFish()) {
            menuEntryInteraction.invokeMenuAction(widgetMenuEntries.createDepositItemInBank(getInventoryItem(fish.getItemId())));
            return;
        }
        else {
            if (inventoryIsFull()) {
                if (bankSpot.canSeeBank()) {
                    menuEntryInteraction.invokeMenuAction(bankSpot.openBank());
                    return;
                } else {
                    walkNearBank();
                }
                return;
            }

            if(method.equals(Method.BANK)) {
                if (bankSpot.canSeeFish()) {
                    menuEntryInteraction.invokeMenuAction(npcMenuEntries.createFishMenuEntry(fish.getNpcId()));
                } else {
                    if (bankSpot.getAreaNearFish() != null) {
                        walkNearFish();
                        return;
                    }
                }
            }

            if(method.equals(Method.DROP)){
                menuEntryInteraction.invokeMenuAction(npcMenuEntries.createFishMenuEntry(fish.getNpcId()));
                return;
            }

        }
    }

    private boolean inventoryContainsFish() {
        Widget[] inventory = client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER).getChildren();
        if (inventory != null) {
            for (Widget item : inventory) {
                if (fish.getItemId().contains(item.getItemId()))
                    return true;
            }
        }

        return false;
    }

    private int getIndexOfAction(String[] actions, Actions withdrawOptions) {
        int index = 1;
        for (String action : actions) {
            if (action != null) {
                if (action.toLowerCase().contains(withdrawOptions.getText().toLowerCase())) {
                    return index;
                }
            }

            index++;
        }

        return -1;
    }

    private Widget getFirstBankInventoryItem(Set<Integer> itemIds) {
        Widget[] inventory = client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER).getChildren();
        if (inventory != null) {
            for (Widget item : inventory) {
                if (itemIds.contains(item.getItemId()))
                    return item;
            }
        }

        return null;
    }

    private Widget getBankInventoryItem(int itemId) {
        Widget[] inventory = client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER).getChildren();
        if (inventory != null) {
            for (Widget item : inventory) {
                if (item.getItemId() == itemId)
                    return item;
            }
        }

        return null;
    }

    private void walkNearFish() {
        List<WorldPoint> worldPointList = bankSpot.getAreaNearFish().toWorldPointList();
        WorldPoint randomTile = worldPointList.get(random.nextInt(worldPointList.size()));
        walkInteractions.walkTo(randomTile);
    }

    private void walkNearBank() {
        List<WorldPoint> worldPointList = bankSpot.getAreaNearBank().toWorldPointList();
        WorldPoint randomTile = worldPointList.get(random.nextInt(worldPointList.size()));
        walkInteractions.walkTo(randomTile);
    }

    private boolean inventoryIsFull() {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            int emptyWidgetId = 6512;
            Widget[] items = inventoryWidget.getChildren();
            if (Arrays.stream(items).filter(i -> i.getItemId() != emptyWidgetId).count() >= 28)
                return true;
        }

        return false;
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
        int size = nonemptyInventorySlots();
        if (size == 28)
        {
            updateDropList();
        }

        previousSize = size;
    }

    private int nonemptyInventorySlots()
    {
        Widget inventory = client.getWidget(WidgetInfo.INVENTORY.getId());
        if (inventory == null)
        {
            return 28;
        }

        return (int) Arrays.stream(inventory.getDynamicChildren()).filter(w -> w.getItemId() != 6512).count();
    }

    private void updateDropList()
    {
        dropList = getItems(fish.getItemId());
        if( dropList == null || dropList.size() == 0)
        {
            dropping = false;
            return;
        }
        dropListIterator = dropList.listIterator();
        dropping = true;
    }

    public List<Widget> getItems(Collection<Integer> ids)
    {
        clientThread.invoke(() -> client.runScript(6009, 9764864, 28, 1, -1));
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        List<Widget> matchedItems = new ArrayList<>();

        if (inventoryWidget != null && inventoryWidget.getChildren() != null)
        {
            for(int i = 0; i <= 27; i++)
            {
                int index = config.customDrop() ? dropOrder.get(i) : i;
                Widget item = inventoryWidget.getChild(index);
                if (item != null && ids.contains(item.getItemId()))
                {
                    matchedItems.add(item);
                }
            }
            return matchedItems;
        }
        return null;
    }

    private boolean bankIsOpen() {
        Widget bankWidget = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        return widgetIsVisible(bankWidget);
    }

    private Widget getInventoryItem(Set<Integer> ids) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        Widget bankInventoryWidget = client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
        if (widgetIsVisible(inventoryWidget)) {
            return getWidgetItem(inventoryWidget, ids);
        }
        if (widgetIsVisible(bankInventoryWidget)) {
            return getWidgetItem(bankInventoryWidget, ids);
        }
        return null;
    }

    private Widget getWidgetItem(Widget widget, Set<Integer> ids) {
        for (Widget item : widget.getDynamicChildren()) {
            if (ids.contains(item.getItemId())) {
                return item;
            }
        }
        return null;
    }

    private boolean shouldOpenInventory() {
        return inventoryIsHidden() && !bankIsOpen();
    }

    private boolean inventoryIsHidden() {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        return !widgetIsVisible(inventoryWidget);
    }

    @Subscribe
    private void onClientTick(ClientTick event) {
        if (client.getLocalPlayer() == null || client.getGameState() != GameState.LOGGED_IN || client.isMenuOpen())
            return;

        client.createMenuEntry(-1).setOption(oneClickText).setTarget("").setIdentifier(0).setType(MenuAction.CC_OP)
                .setParam0(0).setParam1(0).setForceLeftClick(true);
    }
}
