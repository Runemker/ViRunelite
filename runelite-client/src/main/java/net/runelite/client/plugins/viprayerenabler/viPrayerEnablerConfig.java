package net.runelite.client.plugins.viprayerenabler;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("viPrayerEnabler")
public interface viPrayerEnablerConfig extends Config
{
	@ConfigItem(
			position = 0,
			keyName = "enablePrayerFlick",
			name = "enable Prayer Flick",
			description = "This is the minimum amount before the plugin will let you run on"
	)
	default boolean enablePrayerFlick()
	{
		return false;
	}
}
