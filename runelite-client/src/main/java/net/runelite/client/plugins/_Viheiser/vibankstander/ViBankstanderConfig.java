package net.runelite.client.plugins._Viheiser.vibankstander;

import net.runelite.client.config.*;
import net.runelite.client.plugins._Viheiser.vibankstander.enums.BankType;
import net.runelite.client.plugins._Viheiser.vibankstander.enums.ToolType;

@ConfigGroup("ViBankstanderConfig")
public interface ViBankstanderConfig extends Config {

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
    @ConfigItem(keyName = "CustomToolId", name = "Custom Tool Id", description = "The Id of the custom tool you want to use", position = 5, section = interactionSection)
    default ToolType toolType(){return ToolType.KNIFE;}
    @ConfigItem(keyName = "CustomToolId", name = "Custom Tool Id", description = "The Id of the custom tool you want to use", position = 5, section = interactionSection)
    default int toolId(){return 0;}
    @ConfigItem(keyName = "itemIds", name = "itemIds", description = "Item ids of the items you wanna use your tool on (logs, gems etc)", position = 10, section = interactionSection)
    default String itemIds(){return "1517,1519";}
    @ConfigItem(keyName = "useSpecificTiles", name = "Use Specific Tiles", description = "Specific tiles to stand on before starting to interact. (before firemaking etc)", position = 20, section = interactionSection)
    default boolean useSpecificTiles() {return false;}
    @ConfigItem(keyName = "tiles", name = "Specific Tile", description = "The x and y of the tiles you wanna use. Sperate with |", position = 30, section = interactionSection)
    default String specificTile() {return "";}
    @ConfigSection(
            name = "Banking",
            description = "Settings for banking behaviour",
            position = 30
    )
    String bankingSection = "Banking";
    @ConfigItem(keyName = "bankType", name = "Bank Type", description = "What type of bank is it? npc or gameobject?", position = 0, section = interactionSection)
    default BankType bankType(){return BankType.GAME_OBJECT;}
    @ConfigItem(keyName = "bankId", name = "Bank Id", description = "Id of the bank object/npc", position = 5, section = interactionSection)
    default int bankId(){return 0;}
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
