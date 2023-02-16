package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import javax.inject.Inject;

@Slf4j
public class InventoryEntries {
    @Inject
    Client client;
    public MenuEntry createUseItem(Widget item) {
        return createMenuEntry(0, MenuAction.WIDGET_TARGET_ON_WIDGET, item.getIndex(), WidgetInfo.INVENTORY.getId(), false);
    }

    public MenuEntry createDropItemEntry(Widget itemToDrop) {
        if (itemToDrop == null)
            return null;

        return createMenuEntry(
                7,
                MenuAction.CC_OP_LOW_PRIORITY,
                itemToDrop.getIndex(),
                WidgetInfo.INVENTORY.getId(),
                false);
    }

    private MenuEntry createMenuEntry(int identifier, MenuAction type, int param0, int param1, boolean forceLeftClick) {
        return client.createMenuEntry(0).setOption("").setTarget("").setIdentifier(identifier).setType(type)
                .setParam0(param0).setParam1(param1).setForceLeftClick(forceLeftClick);
    }

    private MenuEntry createMenuEntry(int identifier, MenuAction type, int param0, int param1, int itemId, int itemOp, boolean forceLeftClick) {
        return client.createMenuEntry(0).setOption("").setTarget("").setIdentifier(identifier).setType(type)
                .setParam0(param0).setParam1(param1).setForceLeftClick(forceLeftClick);
    }
}
