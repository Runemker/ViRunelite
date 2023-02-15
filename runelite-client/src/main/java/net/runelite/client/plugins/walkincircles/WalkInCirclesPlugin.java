package net.runelite.client.plugins.walkincircles;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.viUtilities.api.interactions.WalkInteractions;
import net.runelite.client.plugins.viUtilities.api.common.ChatMessageHandler;
import net.runelite.client.plugins.viUtilities.api.extensions.objects.viPlayer;

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
	private viPlayer viPlayer;
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
			if(viPlayer.isMoving())
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
