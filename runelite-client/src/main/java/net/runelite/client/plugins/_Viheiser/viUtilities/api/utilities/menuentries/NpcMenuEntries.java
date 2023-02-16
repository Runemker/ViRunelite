package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;

import javax.inject.Inject;

public class NpcMenuEntries {
    @Inject
    private Client client;
    public MenuEntry createThirdNPCOption(int npcIndex){
        return createMenuEntry(
                npcIndex,
                MenuAction.NPC_THIRD_OPTION,
                0,
                0,
                false);
    }
    public MenuEntry createNpcOption(int npcIndex, MenuAction npcOption){
        return createMenuEntry(
                npcIndex,
                npcOption,
                0,
                0,
                false);
    }

    private MenuEntry createMenuEntry(int identifier, MenuAction type, int param0, int param1, boolean forceLeftClick) {
        return client.createMenuEntry(0).setOption("").setTarget("").setIdentifier(identifier).setType(type)
                .setParam0(param0).setParam1(param1).setForceLeftClick(forceLeftClick);
    }
}
