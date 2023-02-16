package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import javax.inject.Inject;

public class DialogUtils {
    @Inject
    private Client client;
    @Inject
    private WidgetUtils widgetUtils;

    public boolean levelUpMessageIsVisible() {
        Widget levelUpMessage = client.getWidget(WidgetInfo.LEVEL_UP);
        Widget dialogSprite = client.getWidget(WidgetInfo.DIALOG_SPRITE);
        return widgetUtils.widgetIsVisible(levelUpMessage) || widgetUtils.widgetIsVisible(dialogSprite);
    }
}
