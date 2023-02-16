package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions;

import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.reflection.ReflectionManager;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.mappings.FieldNameMapping;

import javax.inject.Inject;
import java.util.Objects;

public class WalkInteractions {
    @Inject
    public Client client;
    @Inject
    private ReflectionManager reflectionManager;
    public void walkTo(WorldPoint worldPoint) {
        LocalPoint localPoint = getLocalPointFromWorldPoint(worldPoint);
        startWalking(localPoint.getSceneX(), localPoint.getSceneY());
    }

    private LocalPoint getLocalPointFromWorldPoint(WorldPoint worldPoint) {
        if (worldPoint.isInScene(client)) return Objects.requireNonNull(LocalPoint.fromWorld(client, worldPoint));

        return null;
    }

    private void startWalking(int sceneX, int sceneY){
        reflectionManager.setFieldValue(FieldNameMapping.SELECTED_SCENE_TILE_X, sceneX);
        reflectionManager.setFieldValue(FieldNameMapping.SELECTED_SCENE_TILE_Y, sceneY);
        reflectionManager.setFieldValue(FieldNameMapping.VIEWPORT_WALKING, true);
        reflectionManager.setFieldValue(FieldNameMapping.CHECK_CLICK, false);
    }
}
