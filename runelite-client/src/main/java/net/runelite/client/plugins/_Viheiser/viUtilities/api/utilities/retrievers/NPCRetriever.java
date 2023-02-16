package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.retrievers;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;
import java.util.List;

public class NPCRetriever {
    @Inject
    private  Client client;

    public NPC getNearestNpcWithIds(int... npcIds) {
        NPC nearestNpc = findNearestNpcWithIds(npcIds);

        return nearestNpc;
    }

    private int calculateDistance(WorldPoint point1, WorldPoint point2) {
        return point1.distanceTo(point2);
    }

    private NPC findNearestNpcWithIds(int... npcIds) {
        NPC nearestNpc = null;
        int nearestDistance = Integer.MAX_VALUE;
        WorldPoint playerLocation =  client.getLocalPlayer().getWorldLocation();
        List<NPC> npcList = client.getNpcs();

        for (NPC npc : npcList) {
            for (int npcId : npcIds) {
                if (npc.getId() == npcId) {
                    int distance = calculateDistance(playerLocation, npc.getWorldLocation());
                    if (distance < nearestDistance) {
                        nearestNpc = npc;
                        nearestDistance = distance;
                    }
                }
            }
        }

        return nearestNpc;
    }
}
