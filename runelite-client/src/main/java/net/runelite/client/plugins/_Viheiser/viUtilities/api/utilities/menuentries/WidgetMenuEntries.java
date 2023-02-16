package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.enums.BankOptions;

import javax.inject.Inject;

@Slf4j
public class WidgetMenuEntries {
    @Inject
    private Client client;

    public MenuEntry createWithdrawItemFromBank(Widget bankItem, BankOptions option) {
        int index = -1;

        if (bankItem.getActions() != null) {
            index = getIndexOfAction(bankItem.getActions(), option);
        }

        return createMenuEntry(index, MenuAction.CC_OP, bankItem.getIndex(), 786445, false);
    }

    public MenuEntry createDepositItemInBank(Widget item) {
        return createMenuEntry(
                getIndexOfAction(item.getActions(), BankOptions.DEPOSIT_ALL),
                MenuAction.CC_OP_LOW_PRIORITY,
                item.getIndex(),
                WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getId(),
                false);
    }

    public MenuEntry openInventory() {
        int invButtonId = 10747958;
        return createMenuEntry(1, MenuAction.CC_OP, -1, invButtonId, false);
    }

    public MenuEntry multiSkillMenuMake(int makeId){
        return createMenuEntry(1, MenuAction.CC_OP, -1, makeId, false);
    }


    public MenuEntry createToggleRunEntry(){
        Widget runOrb = client.getWidget(WidgetInfo.MINIMAP_TOGGLE_RUN_ORB);
        return createMenuEntry(1, MenuAction.CC_OP, -1, runOrb.getId(), false);
    }

    public MenuEntry createTogglePrayer(){
        Widget prayerWidget = client.getWidget(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB);
        return createMenuEntry(1, MenuAction.CC_OP, -1, prayerWidget.getId(), false);
    }

    private int getIndexOfAction(String[] actions, BankOptions bankOptions) {
        int index = 1;
        for (String action : actions) {
            if (action != null) {
                if (action.toLowerCase().contains(bankOptions.getText().toLowerCase())) {
                    return index;
                }
            }
            index++;
        }

        return -1;
    }

    private MenuEntry createMenuEntry(int identifier, MenuAction type, int param0, int param1, boolean forceLeftClick) {
        return client.createMenuEntry(0).setOption("").setTarget("").setIdentifier(identifier).setType(type)
                .setParam0(param0).setParam1(param1).setForceLeftClick(forceLeftClick);
    }
}
