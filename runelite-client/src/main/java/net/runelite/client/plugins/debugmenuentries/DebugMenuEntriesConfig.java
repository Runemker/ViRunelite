package net.runelite.client.plugins.debugmenuentries;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("DebugMenuEntries")
public interface DebugMenuEntriesConfig extends Config
{
	@ConfigItem(
			position = 0,
			keyName = "sendToChat",
			name = "Send to Chat",
			description = "Will send debugs to chat instead of system.out"
	)
	default boolean sendToChat()
	{
		return false;
	}
}
