package net.runelite.client.plugins._Viheiser.vibankstander.enums;

import lombok.Getter;
import net.runelite.api.ItemID;

public enum ToolType {
    KNIFE(ItemID.KNIFE),
    GLASSBLOWING_PIPE(ItemID.GLASSBLOWING_PIPE),
    CHISEL(ItemID.CHISEL),
    TINDERBOX(ItemID.TINDERBOX),
    NEEDLE(ItemID.NEEDLE),
    CUSTOM(-1);

    ToolType(int itemId) {
        this.itemId = itemId;
    }
    @Getter
    private int itemId;
}
