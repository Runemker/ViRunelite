package net.runelite.client.plugins._Viheiser.walkincircles;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.ChatMessageHandler;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities.PlayerUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.WalkInteractions;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@PluginDescriptor(
	name = "WalkInCircles",
	enabledByDefault = false,
	description = "Walks in Circles",
	tags = {"viheiser"}
)
@Slf4j
public class WalkInCirclesPlugin extends Plugin
{
	@Inject
	private WalkInCirclesConfig config;
	@Inject
	public Client client;

	@Inject
	private PlayerUtils playerUtils;
	@Inject
	public WalkInteractions walkInteractions;
	@Inject
	public ChatMessageHandler chatter;
	@Inject
	private ConfigManager configManager;
	@Provides
	private WalkInCirclesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WalkInCirclesConfig.class);
	}

	List<WorldPoint> tiles = new ArrayList<>();

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Subscribe
	private void onGameTick(GameTick event)
	{
		if (client.getLocalPlayer() == null
				|| client.getGameState() != GameState.LOGGED_IN
				|| client.isMenuOpen()
				|| client.getWidget(378,78) != null)//login button
			return;

		if(config.startWalking()){
			if(playerUtils.isMoving())
				return;

			chatter.sendGameMessage("is start walking");
			WorldPoint playerPoint = client.getLocalPlayer().getWorldLocation();
			if(tiles.stream().count() == 0){
				chatter.sendGameMessage("Adding tiles");
				tiles.add(new WorldPoint(playerPoint.getX(), playerPoint.getY(), playerPoint.getPlane()));
				tiles.add(new WorldPoint(playerPoint.getX()+1, playerPoint.getY(), playerPoint.getPlane()));
				tiles.add(new WorldPoint(playerPoint.getX()+1, playerPoint.getY()-1, playerPoint.getPlane()));
				tiles.add(new WorldPoint(playerPoint.getX(), playerPoint.getY()-1, playerPoint.getPlane()));
			} else {

				if(playerPoint.getX() == tiles.get(0).getX() && playerPoint.getY() == tiles.get(0).getY()){
					chatter.sendGameMessage("walking to tile one");
					WorldPoint newTile = tiles.get(1);
					walkInteractions.walkTo(newTile);
				}
				else if(playerPoint.getX() == tiles.get(1).getX() && playerPoint.getY() == tiles.get(1).getY()){
					WorldPoint newTile = tiles.get(2);
					walkInteractions.walkTo(newTile);
				}
				else if(playerPoint.getX() == tiles.get(2).getX() && playerPoint.getY() == tiles.get(2).getY()){
					WorldPoint newTile = tiles.get(3);
					walkInteractions.walkTo(newTile);
				}
				else if(playerPoint.getX() == tiles.get(3).getX() && playerPoint.getY() == tiles.get(3).getY()) {
					WorldPoint newTile = tiles.get(0);
					walkInteractions.walkTo(newTile);
				}
			}
		}
		else {
			if(tiles.stream().count() != 0){
				tiles.clear();
			}
		}
	}
}
