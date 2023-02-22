package net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.entities;

import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.calculations.CalculatorUtils;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.interactions.ActionQueue;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.interactions.KeyboardInteractions;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.interactions.InvokeInteractions;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.interactions.MouseInteractions;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.menuentries.BankMenuEntries;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.objectlists.Banks;
import net.runelite.client.plugins._Viheiser.ViUtilities.ViUtilitiesPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.awt.event.KeyEvent.VK_ENTER;
import static net.runelite.api.widgets.WidgetID.BANK_PIN_GROUP_ID;
import static net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.interactions.ActionQueue.sleep;

@Singleton
@PluginDependency(ViUtilitiesPlugin.class)
public class BankUtils {
    @Inject
    private Client client;
    @Inject
    private ItemManager itemManager;
    @Inject
    private ClientThread clientThread;
    @Inject
    private InvokeInteractions invokeInteractions;
    @Inject
    private BankMenuEntries bankMenuEntries;
    @Inject
    private CalculatorUtils calculatorUtils;
    @Inject
    private InventoryUtils inventoryUtils;
    @Inject
    private KeyboardInteractions keyboardInteractions;
    @Inject
    private MouseInteractions mouseInteractions;
    @Inject
    private ViUtilitiesPlugin plugin;
    @Inject
    private ActionQueue actionQueue;
    private boolean iterating;
    public boolean isDepositBoxOpen() {
        return client.getWidget(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER) != null;
    }

    public boolean isOpen() {
        Widget bankWidget = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        // When you close the bank manually with a hot-key, the widget is still active but hidden.
        return client.getItemContainer(InventoryID.BANK) != null && !bankWidget.isHidden(); //TODO handle client thread for isHidden
    }

    public void close(boolean mouseClick, long delay) {
        if (!isOpen()) {
            return;
        }
        final int exitButtonId = 13;
        Widget bankCloseWidget = client.getWidget(BANK_PIN_GROUP_ID, exitButtonId);
        if (bankCloseWidget != null) {
            MenuEntry entry = bankMenuEntries.createCloseBank();
            if(mouseClick)
                plugin.getExecutorService().submit(() -> mouseInteractions.delayMouseClick(bankCloseWidget.getBounds(), entry, delay));
            else
                actionQueue.delayInvokesTime(delay, () -> invokeInteractions.invokeMenuAction(entry));
        }
    }

    public int getBankMenuOpcode(int bankID) {
        return Banks.BANK_CHECK_BOX.contains(bankID) ? MenuAction.GAME_OBJECT_FIRST_OPTION.getId() :
                MenuAction.GAME_OBJECT_SECOND_OPTION.getId();
    }

    //doesn't NPE
    public boolean containsAnyOf(int... ids) {
        if (isOpen()) {
            for (int id : ids) {
                if(foundItemInBankContainer(id)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsAny(Collection<Integer> ids) {
        if (isOpen()) {
            for (int id : ids) {
                if(foundItemInBankContainer(id)){
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean contains(int itemID, int minStackAmount) {
        if (isOpen()) {
            ItemContainer bankItemContainer = client.getItemContainer(InventoryID.BANK);
            if (bankItemContainer != null) {
                for (Item item : bankItemContainer.getItems()) {
                    if (item.getId() == itemID) {
                        return item.getQuantity() >= minStackAmount;
                    }
                }
            }
        }
        return false;
    }

    public Widget getBankItemWidget(int id) {
        if (!isOpen()) {
            return null;
        }

        Widget bankItem = findBankItemWidget(id);
        if (bankItem != null) {
            return bankItem;
        } else {
            return null;
        }
    }

    private Widget findBankItemWidget(int itemId){
        Widget bankContainer = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        for(Widget item : bankContainer.getChildren()){
            if(itemId == item.getItemId()){
                return item;
            }
        }

        return null;
    }

    //doesn't NPE
    public Widget getBankItemWidgetAnyOf(int... ids) {
        if (!isOpen()) {
            return null;
        }

        for(int id : ids){
            Widget item = findBankItemWidget(id);
            if(item != null){
                return item;
            }
        }

        return null;
    }

    public Widget getBankItemWidgetAnyOf(Collection<Integer> ids) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return null;
        }

        for(int id : ids){
            Widget item = findBankItemWidget(id);
            if(item != null){
                return item;
            }
        }

        return null;
    }

    public void depositAll(boolean mouseClick, long delay) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }
        Widget depositInventoryWidget = client.getWidget(WidgetInfo.BANK_DEPOSIT_INVENTORY);
        if ((depositInventoryWidget != null)) {
            if (isDepositBoxOpen()) {
                MenuEntry entry = bankMenuEntries.createDepositBoxDepositInventory();
                if (mouseClick)
                    plugin.getExecutorService().submit(() -> mouseInteractions.delayMouseClick(depositInventoryWidget.getBounds(), entry, delay));
                else
                    actionQueue.delayInvokesTime(delay, () -> invokeInteractions.invokeMenuAction(entry));
            } else {
                MenuEntry entry = bankMenuEntries.createBankDepositInventory();
                if (mouseClick)
                    plugin.getExecutorService().submit(() -> mouseInteractions.delayMouseClick(depositInventoryWidget.getBounds(), entry, delay));
                else
                    actionQueue.delayInvokesTime(delay, () -> invokeInteractions.invokeMenuAction(entry));
            }
        }
    }

    public void depositAllExcept(Collection<Integer> ids, boolean mouseClick, long delay) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }

        Collection<Widget> inventoryItems = inventoryUtils.getAllInventoryItems();
        List<Integer> depositedItems = new ArrayList<>();
        plugin.getExecutorService().submit(() ->
        {
            try {
                iterating = true;
                int emptyWidgetSlot = 6512;
                for (Widget item : inventoryItems) {
                    if (!ids.contains(item.getId()) && item.getId() != emptyWidgetSlot && !depositedItems.contains(item.getId()))
                    {
                        depositAllOfItem(item, mouseClick, delay);
                        depositedItems.add(item.getId());
                    }
                }
                iterating = false;
                depositedItems.clear();
            } catch (Exception e) {
                iterating = false;
                e.printStackTrace();
            }
        });
    }

    public void depositAllOfItem(Widget item, boolean mouseClick, long delay) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }
        boolean depositBox = isDepositBoxOpen();
        MenuEntry entry = bankMenuEntries.createDepositAllOfItem(item, depositBox);
            if (mouseClick)
                plugin.getExecutorService().submit(() -> mouseInteractions.click(item.getBounds()));
            else
                actionQueue.delayInvokesTime(delay, () -> invokeInteractions.invokeMenuAction(entry));
    }

