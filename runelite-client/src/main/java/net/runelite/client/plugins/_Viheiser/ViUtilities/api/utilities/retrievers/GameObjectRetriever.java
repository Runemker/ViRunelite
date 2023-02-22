package net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.retrievers;

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GameObjectRetriever {
    @Inject
    private Client client;

    public GameObject getNearestGameObjectWithIds(int... gameObjectIds) {
        GameObject nearestGameObject = findNearestGameObjectWithIds(gameObjectIds);

        return nearestGameObject;
    }

    private Collection<GameObject> getGameObjects()
    {
        Collection<GameObject> objects = new ArrayList<>();
        for (Tile tile : getTiles())
        {
            GameObject[] gameObjects = tile.getGameObjects();
            if (gameObjects != null)
            {
                objects.addAll(Arrays.asList(gameObjects));
            }
        }
        return objects;
    }

    protected List<Tile> getTiles()
    {
        List<Tile> tilesList = new ArrayList<>();
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = client.getPlane();
        for (int x = 0; x < Constants.SCENE_SIZE; ++x)
        {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y)
            {
                Tile tile = tiles[z][x][y];
                if (tile == null)
                {
                    continue;
                }
                tilesList.add(tile);
            }
        }
        return tilesList;
    }

    private int calculateDistance(WorldPoint point1, WorldPoint point2) {
        return point1.distanceTo(point2);
    }

    private GameObject findNearestGameObjectWithIds(int... gameObjectIds) {
        GameObject nearestGameObject = null;
        int nearestDistance = Integer.MAX_VALUE;
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        Collection<GameObject> gameObjectList = getGameObjects();

        for (GameObject gameObject : gameObjectList) {
            for (int gameObjectId : gameObjectIds) {
                if (gameObject.getId() == gameObjectId) {
                    int distance = calculateDistance(playerLocation, gameObject.getWorldLocation());
                    if (distance < nearestDistance) {
                        nearestGameObject = gameObject;
                        nearestDistance = distance;
                    }
                }
            }
        }

        return nearestGameObject;
    }
}

