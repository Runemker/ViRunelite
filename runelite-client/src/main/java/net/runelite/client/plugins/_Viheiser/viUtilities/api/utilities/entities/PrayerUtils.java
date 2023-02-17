package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities;

import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.enums.ExtendedPrayer;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.MenuEntryInteraction;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.WidgetMenuEntries;

import javax.inject.Inject;

public class PrayerUtils {
    @Inject
    private Client client;
    @Inject
    private MenuEntryInteraction menuEntryInteraction;
    @Inject
    private WidgetMenuEntries widgetMenuEntries;

    public int getPoints() {
        return client.getBoostedSkillLevel(Skill.PRAYER);
    }

    public boolean isActive(Prayer prayer) {
        return client.getVarbitValue(prayer.getVarbit()) == 1;
    }

    public void toggle(ExtendedPrayer prayer) {
        Widget widget = client.getWidget(prayer.getWidgetInfo());
        menuEntryInteraction.invokeMenuAction(widgetMenuEntries.createEnablePrayer(widget));
    }

    public ExtendedPrayer getActivePrayer(){
        for (ExtendedPrayer prayer : ExtendedPrayer.values()) {
            if(client.getVarbitValue(prayer.getPrayer().getVarbit()) == 1)
                return prayer;
        }

        return null;
    }

    public boolean isQuickPrayerActive() {
        return client.getVarbitValue(Varbits.QUICK_PRAYER) == 1;
    }

    public void toggleQuickPrayer() {
        Widget widget = client.getWidget(WidgetInfo.MINIMAP_QUICK_PRAYER_ORB);
        if (widget != null) {
            menuEntryInteraction.invokeMenuAction(widgetMenuEntries.createTogglePrayer());
        }
    }
}
