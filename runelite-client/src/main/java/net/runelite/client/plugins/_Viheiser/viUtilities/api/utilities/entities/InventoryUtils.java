package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities;

import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins._Viheiser.viUtilities.api.objects.DelayWrapper;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.calculations.CalculatorUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.MenuEntryInteraction;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.InventoryEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.viUtilitiesPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

import static net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.Sleeping.sleep;

@Singleton
public class InventoryUtils {
    @Inject
    private Client client;
    @Inject
    private BankUtils bank;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ItemManager itemManager;
    @Inject
    private MenuEntryInteraction menuEntryInteraction;
    @Inject
    private InventoryEntries inventoryEntries;
    @Inject
    private CalculatorUtils calculatorUtils;
    @Inject
    private viUtilitiesPlugin plugin;

    public void interactWithItemInvoke(int itemID, String option) {
        interactWithItemInvoke(new int[]{itemID}, option);
    }

    public void interactWithItemInvoke(int[] itemID, String option) {
        Widget itemWidget = getWidget(itemID);
        if (itemWidget != null) {
            int id = itemOptionToId(itemWidget.getId(), option);
            MenuEntry entry = inventoryEntries.createInteractWithItem(itemWidget, id, idToMenuAction(id));
            if (entry != null) {
                menuEntryInteraction.invokeMenuAction(entry);
            }
        }
    }

    private MenuAction idToMenuAction(int id)
    {
        if (id <= 5)
            return MenuAction.CC_OP;
        else
            return MenuAction.CC_OP_LOW_PRIORITY;
    }

    private int itemOptionToId(int itemId, String match)
    {
        return itemOptionToId(itemId, List.of(match));
    }

    private int itemOptionToId(int itemId, List<String> match) {
        ItemComposition itemDefinition = getItemDefinition(itemId);
        String[] inventoryActions = itemDefinition.getInventoryActions();

        for (int i = 0; i < inventoryActions.length; i++) {
            String action = inventoryActions[i];
            if (action != null && match.stream().anyMatch(action::equalsIgnoreCase)) {
                return (i <= 2) ? i + 2 : i + 3;
            }
        }

        return -1;
    }


    private final Map<Integer, ItemComposition> itemCompositionMap = new HashMap<>();
    public ItemComposition getItemDefinition(int id)
    {
        if (itemCompositionMap.containsKey(id))
        {
            return itemCompositionMap.get(id);
        }
        else
        {
            ItemComposition def = client.getItemDefinition(id);
            itemCompositionMap.put(id, def);

            return def;
        }
    }

    public List<Widget> getInventoryItems(Collection<Integer> ids) {
        rebuildInventoryWidget();
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

        if (inventoryWidget != null && inventoryWidget.getChildren() != null) {
            return getMatchingItems(inventoryWidget, ids);
        }
        return null;
    }

    public Collection<Widget> getAllInventoryItems() {
        rebuildInventoryWidget();
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);

        if (inventoryWidget != null && inventoryWidget.getChildren() != null) {
            return getItems(inventoryWidget);
        }

