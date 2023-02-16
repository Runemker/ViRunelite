package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.reflection.ReflectionManager;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.mappings.MethodNameMapping;

import javax.inject.Inject;

@Slf4j
public class MenuEntryInteraction {
    @Inject
    private ReflectionManager reflectionManager;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;

    public void invokeMenuAction(MenuEntry menuEntry){
        insertMenuItem(menuEntry);
        _invokeMenuAction(menuEntry);
    }

    private void _invokeMenuAction(MenuEntry menuEntry) {
        int itemId = getItemId(menuEntry.getIdentifier(), menuEntry.getType().getId(), menuEntry.getParam0(), menuEntry.getParam1(), -1);
        Object[] argList = {
                menuEntry.getParam0(), menuEntry.getParam1(), menuEntry.getType().getId(),
                menuEntry.getIdentifier(), itemId, menuEntry.getOption(),
                menuEntry.getTarget(), -1, -1, (byte) 0
        };


        reflectionManager.invokeMenuAction(MethodNameMapping.INVOKE_MENU_ACTION, argList);
    }

    public void insertMenuItem(MenuEntry menuEntry){
        Object[] argList = {
                menuEntry.getOption(), menuEntry.getTarget(), menuEntry.getType(),
                menuEntry.getIdentifier(), menuEntry.getParam0(), menuEntry.getParam1(),
                menuEntry.getItemId(), menuEntry.isForceLeftClick()
        };

        reflectionManager.insertMenuEntry(MethodNameMapping.INSERT_MENU_ACTION, argList);
    }

    private int getItemId(int identifier, int opcode, int param0, int param1, int currentItemId)
    {
        switch (opcode)
        {
            case 1006:
                currentItemId = 0;
                break;
            case 25:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 58:
            case 1005:
                currentItemId = getItemId(param0, param1, currentItemId);
                break;

            case 57:
            case 1007:
                if (identifier >= 1 && identifier <= 10)
                {
                    currentItemId = getItemId(param0, param1, currentItemId);
                }

                break;
        }

        return currentItemId;
    }

    private int getItemId(int param0, int param1, int currentItemId)
    {
        Widget widget = client.getWidget(param1);
        if (widget != null)
        {
            int group = param1 >>> 16;
            Widget[] children = widget.getChildren();
            if (children != null && children.length >= 2 && group == WidgetID.EQUIPMENT_GROUP_ID)
            {
                param0 = 1;
            }

            Widget child = widget.getChild(param0);
            if (child != null)
            {
                if (currentItemId != child.getItemId())
                {
                    return child.getItemId();
                }
            }
        }

        return currentItemId;
    }
}
