package net.runelite.client.plugins._Viheiser.viOneClickFishing;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins._Viheiser.viOneClickFishing.enums.BankSpot;
import net.runelite.client.plugins._Viheiser.viOneClickFishing.enums.Fish;
import net.runelite.client.plugins._Viheiser.viOneClickFishing.enums.Method;

@ConfigGroup("OneClickFishingConfig")
public interface viOneClickFishingConfig extends Config {
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
            name = "banking",
            description = "Banking",
            position = 2
    )
    String bankingSection = "banking";

    @ConfigItem(
            keyName = "stopMisclick",
            name = "Stop Misclick",
            description = "Allows you to spam click left click",
            position = 100,
            section = generalSection
    )
    default boolean stopMisclick() {
        return false;
    }

    @ConfigItem(
            keyName = "startScript",
            name = "Start Script",
            description = "Allows you to spam click left click",
            position = 99,
            section = generalSection
    )
    default boolean startScript() {
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
            keyName = "methodType",
            name = "Method Type",
            description = "Method ",
            position = 0,
            section = bankingSection
    )
    default Method methodType() {
        return Method.DROP;
    }

    @ConfigItem(
            position = 2,
            keyName = "customDrop",
            name = "Change Drop Order",
            description = "Enable this to use the custom drop order",
            section = bankingSection,
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
            section = bankingSection,
            hidden = true
    )
    default String dropOrder()
    {
        return "1,2,5,6,9,10,13,14,17,18,21,22,25,26,3,4,7,8,11,12,15,16,19,20,23,24,27,28";
    }

    @ConfigItem(
            position = 4,
            keyName = "bankSpot",
            name = "Bank Spot",
            description = "Select what bank spot you're gonna use.",
            section = bankingSection,
            hidden = true
    )
    default BankSpot bankSpot()
    {
        return BankSpot.FISHING_GUILD;
    }
}