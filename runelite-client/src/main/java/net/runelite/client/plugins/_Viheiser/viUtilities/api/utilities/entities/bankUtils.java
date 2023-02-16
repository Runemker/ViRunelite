package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities;

import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.objectLists.Banks;

public class bankUtils {
    @Inject
    private Client client;
    @Inject
    private ItemManager itemManager;

    @Inject
    private ClientThread clientThread;
    public boolean isDepositBoxOpen() {
        return client.getWidget(WidgetInfo.DEPOSIT_BOX_INVENTORY_ITEMS_CONTAINER) != null;
    }

    public boolean isOpen() {
        Widget bankWidget = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        // When you close the bank manually with a hot-key, the widget is still active but hidden.
        return client.getItemContainer(InventoryID.BANK) != null && !bankWidget.isHidden(); //TODO handle client thread for isHidden
    }

    public void close() {
        if (!isOpen()) {
            return;
        }
//        menu.setEntry(new LegacyMenuEntry("", "", 1, MenuAction.CC_OP.getId(), 11, 786434, false)); //close bank
//        Widget bankCloseWidget = client.getWidget(WidgetInfo.BANK_PIN_EXIT_BUTTON);
//        if (bankCloseWidget != null) {
//            executorService.submit(() -> mouse.handleMouseClick(bankCloseWidget.getBounds()));
//            return;
//        }
//        mouse.delayMouseClick(new Point(0, 0), calc.getRandomIntBetweenRange(10, 100));
    }

    public int getBankMenuOpcode(int bankID) {
        return Banks.BANK_CHECK_BOX.contains(bankID) ? MenuAction.GAME_OBJECT_FIRST_OPTION.getId() :
                MenuAction.GAME_OBJECT_SECOND_OPTION.getId();
    }

    //doesn't NPE
    public boolean contains(String itemName) {
        if (isOpen()) {
            ItemContainer bankItemContainer = client.getItemContainer(InventoryID.BANK);

            for (Item item : bankItemContainer.getItems()) {
                if (itemManager.getItemComposition(item.getId()).getName().equalsIgnoreCase(itemName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
