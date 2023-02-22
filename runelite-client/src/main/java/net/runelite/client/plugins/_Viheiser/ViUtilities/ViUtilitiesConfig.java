package net.runelite.client.plugins._Viheiser.ViUtilities;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.enums.MouseType;

@ConfigGroup("viUtilities")
public interface ViUtilitiesConfig extends Config
{
    @ConfigItem(
            keyName = "getMouse",
            name = "Mouse",
            description = "Choose a mouse movement style",
            position = 15
    )
    default MouseType getMouse() {
        return MouseType.NO_MOVE;
    }
}
