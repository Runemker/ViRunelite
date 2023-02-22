package net.runelite.client.plugins._Viheiser.viskiller;

import net.runelite.client.config.*;
import net.runelite.client.plugins._Viheiser.viskiller.enums.DropBehaviour;
import net.runelite.client.plugins._Viheiser.viskiller.enums.InteractionType;

@ConfigGroup("ViSkillerConfig")
public interface ViSkillerConfig extends Config {

    @ConfigSection(
            name = "General",
            description = "General Settings",
            position = 0
    )
    String generalSection = "General";
    @ConfigItem(keyName = "toggle", name = "Toggle", description = "Key to toggle", position = 0, section = generalSection)
    default Keybind toggle()
    {
        return Keybind.NOT_SET;
    }
    @ConfigItem(keyName = "useInvokes", name = "use Invokes", description = "uses invokes instead", position = 1, section = generalSection)
    default boolean useInvokes() {return false;}
    @ConfigItem(keyName = "oneClick", name = "One Click", description = "Use One Click instead of Automatic", position = 2, section = generalSection)
    default boolean oneClick() {return false;}
    @ConfigItem(keyName = "showOverlay", name = "Show UI", description = "Show the UI on screen", position = 2, section = generalSection)
    default boolean showOverlay() {return true;}
    @ConfigSection(
            name = "Interaction",
            description = "Settings for interacting etc",
            position = 10
    )
    String interactionSection = "Interaction";
    @ConfigItem(keyName = "interactionType", name = "Interacting with", description = "Select what you want to interact with.", position = 0, section = interactionSection)
    default InteractionType interactionType(){return InteractionType.OBJECT;}
    @ConfigItem(keyName = "interactionIds", name = "Ids", description = "Ids of the thing you wanna interact with separate with comma.", position = 5, section = interactionSection)
    default String interactionIds(){return "";}
    @Range(min = 1, max = 60)
    @ConfigItem(keyName = "locationRadius", name = "Location Radius", description = "Radius to search for the interaction type.", position = 10, section = interactionSection)
    default int locationRadius() {return 10;}
    @ConfigItem(keyName = "drawLocationRadius", name = "Draw Location Radius", description = "Draw location Radius on screen.", position = 15, section = interactionSection)
    default boolean drawLocationRadius() {return false;}
    @ConfigItem(keyName = "specificTile", name = "Specific Tile", description = "Specific tile to stand on before starting to interact", position = 20, section = interactionSection)
    default boolean specificTile() {return false;}

    @ConfigSection(
            name = "Dropping",
            description = "Settings for dropping behaviour",
            position = 20
    )
    String droppingSection = "Dropping";
    @ConfigItem(keyName = "dropItems", name = "Drop Items", description = "Should drop items.", position = 0, section = droppingSection)
    default boolean dropItems() {return false;}
    @ConfigItem(keyName = "itemIdsToDrop", name = "Item Ids", description = "Ids of the item to drop.", position = 5, section = droppingSection)
    default String itemIdsToDrop(){return "";}
    @ConfigItem(keyName = "dropBehaviour", name = "Drop Behaviour", description = "Behaviour for dropping.", position = 10, section = droppingSection)
    default DropBehaviour dropBehaviour(){return DropBehaviour.AT_FULL_INVENTORY;}
    @ConfigSection(
            name = "Banking",
            description = "Settings for banking behaviour",
            position = 30
    )
    String bankingSection = "Banking";
    @ConfigSection(
            name = "Sleep Delay",
            description = "Sleep Delay",
            position = 95,
            closedByDefault = true
    )
    String sleepDelaySection = "Sleep Delay";
    @ConfigItem(keyName = "sleepMinDelay", name = "Delay Min", description = "", position = 3, section = sleepDelaySection)
    default int sleepMinDelay()
    {
        return 120;
    }
    @ConfigItem(keyName = "sleepMaxDelay", name = "Delay Max", description = "", position = 4, section = sleepDelaySection)
    default int sleepMaxDelay()
    {
        return 240;
    }
    @ConfigItem(keyName = "sleepTarget", name = "Delay Target", description = "", position = 5, section = sleepDelaySection)
    default int sleepTarget()
    {
        return 180;
    }
    @ConfigItem(keyName = "sleepDeviation", name = "Delay Deviation", description = "", position = 6, section = sleepDelaySection)
    default int sleepDeviation()
    {
        return 10;
    }
    @ConfigItem(keyName = "sleepWeightedDistribution", name = "Weighted Distribution", description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution", position = 7, section = sleepDelaySection)
    default boolean sleepWeightedDistribution()
    {
        return false;
    }
    @ConfigSection(
            name = "Tick Delay",
            description = "Tick Delays",
            position = 98,
            closedByDefault = true
    )
    String tickDelays = "Tick Delay";
    @Range(max = 10)
    @ConfigItem(keyName = "tickDelaysMin", name = "Game Tick Min", description = "", position = 8, section = tickDelays)
    default int tickDelaysMin() {
        return 1;
    }
    @Range(max = 10)
    @ConfigItem(keyName = "tickDelaysMax", name = "Game Tick Max", description = "", position = 9, section = tickDelays)
    default int tickDelaysMax() {
        return 3;
    }
    @Range(max = 10)
    @ConfigItem(keyName = "tickDelaysTarget", name = "Game Tick Target", description = "", position = 10, section = tickDelays)
    default int tickDelaysTarget() {
        return 2;
    }
    @Range(max = 10)
    @ConfigItem(keyName = "tickDelaysDeviation", name = "Game Tick Deviation", description = "", position = 11, section = tickDelays)
    default int tickDelaysDeviation() {
        return 1;
    }
    @ConfigItem(keyName = "tickDelaysWeightedDistribution", name = "Game Tick Weighted Distribution", description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution", position = 12, section = tickDelays)
    default boolean tickDelaysWeightedDistribution() {
        return false;
    }
}