        return null;
    }

    public Widget getWidget(int id) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            Collection<Widget> items = getInventoryItems(List.of(id));
            for (Widget item : items) {
                if (item.getId() == id) {
                    return item;
                }
            }
        }

        return null;
    }

    public Widget getWidget(int[] ids) {
        List<Integer> idList = Arrays.asList(Arrays.stream(ids).boxed().toArray(Integer[]::new));
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            Collection<Widget> items = getInventoryItems(idList);
            for (Widget item : items) {
                if (idList.contains(item.getId())) {
                    return item;
                }
            }
        }

        return null;
    }

    public Widget getWidget(Collection<Integer> ids) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            Collection<Widget> items = getInventoryItems(ids);
            for (Widget item : items) {
                if (ids.contains(item.getId())) {
                    return item;
                }
            }
        }

        return null;
    }

    public void dropAllExcept(Collection<Integer> ids, boolean weightedDistribution, int minDelay, int maxDelay, int deviation, int targetDelay) {
        if (bank.isOpen() || bank.isDepositBoxOpen()) {
            return;
        }
        Collection<Widget> inventoryItems = getAllInventoryItems();
        plugin.getExecutorService().submit(() ->
        {
            try {
                plugin.setIterating(true);
                for (Widget item : inventoryItems) {
                    if (ids.contains(item.getId())) {
                        continue;
                    }
                    menuEntryInteraction.invokeMenuAction(inventoryEntries.createDropItemEntry(item));
                    sleep(calculatorUtils.randomDelay(weightedDistribution, minDelay, maxDelay, deviation, targetDelay));
                }
                plugin.setIterating(false);
            } catch (Exception e) {
                plugin.setIterating(false);
                e.printStackTrace();
            }
        });
    }

    public void dropItem(Widget item) {
        assert !client.isClientThread();

        menuEntryInteraction.invokeMenuAction(inventoryEntries.createDropItemEntry(item));
    }

    public void dropInventory(DelayWrapper delayWrapper) {
        if (bank.isOpen() || bank.isDepositBoxOpen()) {
            return;
        }
        Collection<Integer> inventoryItems = getAllInventoryItems().stream().map(item -> item.getItemId()).collect(Collectors.toList());
        dropItems(inventoryItems, true, delayWrapper);
    }

    public void dropItems(Collection<Integer> ids, boolean dropAll, DelayWrapper delayWrapper) {
        if (bank.isOpen() || bank.isDepositBoxOpen()) {
            return;
        }
        Collection<Widget> inventoryItems = getInventoryItems(ids);
        plugin.getExecutorService().submit(() ->
        {
            try {
                plugin.setIterating(true);

                for (Widget item : inventoryItems) {
                    menuEntryInteraction.invokeMenuAction(inventoryEntries.createDropItemEntry(item));
                    sleep(calculatorUtils.randomDelay(delayWrapper.isWeightedDistribution(), delayWrapper.getMinDelay(), delayWrapper.getMaxDelay(), delayWrapper.getDeviation(), delayWrapper.getTarget()));
                    if (!dropAll) {
                        break;
                    }
                }
                plugin.setIterating(false);
            } catch (Exception e) {
                plugin.setIterating(false);
                e.printStackTrace();
            }
        });
    }



    public List<Item> getAllItemsExcept(List<Integer> exceptIDs) {
        int emptySlot = -1;
        exceptIDs.add(emptySlot);
        ItemContainer inventoryContainer = client.getItemContainer(InventoryID.INVENTORY);
        if (inventoryContainer != null) {
            Item[] items = inventoryContainer.getItems();
            List<Item> itemList = new ArrayList<>(Arrays.asList(items));
            itemList.removeIf(item -> exceptIDs.contains(item.getId()));
            return itemList.isEmpty() ? null : itemList;
        }
        return null;
    }

    private List<Widget> getItems(Widget inventoryWidget) {
        List<Widget> items = new ArrayList<>();
        for (int i = 0; i < getInventorySize(inventoryWidget); i++) {
            Widget item = inventoryWidget.getChild(i);
            if (item != null) {
                items.add(item);
            }
        }

        return items;
    }

    private List<Widget> getMatchingItems(Widget inventoryWidget, Collection<Integer> ids) {
        List<Widget> matchedItems = new ArrayList<>();
        for (int i = 0; i < getInventorySize(inventoryWidget); i++) {
            Widget item = inventoryWidget.getChild(i);
            if (item != null && ids.contains(item.getItemId())) {
                matchedItems.add(item);
            }
        }
        return matchedItems;
    }

    private boolean inventoryIsHidden() {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        return !widgetIsVisible(inventoryWidget);
    }

    private boolean widgetIsVisible(Widget widget) {
        return widget != null && !widget.isHidden();
    }


    public boolean isFull() {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            int emptyWidgetId = 6512;
            Widget[] items = inventoryWidget.getChildren();
            if (Arrays.stream(items).filter(i -> i.getItemId() != emptyWidgetId).count() >= 28)
                return true;
        }

        return false;
    }


    private int getInventorySize(Widget inventoryWidget) {
        return Math.min(inventoryWidget.getChildren().length, 28);
    }

    private void rebuildInventoryWidget() {
        clientThread.invoke(() -> client.runScript(6009, 9764864, 28, 1, -1));
    }

    public int getEmptySlots() {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            return 28 - getUsedItemSlots();
        } else {
            return -1;
        }
    }

    public int getUsedItemSlots() {
        Widget inventory = client.getWidget(WidgetInfo.INVENTORY.getId());
        return inventory == null ? 28 : (int) Arrays.stream(inventory.getDynamicChildren()).filter(w -> w.getItemId() != 6512).count();
    }
}
