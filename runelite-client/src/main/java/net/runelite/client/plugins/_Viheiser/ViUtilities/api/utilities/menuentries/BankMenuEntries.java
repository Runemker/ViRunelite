package net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.menuentries;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import javax.inject.Inject;

public class BankMenuEntries {
    @Inject
    private Client client;

    public MenuEntry createDepositBoxDepositInventory(){
        final int depositInventoryWidgetId = 12582916;
        return createMenuEntry(1, MenuAction.CC_OP, -1, depositInventoryWidgetId, false);
    }

    public MenuEntry createBankDepositInventory(){
        final int depositInventoryWidgetId = 12582916;
        return createMenuEntry(1, MenuAction.CC_OP, -1, depositInventoryWidgetId, false);
    }
    public MenuEntry createCloseBank() {
        return createMenuEntry(1, MenuAction.CC_OP, 11, 786434, false);
    }

    public MenuEntry createDepositAllOfItem(Widget itemToDeposit, boolean isDepositBox) {
        return createMenuEntry((isDepositBox) ? 1 : 8, MenuAction.CC_OP, itemToDeposit.getIndex(),
                (isDepositBox) ? 12582914 : 983043, false);
    }

    public MenuEntry createDepositOneOfItem(Widget itemToDeposit, boolean isDepositBox){
        return  createMenuEntry((client.getVarbitValue(6590) == 0) ? 2 : 3, MenuAction.CC_OP, itemToDeposit.getIndex(),
                (isDepositBox) ? 12582914 : 983043, false);
    }

    public MenuEntry createWithdrawAllItems(Widget bankItemToWithdraw){
        return  createMenuEntry(7, MenuAction.CC_OP, bankItemToWithdraw.getIndex(), WidgetInfo.BANK_ITEM_CONTAINER.getId(), false);
    }

    public MenuEntry createWithdrawOneItem(Widget bankItemToWithdraw){
        return  createMenuEntry((client.getVarbitValue(6590) == 0) ? 1 : 2, MenuAction.CC_OP,
                bankItemToWithdraw.getIndex(), WidgetInfo.BANK_ITEM_CONTAINER.getId(), false);
    }

    public MenuEntry createWithdrawItemAmount(Widget bankItemToWithdraw, int amount, int identifier){
        return  createMenuEntry(identifier, MenuAction.CC_OP, bankItemToWithdraw.getIndex(), WidgetInfo.BANK_ITEM_CONTAINER.getId(), false);
    }

    private MenuEntry createMenuEntry(int identifier, MenuAction type, int param0, int param1, boolean forceLeftClick) {
        return client.createMenuEntry(0).setOption("").setTarget("").setIdentifier(identifier).setType(type)
                .setParam0(param0).setParam1(param1).setForceLeftClick(forceLeftClick);
    }

}
