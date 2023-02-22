package net.runelite.client.plugins._Viheiser.viskiller.handlers;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins._Viheiser.ViUtilities.ViUtilitiesPlugin;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.entities.InventoryUtils;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.entities.NpcUtils;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.entities.ObjectUtils;
import net.runelite.client.plugins._Viheiser.viskiller.ViSkillerConfig;
import net.runelite.client.plugins._Viheiser.viskiller.ViSkiller;
import net.runelite.client.plugins._Viheiser.viskiller.enums.InteractionType;
import net.runelite.client.plugins._Viheiser.viskiller.enums.ViSkillerState;

import javax.inject.Inject;

@PluginDependency(ViUtilitiesPlugin.class)
public class Interacting {
    @Inject
    private Client client;
    @Inject
    private NpcUtils npcUtils;
    @Inject
    private ObjectUtils objectUtils;
    @Inject
    private ViSkiller plugin;
    @Inject
    private ViUtilitiesPlugin viUtilities;
    @Inject
    private ViSkillerConfig config;
    @Inject
    private InventoryUtils inventoryUtils;
    @Subscribe
    private void onGameTick(GameTick event){
        if(!plugin.isStartThieving() && viUtilities.isInvalidGameState()) return;

        if(plugin.getState().equals(ViSkillerState.INTERACTING)){
            if(config.interactionType().equals(InteractionType.NPC)){
                NPC interactNpc = npcUtils.findNearestNpc(plugin.getInteractionIds());
                npcUtils.interactWithNpc(interactNpc, MenuAction.NPC_FIRST_OPTION, plugin.useMouse(), plugin.sleepDelay());
            } else {
                GameObject interactObject = objectUtils.getNearestGameObject(plugin.getInteractionIds());
                objectUtils.interactWithObject(interactObject);
            }
        }
    }

    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged event){
        if(event.getContainerId() != WidgetInfo.INVENTORY.getId()) return;

        if(!inventoryUtils.containsItem(plugin.getItemIds()) && plugin.getState().equals(ViSkillerState.DROPPING))
        {
            plugin.setState(ViSkillerState.INTERACTING);
        }

    }


}
