package net.runelite.client.plugins._Viheiser.viUtilities.api.interactions;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.mappings.FieldNameMapping;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.reflection.ReflectionManager;

import javax.inject.Inject;

public class ItemInteractions {
    @Inject
    public Client client;
    @Inject
    public ReflectionManager reflectionManager;

    public void useItemOnItem(Widget firstItem, Widget secondItem){
        setSelectedInventoryItem(firstItem);
    }

    private void setSelectedInventoryItem(Widget item) {
        reflectionManager.setFieldValue(FieldNameMapping.SELECTED_SPELL_WIDGET, WidgetInfo.INVENTORY.getId(), client);
        reflectionManager.setFieldValue(FieldNameMapping.SELECTED_SPELL_CHILD_INDEX, item.getIndex(), client);
        reflectionManager.setFieldValue(FieldNameMapping.SELECTED_SPELL_ITEM_ID, item.getItemId(), client);
    }
}
