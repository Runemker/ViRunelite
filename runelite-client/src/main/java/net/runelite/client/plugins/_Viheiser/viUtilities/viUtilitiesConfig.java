package net.runelite.client.plugins._Viheiser.viUtilities;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins._Viheiser.viUtilities.api.enums.MouseType;

@ConfigGroup("viUtilities")
public interface viUtilitiesConfig extends Config
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
