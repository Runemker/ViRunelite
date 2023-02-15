package net.runelite.client.plugins.viUtilities.api.menuEntries;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.client.plugins.viUtilities.api.common.retrievers.NPCRetriever;

import javax.inject.Inject;
import java.util.Set;

public class NpcMenuEntries {
    @Inject
    private Client client;
    @Inject
    private NPCRetriever npcRetriever;
    public MenuEntry createOpenBank(int npcId){
        NPC bankNpc = npcRetriever.getNearestNpcWithIds(npcId);

        if (bankNpc != null) {
            return createMenuEntry(
                    bankNpc.getIndex(),
                    MenuAction.NPC_THIRD_OPTION,
                    0,
                    0,
                    false);
        }

        return null;
    }

    public MenuEntry createFishMenuEntry(Set<Integer> npcIds) {
        NPC bankNpc = npcRetriever.getNearestNpcWithIds(npcIds.stream().mapToInt(Integer::intValue).toArray());

        return createMenuEntry(
                bankNpc.getIndex(),
                MenuAction.NPC_FIRST_OPTION,
                0,
                0,
                false);
    }

    private MenuEntry createMenuEntry(int identifier, MenuAction type, int param0, int param1, boolean forceLeftClick) {
        return client.createMenuEntry(0).setOption("").setTarget("").setIdentifier(identifier).setType(type)
                .setParam0(param0).setParam1(param1).setForceLeftClick(forceLeftClick);
    }
}
