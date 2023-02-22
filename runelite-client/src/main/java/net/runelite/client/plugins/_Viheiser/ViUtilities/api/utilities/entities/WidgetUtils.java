package net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.entities;

import net.runelite.api.widgets.Widget;

import javax.inject.Singleton;

@Singleton
public class WidgetUtils {
    public boolean widgetIsVisible(Widget widget) {
        return widget != null && !widget.isHidden();
    }
}
