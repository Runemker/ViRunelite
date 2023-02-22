package net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.entities;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.interactions.ActionQueue;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.interactions.InvokeInteractions;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.interactions.MouseInteractions;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.menuentries.NpcMenuEntries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class NpcUtils {
    @Inject
    private Client client;
    @Inject
    private NpcMenuEntries npcMenuEntries;
    @Inject
    private InvokeInteractions invokeInteractions;
    @Inject
    private MouseInteractions mouseInteractions;
    @Inject
    private ActionQueue actionQueue;
    private int calculateDistance(WorldPoint point1, WorldPoint point2) {
        return point1.distanceTo(point2);
    }

    public void interactWithNpc(NPC npc, String actionText, boolean mouseClick, long delay) {
        String[] npcActions = npc.getComposition().getActions();
        int actionIndex = findActionIndex(npcActions, actionText);
        if (actionIndex == -1) {
            return;
        }
        MenuAction npcOption = getNpcOption(actionIndex);
        if (npcOption == null) {
            return;
        }
        MenuEntry entry = npcMenuEntries.createNpcOption(npc.getIndex(), npcOption);
        if(mouseClick)
            mouseInteractions.delayMouseClick(npc.getConvexHull().getBounds(), entry, delay);
        else {
            actionQueue.delayInvokesTime(delay, () -> invokeInteractions.invokeMenuAction(entry));
        }
    }

    public void interactWithNpc(NPC npc, MenuAction npcOption, boolean mouseClick, long delay) {
        MenuEntry entry = npcMenuEntries.createNpcOption(npc.getIndex(), npcOption);
        if(mouseClick)
            mouseInteractions.delayMouseClick(npc.getConvexHull().getBounds(), entry, delay);
        else
            actionQueue.delayInvokesTime(delay, () -> invokeInteractions.invokeMenuAction(entry));
    }

    private int findActionIndex(String[] npcActions, String actionText) {
        for (int i = 0; i < npcActions.length; i++) {
            if (npcActions[i].equalsIgnoreCase(actionText)) {
                return i;
            }
        }
        return -1;
    }

    private MenuAction getNpcOption(int actionIndex) {
        MenuAction[] options = {
                MenuAction.NPC_FIRST_OPTION,
                MenuAction.NPC_SECOND_OPTION,
                MenuAction.NPC_THIRD_OPTION,
                MenuAction.NPC_FOURTH_OPTION,
                MenuAction.NPC_FIFTH_OPTION
        };
        if (actionIndex >= 0 && actionIndex < options.length) {
            return options[actionIndex];
        }
        return null;
    }


    public NPC findNearestNpc(String... npcNames) {
        if(!validChecks()) return null;
        List<NPC> matchingNpcs = findMatchingNpcs(npcNames);
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        return findNearestNpc(matchingNpcs, playerLocation);
    }

    public NPC findNearestNpc(int... npcIds) {
        if(!validChecks()) return null;
        List<NPC> matchingNpcs = findMatchingNpcs(npcIds);
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        return findNearestNpc(matchingNpcs, playerLocation);
    }

    public NPC findNearestNpc(Collection<Integer> npcIds) {
        if(!validChecks()) return null;
        List<NPC> matchingNpcs = findMatchingNpcs(npcIds);
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        return findNearestNpc(matchingNpcs, playerLocation);
    }

    private List<NPC> findMatchingNpcs(Object... npcIdsOrNames) {
        List<NPC> matchingNpcs = new ArrayList<>();
        List<NPC> npcList = client.getNpcs();
        Object[] idsOrNamesArray = checkIfNested(npcIdsOrNames);

        for (NPC npc : npcList) {
            for (Object npcIdOrName : idsOrNamesArray) {
                boolean match = false;
                if (npcIdOrName instanceof Integer && npc.getId() == (int) npcIdOrName) {
                    match = true;
                } else if (npcIdOrName instanceof String && npc.getName().equals(npcIdOrName)) {
                    match = true;
                }
                if (match) {
                    matchingNpcs.add(npc);
                    break;
                }
            }
        }

        return matchingNpcs;
    }

    @NotNull
    private static Object[] checkIfNested(Object[] npcIdsOrNames) {
        Object[] idsOrNamesArray;
        if (npcIdsOrNames.length == 1) {
            idsOrNamesArray = ((Collection<?>) npcIdsOrNames[0]).toArray();
        } else {
            idsOrNamesArray = npcIdsOrNames;
        }

        return idsOrNamesArray;
    }

    private NPC findNearestNpc(List<NPC> npcs, WorldPoint playerLocation) {
        NPC nearestNpc = null;
        int nearestDistance = Integer.MAX_VALUE;

        for (NPC npc : npcs) {
            int distance = calculateDistance(playerLocation, npc.getWorldLocation());
            if (distance < nearestDistance) {
                nearestNpc = npc;
                nearestDistance = distance;
            }
        }

        return nearestNpc;
    }

    private List<NPC> getSortedNpcListByDistance(int... npcIds) {
        WorldPoint playerLocation =  client.getLocalPlayer().getWorldLocation();
        List<NPC> npcsWithId = client.getNpcs().stream()
                .filter(npc -> Arrays.stream(npcIds).anyMatch(id -> id == npc.getId())).collect(Collectors.toList());

        // Sort the filtered NPCs based on their distance from the given point
        sortNpcsByDistance(npcsWithId, playerLocation);

        return npcsWithId;
    }

    private void sortNpcsByDistance(List<NPC> npcs, WorldPoint point) {
        Collections.sort(npcs, new Comparator<NPC>() {
            @Override
            public int compare(NPC npc1, NPC npc2) {
                int dist1 = point.distanceTo(npc1.getWorldLocation());
                int dist2 = point.distanceTo(npc2.getWorldLocation());
                return Integer.compare(dist1, dist2);
            }
        });
    }

    private List<NPC> findNpcsWithIds(int... ids) {
        List<NPC> foundNpcs = new ArrayList<>();
        List<NPC> npcList = client.getNpcs();

        for (NPC npc : npcList) {
            for (int id : ids) {
                if (npc.getId() == id) {
                        foundNpcs.add(npc);
                }
            }
        }

        return foundNpcs;
    }

    @Nullable
    public NPC findNearestNpcIndex(int index, int... ids) {
        if(!validChecks()) return null;

        List<NPC> npcs = findNpcsWithIds(ids);

        for (NPC npc : npcs) {
            if (npc.getIndex() == index) {
                return npc;
            }
        }
        return null;
    }

    private boolean validChecks(){
        if (client.getLocalPlayer() == null || !client.isClientThread()) {
            return false;
        }

        return true;
    }

    public NPC findNearestNpcWithin(WorldPoint worldPoint, int dist, Collection<Integer> ids) {
        List<NPC> matchingNpcs = client.getNpcs().stream()
                .filter(npc -> ids.contains(npc.getId()))
                .filter(npc -> worldPoint.distanceTo(npc.getWorldLocation()) <= dist)
                .collect(Collectors.toList());

        return findNearestNpc(matchingNpcs, worldPoint);
    }

    @Nullable
    public NPC findNearestAttackableNpcWithin(WorldPoint worldPoint, int dist, String name, boolean exactNpcName) {
        assert client.isClientThread();
        List<NPC> npcList = client.getNpcs();

        if (client.getLocalPlayer() == null) {
            return null;
        }
        List<String> npcNames = Arrays.asList(name.split(","));
        for (String npcName : npcNames) {
            NPC nearestNpc = npcList.stream()
                    .filter(npc -> npc.getName() != null && isNpcAttackable(npc) && isNpcNameMatch(npc, npcName, exactNpcName) && isNpcWithinDistance(npc, worldPoint, dist))
                    .min(Comparator.comparingInt(npc -> calculateDistance(npc.getWorldLocation(), worldPoint)))
                    .orElse(null);
            if (nearestNpc != null) {
                return nearestNpc;
            }
        }

        return null;
    }


    private boolean isNpcAttackable(NPC npc) {
        return npc.getInteracting() == null && npc.getHealthRatio() != 0;
    }

    private boolean isNpcNameMatch(NPC npc, String npcName, boolean exactNpcName) {
        if (exactNpcName) {
            return npc.getName().equalsIgnoreCase(npcName);
        } else {
            return npc.getName().toLowerCase().contains(npcName.toLowerCase());
        }
    }

    private boolean isNpcWithinDistance(NPC npc, WorldPoint point, int dist) {
        return calculateDistance(npc.getWorldLocation(), point) <= dist;
    }

    @Nullable
    public NPC findNearestNpcTargetingLocal(String name, boolean exactNpcName) {
        if(!validChecks()) return null;

        List<String> npcNames = Arrays.asList(name.split(","));
        for (String npcName : npcNames) {
            NPC nearestNpc = client.getNpcs().stream()
                    .filter(npc -> npc.getName() != null && isNpcTargetingLocalPlayer(npc) && isNpcNameMatch(npc, npcName, exactNpcName))
                    .min(Comparator.comparingInt(npc -> calculateDistance(npc.getWorldLocation(), client.getLocalPlayer().getWorldLocation())))
                    .orElse(null);
            if (nearestNpc != null) {
                return nearestNpc;
            }
        }

        return null;
    }

    private boolean isNpcTargetingLocalPlayer(NPC npc) {
        return npc.getInteracting() == client.getLocalPlayer() && npc.getHealthRatio() != 0;
    }

    public List<NPC> getNPCs(int... ids) {
        if(!validChecks()) return null;

        return client.getNpcs().stream()
                .filter(npc -> Arrays.stream(ids).anyMatch(id -> npc.getId() == id))
                .collect(Collectors.toList());
    }

    public List<NPC> getNPCs(String... names) {
        if(!validChecks()) return null;

        return client.getNpcs().stream()
                .filter(npc -> Arrays.stream(names).anyMatch(name -> npc.getName() == name))
                .collect(Collectors.toList());
    }

    public NPC getFirstNPCWithLocalTarget() {
        assert client.isClientThread();

        List<NPC> npcs = client.getNpcs();
        for (NPC npc : npcs) {
            if (npc.getInteracting() == client.getLocalPlayer()) {
                return npc;
            }
        }
        return null;
    }
}
