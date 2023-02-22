package net.runelite.client.plugins._Viheiser.viskiller.handlers;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins._Viheiser.ViUtilities.ViUtilitiesPlugin;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.ChatMessageHandler;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.entities.InventoryUtils;
import net.runelite.client.plugins._Viheiser.viskiller.ViSkillerConfig;
import net.runelite.client.plugins._Viheiser.viskiller.ViSkiller;
import net.runelite.client.plugins._Viheiser.viskiller.enums.DropBehaviour;
import net.runelite.client.plugins._Viheiser.viskiller.enums.ViSkillerState;

import javax.inject.Inject;

@ConfigGroup("ViSkillerConfig")
public class Dropping {
    @Inject
    private Client client;
    @Inject
    private ViSkiller plugin;
    @Inject
    private ViUtilitiesPlugin viUtilities;
    @Inject
    private InventoryUtils inventoryUtils;
    @Inject
    private ChatMessageHandler chatMessageHandler;
    @Inject
    private ViSkillerConfig config;
    @Subscribe
    private void onGameTick(GameTick event){
        if(!plugin.isStartThieving() && viUtilities.isInvalidGameState()) return;

        if(config.dropBehaviour().equals(DropBehaviour.AT_FULL_INVENTORY) && plugin.getState().equals(ViSkillerState.DROPPING)){
            inventoryUtils.dropItems(plugin.getItemIds(), true, plugin.sleepDelay());
        }
    }

    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged event){
        if(event.getContainerId() != WidgetInfo.INVENTORY.getId()) return;

        if(dropAtFullInventoryIsVaild() || dropAfterOneIsValid())
        {
            plugin.setState(ViSkillerState.DROPPING);
        }

    }

    @Subscribe
    private void onInteractingChanged(InteractingChanged event){
        if(!event.getSource().equals(client.getLocalPlayer())) return;

        if(plugin.getState().equals(ViSkillerState.INTERACTING) &&
        event.getSource().getInteracting() != null &&
        inventoryUtils.containsItem(plugin.getItemIds())){
            plugin.setState(ViSkillerState.DROPPING);
        }
    }

    private boolean dropAfterOneIsValid() {
        return config.dropBehaviour().equals(DropBehaviour.DROP_AFTER_ONE)
                && inventoryUtils.containsItem(plugin.getItemIds());
    }

    private boolean dropAtFullInventoryIsVaild() {
        return config.dropBehaviour().equals(DropBehaviour.AT_FULL_INVENTORY)
                && inventoryUtils.containsItem(plugin.getItemIds());
    }
}
