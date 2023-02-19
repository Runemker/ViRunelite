package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.calculations.CalculatorUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.mappings.FieldNameMapping;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.mappings.MethodNameMapping;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.reflection.ReflectionManager;

import javax.inject.Inject;

@Slf4j
public class MenuEntryInteractions {
    @Inject
    private ReflectionManager reflectionManager;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private CalculatorUtils calculatorUtils;
    @Inject
    private ActionQueue actionQueue;

    public void invokeMenuAction(MenuEntry menuEntry, long delay){
        actionQueue.delayTime(delay, () -> {
            insertMenuItem(menuEntry);
            _invokeMenuAction(menuEntry);
        });
    }

    public void invokeMenuAction(MenuEntry menuEntry){
        addMenuEntry(menuEntry);
        _invokeMenuAction(menuEntry);
    }

    private void _invokeMenuAction(MenuEntry menuEntry) {
        int itemId = getItemId(menuEntry.getIdentifier(), menuEntry.getType().getId(), menuEntry.getParam0(), menuEntry.getParam1(), -1);
        Object[] argList = {
                menuEntry.getParam0(), menuEntry.getParam1(), menuEntry.getType().getId(),
                menuEntry.getIdentifier(), itemId, menuEntry.getOption(),
                menuEntry.getTarget(), getRandomY(), getRandomX(), (byte) 0
        };
        client.getViewportHeight();
        client.getViewportWidth();

        reflectionManager.invokeMenuAction(MethodNameMapping.INVOKE_MENU_ACTION, argList);
    }

    private int getRandomY(){
        int clientHeight = client.getViewportHeight();
        return calculatorUtils.getRandomIntBetweenRange(15, clientHeight / 2);
    }

    private int getRandomX(){
        int clientWidth = client.getViewportWidth();
        return calculatorUtils.getRandomIntBetweenRange(15, clientWidth / 2);
    }

    public void insertMenuItem(MenuEntry menuEntry){
        Object[] argList = {
                menuEntry.getOption(), menuEntry.getTarget(), menuEntry.getType(),
                menuEntry.getIdentifier(), menuEntry.getParam0(), menuEntry.getParam1(),
                menuEntry.getItemId(), menuEntry.isForceLeftClick()
        };

        reflectionManager.insertMenuEntry(MethodNameMapping.INSERT_MENU_ACTION, argList);
    }

    public void addMenuEntry(MenuEntry menuEntry){
        int menuOptionsCount = 499;//reflectionManager.getIntField(FieldNameMapping.MENU_OPTIONS_COUNT, client);
        Object menuArguments1AsObject = reflectionManager.getField(FieldNameMapping.MENU_ARGUMENTS_1);
        Object menuArguments2AsObject = reflectionManager.getField(FieldNameMapping.MENU_ARGUMENTS_2);
        Object menuOpcodesAsObject = reflectionManager.getField(FieldNameMapping.MENU_OPCODES);
        Object menuIdentifiersAsObject = reflectionManager.getField(FieldNameMapping.MENU_IDENTIFIERS);
        Object menuItemIdsAsObject = reflectionManager.getField(FieldNameMapping.MENU_ITEM_IDS);

        if(menuArguments1AsObject instanceof int[]){
            int[] menuArguments1 = (int[]) menuArguments1AsObject;
            menuArguments1[menuOptionsCount] = menuEntry.getParam0();
            reflectionManager.setFieldValue(FieldNameMapping.MENU_ARGUMENTS_1, menuArguments1);
        }
        if(menuArguments2AsObject instanceof int[]){
            int[] menuArguments2 = (int[]) menuArguments2AsObject;
            menuArguments2[menuOptionsCount] = menuEntry.getParam1();
            reflectionManager.setFieldValue(FieldNameMapping.MENU_ARGUMENTS_2, menuArguments2);
        }
        if(menuOpcodesAsObject instanceof int[]){
            int[] menuOpcodes = (int[]) menuOpcodesAsObject;
            int opcode = menuOpcodes[499];
            short mod = 0;
            if (opcode >= 2000) {
                mod = 2000;
            }

            menuOpcodes[menuOptionsCount] = menuEntry.getType().getId() + mod;
            reflectionManager.setFieldValue(FieldNameMapping.MENU_OPCODES, menuOpcodes);
        }
        if(menuIdentifiersAsObject instanceof int[]){
            int[] menuIdentifiers = (int[]) menuIdentifiersAsObject;
            menuIdentifiers[menuOptionsCount] = menuEntry.getIdentifier();
            reflectionManager.setFieldValue(FieldNameMapping.MENU_IDENTIFIERS, menuIdentifiers);
        }
        if(menuItemIdsAsObject instanceof int[]){
            int[] menuItemIds = (int[]) menuItemIdsAsObject;
            menuItemIds[menuOptionsCount] = menuEntry.getItemId();
            reflectionManager.setFieldValue(FieldNameMapping.MENU_ITEM_IDS, menuItemIds);
        }
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
