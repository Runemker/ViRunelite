package net.runelite.client.plugins.walkincircles;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("WalkInCircles")
public interface WalkInCirclesConfig extends Config
{
	@ConfigItem(
			position = 0,
			keyName = "startWalking",
			name = "Start Walking",
			description = "start Walking in circles"
	)
	default boolean startWalking()
	{
		return false;
	}
}
