package net.runelite.client.plugins._Viheiser.ViFisher;

import net.runelite.client.config.*;
import net.runelite.client.plugins._Viheiser.ViFisher.enums.Fish;

@ConfigGroup("OneClickFishingConfig")
public interface ViFisherConfig extends Config {
    @ConfigSection(
            name = "general",
            description = "General",
            position = 0
    )
    String generalSection = "general";

    @ConfigSection(
            name = "fishing",
            description = "Fishing",
            position = 1
    )
    String fishingSection = "fishing";

    @ConfigSection(
            name = "dropping",
            description = "Dropping",
            position = 2
    )
    String droppingSection = "dropping";

    @ConfigSection(
            name = "sleepDelay",
            description = "Sleep Delay",
            position = 95,
            closedByDefault = true
    )
    String sleepDelaySection = "sleepDelay";

    @ConfigSection(
            name = "tickDelays",
            description = "Tick Delays",
            position = 98,
            closedByDefault = true
    )
    String tickDelays = "Tick Delays";

    @ConfigItem(
            keyName = "toggle",
            name = "Toggle",
            description = "Key to toggle",
            position = 4,
            section = generalSection
    )
    default Keybind toggle()
    {
        return Keybind.NOT_SET;
    }
    @ConfigItem(
            keyName = "useInvokes",
            name = "use Invokes",
            description = "uses invokes instead",
            position = 5,
            section = generalSection
    )
    default boolean useInvokes()
    {
        return false;
    }
    @ConfigItem(
            keyName = "fishType",
            name = "Fish Method",
            description = "What fishing method are you using",
            position = 0,
            section = fishingSection
    )
    default Fish fishType() {
        return Fish.SMALL_NET;
    }

    @ConfigItem(
            position = 2,
            keyName = "customDrop",
            name = "Change Drop Order",
            description = "Enable this to use the custom drop order",
            section = droppingSection,
            hidden = true
    )
    default boolean customDrop()
    {
        return false;
    }

    @ConfigItem(
            position = 3,
            keyName = "dropOrder",
            name = "Drop Order",
            description = "The order to drop items in. 1 is the top left, 28 is the bottom right",
            section = droppingSection,
            hidden = true
    )
    default String dropOrder()
    {
        return "1,2,5,6,9,10,13,14,17,18,21,22,25,26,3,4,7,8,11,12,15,16,19,20,23,24,27,28";
    }

    @ConfigItem(
            keyName = "minDelay",
            name = "Delay Min",
            description = "",
            position = 3,
            section = sleepDelaySection
    )
    default int minDelay()
    {
        return 120;
    }

    @ConfigItem(
            keyName = "maxDelay",
            name = "Delay Max",
            description = "",
            position = 4,
            section = sleepDelaySection
    )
    default int maxDelay()
    {
        return 240;
    }

    @ConfigItem(
            keyName = "target",
            name = "Delay Target",
            description = "",
            position = 5,
            section = sleepDelaySection
    )
    default int target()
    {
        return 180;
    }

    @ConfigItem(
            keyName = "deviation",
            name = "Delay Deviation",
            description = "",
            position = 6,
            section = sleepDelaySection
    )
    default int deviation()
    {
        return 10;
    }

    @ConfigItem(
            keyName = "weightedDistribution",
            name = "Weighted Distribution",
            description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
            position = 7,
            section = sleepDelaySection
    )
    default boolean weightedDistribution()
    {
        return false;
    }

    @Range(
            min = 0,
            max = 10
    )
    @ConfigItem(
            keyName = "tickDelaysMin",
            name = "Game Tick Min",
            description = "",
            position = 8,
            section = tickDelays
    )
    default int tickDelaysMin() {
        return 1;
    }

    @Range(
            min = 0,
            max = 10
    )
    @ConfigItem(
            keyName = "tickDelaysMax",
            name = "Game Tick Max",
            description = "",
            position = 9,
            section = tickDelays
    )
    default int tickDelaysMax() {
        return 3;
    }

    @Range(
            min = 0,
            max = 10
    )
    @ConfigItem(
            keyName = "tickDelaysTarget",
            name = "Game Tick Target",
            description = "",
            position = 10,
            section = tickDelays
    )
    default int tickDelaysTarget() {
        return 2;
    }

    @Range(
            min = 0,
            max = 10
    )
    @ConfigItem(
            keyName = "tickDelaysDeviation",
            name = "Game Tick Deviation",
            description = "",
            position = 11,
            section = tickDelays
    )
    default int tickDelaysDeviation() {
        return 1;
    }

    @ConfigItem(
            keyName = "tickDelaysWeightedDistribution",
            name = "Game Tick Weighted Distribution",
            description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
            position = 12,
            section = tickDelays
    )
    default boolean tickDelaysWeightedDistribution() {
        return false;
    }
}