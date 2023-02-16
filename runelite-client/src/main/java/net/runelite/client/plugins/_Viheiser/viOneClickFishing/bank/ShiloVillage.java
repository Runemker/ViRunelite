package net.runelite.client.plugins._Viheiser.viOneClickFishing.bank;

import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities.NpcUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.NpcMenuEntries;

import javax.inject.Inject;

public class ShiloVillage extends BankBase{
    @Inject
    private Client client;
    @Inject
    private NpcUtils npcUtils;
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

        NPC bankNpc = npcUtils.findNearestNpc(NpcID.ROD_FISHING_SPOT_1515);
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
        return npcMenuEntries.createNpcOption(npcUtils.findNearestNpc(NpcID.BANKER_3093).getIndex(), MenuAction.NPC_THIRD_OPTION);
    }

    @Override
    public boolean canSeeBank() {
        Player player = client.getLocalPlayer();
        if(player == null)
            return false;

        NPC bankNpc = npcUtils.findNearestNpc(NpcID.BANKER_3093);
        if(bankNpc == null)
            return false;

        return true;
    }
}