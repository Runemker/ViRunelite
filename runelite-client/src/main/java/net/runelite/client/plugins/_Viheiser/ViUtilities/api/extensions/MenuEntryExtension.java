package net.runelite.client.plugins._Viheiser.ViUtilities.api.extensions;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.interactions.ItemInteractions;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class MenuEntryExtension {
    @Inject
    private Client client;
    @Inject
    private ItemInteractions itemInteractions;
    String option;
    String target;
    int identifier;
    MenuAction type;
    int opcode;
    int param0;
    int param1;
    boolean forceLeftClick;
    boolean hasMenuEntry = false;
    public boolean consumeClick;
    public boolean modifiedMenu;
    public int modifiedItemID;
    public int modifiedItemIndex;
    public int modifiedOpCode;

    public void setSelectedItem(WidgetInfo info) {
        final Widget widget = client.getWidget(info);

        if (widget != null) {
            itemInteractions.setSelectedItem(widget);
        }
    }

    public void clearMenuEntry(){
        this.hasMenuEntry = false;
    }

    public void setMenuEntry(String option, String target, int identifier, MenuAction type, int param0, int param1, boolean forceLeftClick) {
        this.option = option;
        this.target = target;
        this.identifier = identifier;
        this.type = type;
        this.param0 = param0;
        this.param1 = param1;
        this.forceLeftClick = forceLeftClick;
        this.hasMenuEntry = true;
    }

    public void setMenuEntry(MenuEntry menuEntry) {
        setMenuEntry(menuEntry.getOption(),
                menuEntry.getTarget(),
                menuEntry.getIdentifier(),
                menuEntry.getType(),
                menuEntry.getParam0(),
                menuEntry.getParam1(),
                false
        );
    }
    public void setMenuEntry(MenuEntry menuEntry, boolean consume) {
        setMenuEntry(menuEntry);
        consumeClick = consume;
    }

    public void setModifiedEntry(MenuEntry menuEntry, int itemID, int itemIndex, int opCode) {
        setMenuEntry(menuEntry);
        modifiedMenu = true;
        modifiedItemID = itemID;
        modifiedItemIndex = itemIndex;
        modifiedOpCode = opCode;
    }

    public MenuEntry buildMenuEntry(){
        return client.createMenuEntry(1)
                .setOption(this.option)
                .setTarget(this.target)
                .setType(this.type)
                .setParam0(this.param0)
                .setParam1(this.param1)
                .setIdentifier(this.identifier)
                .setForceLeftClick(this.forceLeftClick);
    }

    public boolean hasMenuEntry(){
        return hasMenuEntry;
    }
}
