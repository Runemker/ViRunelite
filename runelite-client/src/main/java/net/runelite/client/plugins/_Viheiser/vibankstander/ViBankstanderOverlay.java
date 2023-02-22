package net.runelite.client.plugins._Viheiser.vibankstander;

import net.runelite.api.MenuAction;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@PluginDependency(ViBankstanderPlugin.class)
public class ViBankstanderOverlay extends OverlayPanel {
    private final ViBankstanderPlugin plugin;
    private final ViBankstanderConfig config;

    @Inject
    public ViBankstanderOverlay(ViBankstanderPlugin plugin, ViBankstanderConfig config) {
        super(plugin);
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        this.plugin = plugin;
        this.config = config;
        getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "ViThieving Overlay"));
    }

    public Dimension render(final Graphics2D graphics) {
        if(!config.showOverlay())
            return null;
        panelComponent.getChildren().clear();

        /* Setup */
        final String title = "ViBankstander v0.1";
        Duration duration = Duration.between(plugin.botTimer, Instant.now());
        panelComponent.getChildren().add(TitleComponent.builder().text(title).color(Color.YELLOW).build());
        panelComponent.setBackgroundColor(Color.DARK_GRAY);
        panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(title) + 120, 0));

        /* Content */
        panelComponent.getChildren().add(LineComponent.builder().left("").build());
        panelComponent.getChildren().add(LineComponent.builder().left("Runtime: ").right((duration.toHours() > 0 ? (Long.toString(duration.toHours()) + ":") : ("")) + (new SimpleDateFormat("mm:ss").format(new Date(duration.toMillis())))).build());
        if (plugin.getState() != null)
            panelComponent.getChildren().add(LineComponent.builder().left("State: ").right(plugin.getBotTimer().toString().toLowerCase()).build());
        //if (config.debug())
        return panelComponent.render(graphics);
    }
}