    public void depositAllOfItem(int itemID, boolean mouseClick, long delay) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }
        depositAllOfItem(inventoryUtils.getItem(itemID), mouseClick, delay);
    }

    public void depositAllOfItems(Collection<Integer> itemIDs, boolean mouseClick, long delay) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }
        Collection<Widget> inventoryItems = inventoryUtils.getAllInventoryItems();
        List<Integer> depositedItems = new ArrayList<>();
        plugin.getExecutorService().submit(() ->
        {
            try {
                iterating = true;
                for (Widget item : inventoryItems) {
                    if (itemIDs.contains(item.getId()) && !depositedItems.contains(item.getId()))
                    {
                        depositAllOfItem(item, mouseClick, delay);
                        depositedItems.add(item.getId());
                    }
                }
                iterating = false;
                depositedItems.clear();
            } catch (Exception e) {
                iterating = false;
                e.printStackTrace();
            }
        });
    }

    public void depositOneOfItem(Widget item) {
        if (!isOpen() && !isDepositBoxOpen() || item == null) {
            return;
        }
        boolean depositBox = isDepositBoxOpen();

        invokeInteractions.invokeMenuAction(bankMenuEntries.createDepositOneOfItem(item, depositBox));
    }

    public void depositOneOfItem(int itemID) {
        if (!isOpen() && !isDepositBoxOpen()) {
            return;
        }

        depositOneOfItem(inventoryUtils.getItem(itemID));
    }

    private void withdrawAllItem(Widget bankItemToWithdraw) {
        invokeInteractions.invokeMenuAction(bankMenuEntries.createWithdrawAllItems(bankItemToWithdraw));
    }

    public void withdrawAllItem(int bankItemID) {
        Widget item = getBankItemWidget(bankItemID);
        if (item != null) {
            withdrawAllItem(item);
        }
    }

    private void withdrawItem(Widget bankItemToWithdraw) {
        invokeInteractions.invokeMenuAction(bankMenuEntries.createWithdrawOneItem(bankItemToWithdraw));
    }

    public void withdrawItem(int bankItemID) {
        Widget item = getBankItemWidget(bankItemID);
        if (item != null) {
            withdrawItem(item);
        }
    }

    public void withdrawItemAmount(int bankItemID, int amount) {
            Widget item = getBankItemWidget(bankItemID);
            if (item != null) {
                int identifier;
                switch (amount) {
                    case 1:
                        identifier = (client.getVarbitValue(6590) == 0) ? 1 : 2;
                        break;
                    case 5:
                        identifier = 3;
                        break;
                    case 10:
                        identifier = 4;
                        break;
                    default:
                        identifier = (client.getVarbitValue(3960) == amount) ? 5 : 6;
                        break;
                }
                invokeInteractions.invokeMenuAction(bankMenuEntries.createWithdrawItemAmount(item, amount, identifier));
                if (identifier == 6) {
                    plugin.getExecutorService().submit(() -> {
                        int toSleep = calculatorUtils.getRandomIntBetweenRange(1000, 1500);
                        sleep(toSleep);
                        keyboardInteractions.typeString(String.valueOf(amount));
                        toSleep = calculatorUtils.getRandomIntBetweenRange(80, 250);
                        sleep(toSleep);
                        keyboardInteractions.pressKey(VK_ENTER);
                    });
                }
            }
    }


    private boolean foundItemInBankContainer(int itemId){
        ItemContainer bankItemContainer = client.getItemContainer(InventoryID.BANK);
        for (Item item : bankItemContainer.getItems()) {
            if (itemManager.getItemComposition(item.getId()).getId() == itemId) {
                return true;
            }
        }

        return false;
    }
}
