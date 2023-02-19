package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities;

import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.calculations.CalculatorUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.ActionQueue;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.MenuEntryInteractions;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.MouseInteractions;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.InventoryEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.viUtilitiesPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

import static net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.ActionQueue.sleep;

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
    private MenuEntryInteractions menuEntryInteractions;
    @Inject
    private InventoryEntries inventoryEntries;
    @Inject
    private CalculatorUtils calculatorUtils;
    @Inject
    private viUtilitiesPlugin plugin;
    @Inject
    private MouseInteractions mouseInteractions;
    @Inject
    private ActionQueue actionQueue;

    public void interactWithItemInvoke(int itemID, String option, long delay) {
        interactWithItemInvoke(new int[]{itemID}, option, delay);
    }

    public void interactWithItem(int itemID, long delay, String... option) {
        interactWithItem(itemID, false, delay, option);
    }

    public void interactWithItem(int itemID, boolean forceLeftClick, long delay, String... option) {
        interactWithItem(new int[]{itemID}, forceLeftClick, delay, option);
    }

    public void interactWithItem(int[] itemID, long delay, String... option) {
        interactWithItem(itemID, false, delay, option);
    }

    public void interactWithItemInvoke(int[] itemID, String option, long delay) {
        Widget itemWidget = getItem(itemID);
        if (itemWidget != null) {
            int id = itemOptionToId(itemWidget.getItemId(), option);
            MenuEntry entry = inventoryEntries.createInteractWithItem(itemWidget, id, idToMenuAction(id));
            if (entry != null) {
                actionQueue.delayTime(delay, () -> menuEntryInteractions.invokeMenuAction(entry));
            }
        }
    }

    public void interactWithItem(int[] itemID, boolean forceLeftClick, long delay, String... option) {
        List<Integer> boxedIds = Arrays.stream(itemID).boxed().collect(Collectors.toList());
        MenuEntry entry = getMenuEntry(boxedIds, Arrays.asList(option), forceLeftClick);
        if (entry != null) {
            Widget item = getItem(boxedIds);
            if (item != null) {
                mouseInteractions.doActionMsTime(entry, item.getBounds(), delay);
            }
        }
    }

    private MenuEntry getMenuEntry(List<Integer> itemID, List<String> option, boolean forceLeftClick) {
        Widget itemWidget = getItem(itemID);
        if (itemWidget != null) {
            int id = itemOptionToId(itemWidget.getItemId(), option);
            return inventoryEntries.createInteractWithItem(itemWidget, id, idToMenuAction(id), forceLeftClick);
        }
        return null;
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

    public Widget getItem(int id) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            Collection<Widget> items = getInventoryItems(List.of(id));
            for (Widget item : items) {
                if (item.getItemId() == id) {
                    return item;
                }
            }
        }

        return null;
    }

    public Widget getItem(int[] ids) {
        List<Integer> idList = Arrays.asList(Arrays.stream(ids).boxed().toArray(Integer[]::new));
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            Collection<Widget> items = getInventoryItems(idList);
            for (Widget item : items) {
                if (idList.contains(item.getItemId())) {
                    return item;
                }
            }
        }

        return null;
    }

    public Widget getItem(Collection<Integer> ids) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            Collection<Widget> items = getInventoryItems(ids);
            for (Widget item : items) {
                if (ids.contains(item.getItemId())) {
                    return item;
                }
            }
        }

        return null;
    }

    public void dropAllExcept(Collection<Integer> ids, boolean mouseClick, long delay) {
        if (bank.isOpen() || bank.isDepositBoxOpen()) {
            return;
        }
        Collection<Widget> inventoryItems = getAllInventoryItems();
        plugin.getExecutorService().submit(() ->
        {
            try {
                plugin.setIterating(true);
                for (Widget item : inventoryItems) {
                    if (ids.contains(item.getItemId())) {
                        continue;
                    }
                    sleep(delay);
                    MenuEntry entry = inventoryEntries.createDropItemEntry(item);
                    if (mouseClick)
                        mouseInteractions.doActionMsTime(entry, item.getBounds());
                    else
                        menuEntryInteractions.invokeMenuAction(entry);

                }
                plugin.setIterating(false);
            } catch (Exception e) {
                plugin.setIterating(false);
                e.printStackTrace();
            }
        });
    }

    public Widget getItemMenu(Collection<String> menuOptions) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            Collection<Widget> items = getItemsWidget();
            for (Widget item : items) {
                String[] menuActions = itemManager.getItemComposition(item.getItemId()).getInventoryActions();
                for (String action : menuActions) {
                    if (action != null && menuOptions.contains(action)) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    public Widget getWidgetItemMenu(ItemManager itemManager, String menuOption, int opcode) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            Collection<Widget> items = getItemsWidget();
            for (Widget item : items) {
                String[] menuActions = itemManager.getItemComposition(item.getId()).getInventoryActions();
                for (String action : menuActions) {
                    if (action != null && action.equals(menuOption)) {
                        return item;
                    }
                }
            }
        }
        return null;
    }



    public void dropItem(Widget item, boolean mouseClick) {
        assert !client.isClientThread();

        if(mouseClick)
            mouseInteractions.click(item.getBounds());
        else
            menuEntryInteractions.invokeMenuAction(inventoryEntries.createDropItemEntry(item));
    }

    public void dropInventory(boolean mouseClick, long sleep) {
        if (bank.isOpen() || bank.isDepositBoxOpen()) {
            return;
        }
        Collection<Integer> inventoryItems = getAllInventoryItems().stream().map(item -> item.getItemId()).collect(Collectors.toList());
        dropItems(inventoryItems, true, mouseClick, sleep);
    }

    private Widget getItemAtIndex(int itemId, int index){
        rebuildInventoryWidget();
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        List<Widget> items = getItems(inventoryWidget);

        Widget item = items.get(index);
        if(item != null && item.getItemId() == itemId)
            return item;

        return null;
    }

    public void dropItems(Collection<Integer> ids, boolean dropAll, boolean mouseClick, long delay) {
        if (bank.isOpen() || bank.isDepositBoxOpen()) {
            return;
        }
        Collection<Widget> inventoryItems = getInventoryItems(ids);
        plugin.getExecutorService().submit(() ->
        {
            try {
                plugin.setIterating(true);

                for (Widget item : inventoryItems) {
                    sleep(delay);
                    MenuEntry entry = inventoryEntries.createDropItemEntry(item);
                    if (mouseClick)
                        mouseInteractions.doActionMsTime(entry, item.getBounds());
                    else
                        menuEntryInteractions.invokeMenuAction(entry);
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

    public boolean isOpen() {
        if (client.getWidget(WidgetInfo.INVENTORY) == null) {
            return false;
        }
        return !client.getWidget(WidgetInfo.INVENTORY).isHidden();
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

    public int getItemCount(int id, boolean stackable) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        int total = 0;
        if (inventoryWidget != null) {
            Collection<Widget> items = getItemsWidget();
            for (Widget item : items) {
                if (item.getItemId() == id) {
                    if (stackable) {
                        return item.getItemQuantity();
                    }
                    total++;
                }
            }
        }
        return total;
    }

    public boolean containsItem(int itemID) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }

        Widget item = getItem(List.of(itemID));
        return item != null;
    }

    public boolean containsStackAmount(int itemID, int minStackAmount) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }
        Widget item = getItem(List.of(itemID));

        return item != null && item.getItemQuantity() >= minStackAmount;
    }

    public boolean containsItemAmount(int id, int amount, boolean stackable, boolean exactAmount) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        int total = 0;
        if (inventoryWidget != null) {
            Collection<Widget> items = getItemsWidget();
            for (Widget item : items) {
                if (item.getItemId() == id) {
                    if (stackable) {
                        total = item.getItemQuantity();
                        break;
                    }
                    total++;
                }
            }
        }
        return (!exactAmount || total == amount) && (total >= amount);
    }

    public boolean containsItemAmount(Collection<Integer> ids, int amount, boolean stackable, boolean exactAmount) {
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        int total = 0;
        if (inventoryWidget != null) {
            Collection<Widget> items = getItemsWidget();
            for (Widget item : items) {
                if (ids.contains(item.getItemId())) {
                    if (stackable) {
                        total = item.getItemQuantity();
                        break;
                    }
                    total++;
                }
            }
        }
        return (!exactAmount || total == amount) && (total >= amount);
    }

    public Collection<Widget> getItemsWidget() {
        Widget geWidget = client.getWidget(WidgetInfo.GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER);
        boolean geOpen = geWidget != null;
        boolean bankOpen = !geOpen && client.getItemContainer(InventoryID.BANK) != null;
        Widget inventoryWidget = bankOpen
                ? client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER)
                : geOpen
                ? geWidget
                : client.getWidget(WidgetInfo.INVENTORY);

        if (inventoryWidget == null) {
            return Collections.emptyList();
        }

        if (!bankOpen && !geOpen && inventoryWidget.isHidden()) {
            refreshInventory();
        }

        Widget[] children = inventoryWidget.getDynamicChildren();

        if (children == null) {
            return Collections.emptyList();
        }

        Collection<Widget> widgetItems = new ArrayList<>();
        for (Widget child : children) {
            if (child.getItemId() != 6512) {
                widgetItems.add(child);
            }
        }

        return widgetItems;
    }


    public void refreshInventory() {
        if (client.isClientThread())
            client.runScript(6009, 9764864, 28, 1, -1);
        else
            clientThread.invokeLater(() -> client.runScript(6009, 9764864, 28, 1, -1));
    }

    public boolean containsItem(Collection<Integer> itemIds) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }
        return getInventoryItems(itemIds).size() > 0;
    }

    public boolean containsAllOf(Collection<Integer> itemIds) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }
        for (int item : itemIds) {
            if (!containsItem(item)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsExcept(Collection<Integer> itemIds) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }
        Collection<Widget> inventoryItems = getAllInventoryItems();
        List<Integer> depositedItems = new ArrayList<>();

        for (Widget item : inventoryItems) {
            if (!itemIds.contains(item.getItemId())) {
                return true;
            }
        }
        return false;
    }

    public boolean onlyContains(Collection<Integer> itemIds) {
        if (client.getItemContainer(InventoryID.INVENTORY) == null) {
            return false;
        }

        Collection<Widget> inventoryItems = getAllInventoryItems();

        for (Widget item : inventoryItems) {
            if (!itemIds.contains(item.getItemId())) {
                return false;
            }
        }

        return true;
    }
}
