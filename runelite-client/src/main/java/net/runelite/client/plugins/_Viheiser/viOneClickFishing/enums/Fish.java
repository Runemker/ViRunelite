package net.runelite.client.plugins._Viheiser.viOneClickFishing.enums;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public enum Fish {
    SMALL_NET(ImmutableSet.of(0), ImmutableSet.of(""), ImmutableSet.of(317, 321)),
    BAIT(ImmutableSet.of(0), ImmutableSet.of(""), ImmutableSet.of(0)),
    FLY_FISHING(ImmutableSet.of(1526, 1515), ImmutableSet.of("Rod Fishing spot"), ImmutableSet.of(331, 335)),
    HARPOON_FISHING(ImmutableSet.of(0), ImmutableSet.of(""), ImmutableSet.of(371, 383)),
    VESSEL_FISHING(ImmutableSet.of(0), ImmutableSet.of(""), ImmutableSet.of(3142)),
    BARBARIAN_FISHING(ImmutableSet.of(1542), ImmutableSet.of(""), ImmutableSet.of(11330, 11328, 11332)),
    MINNOWS(ImmutableSet.of(0), ImmutableSet.of(""), ImmutableSet.of(0));

    Set<Integer> npcId;
    Set<Integer> itemId;
    Set<String> npcName;

    public Set<Integer> getNpcId() {
        return npcId;
    }

    public Set<Integer> getItemId() {
        return itemId;
    }

    public Set<String> getNpcName() {
        return npcName;
    }

    Fish(Set<Integer> npcId, Set<String> npcName, Set<Integer> itemId) {
        this.npcName = npcName;
        this.npcId = npcId;
        this.itemId = itemId;
    }
}
