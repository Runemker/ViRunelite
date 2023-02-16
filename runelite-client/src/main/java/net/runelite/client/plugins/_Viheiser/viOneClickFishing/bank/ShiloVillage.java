package net.runelite.client.plugins._Viheiser.viOneClickFishing.bank;

import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.retrievers.NPCRetriever;
import net.runelite.client.plugins._Viheiser.viUtilities.api.menuEntries.NpcMenuEntries;

import javax.inject.Inject;

public class ShiloVillage extends BankBase{
    @Inject
    private Client client;
    @Inject
    private NPCRetriever npcLocator;
    @Inject
    private NpcMenuEntries npcMenuEntries;
    public ShiloVillage(Client client) {
        super(client);
        this.client = client;
    }

    @Override
    public boolean canSeeFish() {
        Player player = client.getLocalPlayer();
        if(player == null)
            return false;

        NPC bankNpc = npcLocator.getNearestNpcWithIds(NpcID.ROD_FISHING_SPOT_1515);
        if(bankNpc == null)
            return false;

        return true;
    }

    @Override
    public WorldArea getAreaNearBank() {
        return new WorldArea(2849, 2958, 6, 5, 0);
    }

    @Override
    public WorldArea getAreaNearFish() {
        return new WorldArea(2853, 2969, 6, 4, 0);
    }

    @Override
    public boolean isNpc() {
        return true;
    }

    @Override
    public MenuEntry openBank() {
        return npcMenuEntries.createOpenBank(NpcID.BANKER_3093);
    }

    @Override
    public boolean canSeeBank() {
        Player player = client.getLocalPlayer();
        if(player == null)
            return false;

        NPC bankNpc = npcLocator.getNearestNpcWithIds(NpcID.BANKER_3093);
        if(bankNpc == null)
            return false;

        return true;
    }
}