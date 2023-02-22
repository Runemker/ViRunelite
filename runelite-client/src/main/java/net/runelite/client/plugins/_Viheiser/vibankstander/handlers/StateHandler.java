package net.runelite.client.plugins._Viheiser.vibankstander.handlers;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins._Viheiser.ViUtilities.ViUtilitiesPlugin;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.entities.InventoryUtils;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.entities.NpcUtils;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.entities.ObjectUtils;
import net.runelite.client.plugins._Viheiser.vibankstander.ViBankstanderConfig;
import net.runelite.client.plugins._Viheiser.vibankstander.ViBankstanderPlugin;
import net.runelite.client.plugins._Viheiser.vibankstander.enums.ViBankstanderState;

import javax.inject.Inject;

@PluginDependency(ViUtilitiesPlugin.class)
public class StateHandler {
    @Inject
    private Client client;
    @Inject
    private NpcUtils npcUtils;
    @Inject
    private ObjectUtils objectUtils;
    @Inject
    private ViBankstanderPlugin plugin;
    @Inject
    private ViUtilitiesPlugin viUtilities;
    @Inject
    private ViBankstanderConfig config;
    @Inject
    private InventoryUtils inventoryUtils;

    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged event){
        if(event.getContainerId() != WidgetInfo.INVENTORY.getId()) return;

        if(!inventoryUtils.containsItem(plugin.getItemIds()) && plugin.getState().equals(ViBankstanderState.INTERACTING)) {
            plugin.setState(ViBankstanderState.BANKING);
        }
        if(inventoryUtils.containsItem(plugin.getItemIds()) && plugin.getState().equals(ViBankstanderState.BANKING)){
            plugin.setState(ViBankstanderState.INTERACTING);
        }

    }


}
