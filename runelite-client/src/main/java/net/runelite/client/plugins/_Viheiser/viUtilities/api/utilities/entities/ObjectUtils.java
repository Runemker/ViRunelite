package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

import static net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.objectlists.Banks.ALL_BANKS;

@Singleton
public class ObjectUtils {
    @Inject
    private Client client;

    public GameObject getNearestGameObject(int... gameObjectIds) {
        GameObject nearestGameObject = findNearestGameObject(gameObjectIds);

        return nearestGameObject;
    }

    @Nullable
    public GameObject findNearestGameObjectWithin(WorldPoint worldPoint, int dist, int... ids) {
        if(!validChecks()) return null;
        Collection<GameObject> gameObjects = getGameObjects();

        return gameObjects.stream()
                .filter(gameObject -> Arrays.stream(ids).anyMatch(id -> id == gameObject.getId()))
                .filter(gameObject -> gameObject.getWorldLocation().distanceTo(worldPoint) <= dist)
                .min(Comparator.comparingInt(gameObject -> gameObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }


    @Nullable
    public GameObject findNearestGameObjectWithin(WorldPoint worldPoint, int dist) {
        if(!validChecks()) return null;
        Collection<GameObject> gameObjects = getGameObjects();

        return gameObjects.stream()
                .filter(gameObject -> gameObject.getWorldLocation().distanceTo(worldPoint) <= dist)
                .min(Comparator.comparingInt(gameObject -> gameObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }

    @Nullable
    public GameObject findNearestGameObjectWithin(WorldPoint worldPoint, int dist, Collection<Integer> ids) {
        if(!validChecks()) return null;
        Collection<GameObject> gameObjects = getGameObjects();

        return gameObjects.stream()
                .filter(gameObject -> ids.contains(gameObject.getId()))
                .filter(gameObject -> gameObject.getWorldLocation().distanceTo(worldPoint) <= dist)
                .min(Comparator.comparingInt(gameObject -> gameObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }

    @Nullable
    public GameObject findNearestGameObjectWithin(LocalPoint localPoint, int dist, int... ids) {
        if(!validChecks()) return null;
        WorldPoint worldPoint = WorldPoint.fromLocal(client, localPoint);

        return findNearestGameObjectWithin(worldPoint, dist, ids);
    }

    @Nullable
    public GameObject findNearestGameObjectWithin(LocalPoint localPoint, int dist, Collection<Integer> ids) {
        if(!validChecks()) return null;
        WorldPoint worldPoint = WorldPoint.fromLocal(client, localPoint);

        return findNearestGameObjectWithin(worldPoint, dist, ids);
    }

    @Nullable
    public GameObject findNearestGameObjectMenuWithin(WorldPoint worldPoint, int dist, String menuAction) {
        if(!validChecks()) return null;
        Collection<GameObject> gameObjects = getGameObjects();

        return gameObjects.stream()
                .filter(gameObject -> gameObject.getWorldLocation().distanceTo(worldPoint) <= dist)
                .filter(gameObject -> ArrayUtils.contains(client.getObjectDefinition(gameObject.getId()).getActions(), menuAction))
                .min(Comparator.comparingInt(gameObject -> gameObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }


    public List<WallObject> getWallObjects(int... ids) {
        if(!validChecks()) return null;
        Collection<WallObject> wallObjects = getWallObjects();

        return wallObjects.stream()
                .filter(wallObject -> Arrays.stream(ids).anyMatch(id -> id == wallObject.getId()))
                .collect(Collectors.toList());
    }


    public List<DecorativeObject> getDecorObjects(int... ids) {
        if(!validChecks()) return null;
        Collection<DecorativeObject> decorativeObjects = getDecorativeObjects();

        return decorativeObjects.stream()
                .filter(decorativeObject -> Arrays.stream(ids).anyMatch(id -> id == decorativeObject.getId()))
                .collect(Collectors.toList());
    }

    public List<GroundObject> getGroundObjects(int... ids) {
        if(!validChecks()) return null;
        Collection<GroundObject> groundObjects = getGroundObject();

        return groundObjects.stream()
                .filter(groundObject -> Arrays.stream(ids).anyMatch(id -> id == groundObject.getId()))
                .collect(Collectors.toList());
    }

    public TileItem getGroundItem(int id) {
        Scene scene = client.getScene();
        int plane = client.getPlane();
        Player localPlayer = client.getLocalPlayer();

        return Arrays.stream(scene.getTiles()[plane])
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .map(tile -> findItemAtTile(tile, id))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }


    private TileItem findItemAtTile(Tile tile, int id) {
        ItemLayer tileItemPile = tile.getItemLayer();
        if (tileItemPile != null) {
            TileItem tileItem = (TileItem) tileItemPile.getBottom();
            if (tileItem.getId() == id) {
                return tileItem;
            }
        }
        return null;
    }

    @Nullable
    public TileObject findNearestObject(int... ids) {
        List<TileObject> objects = new ArrayList<>();
        GameObject gameObject = findNearestGameObject(ids);
        WallObject wallObject = findNearestWallObject(ids);
        DecorativeObject decorativeObject = findNearestDecorObject(ids);
        TileObject groundObject = findNearestGroundObject(ids);

        if (gameObject != null) {
            objects.add(gameObject);
        }
        if (wallObject != null) {
            objects.add(wallObject);
        }
        if (decorativeObject != null) {
            objects.add(decorativeObject);
        }
        if (groundObject != null) {
            objects.add(groundObject);
        }

        return objects.stream()
                .min(Comparator.comparingInt(tileObject -> calculateDistance(tileObject.getWorldLocation(), client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }


    @Nullable
    public TileObject findNearestObjectWithin(WorldPoint worldPoint, int dist, int... ids) {
        GameObject gameObject = findNearestGameObjectWithin(worldPoint, dist, ids);

        if (gameObject != null) {
            return gameObject;
        }

        WallObject wallObject = findWallObjectWithin(worldPoint, dist, ids);

        if (wallObject != null) {
            return wallObject;
        }
        DecorativeObject decorativeObject = findNearestDecorObjectWithin(worldPoint, dist, ids);

        if (decorativeObject != null) {
            return decorativeObject;
        }

        return findNearestGroundObjectWithin(worldPoint, dist, ids);
    }

    @Nullable
    public TileObject findNearestObjectWithin(WorldPoint worldPoint, int dist) {
        GameObject gameObject = findNearestGameObjectWithin(worldPoint, dist);

        if (gameObject != null) {
            return gameObject;
        }

        WallObject wallObject = findWallObjectWithin(worldPoint, dist);

        if (wallObject != null) {
            return wallObject;
        }
        DecorativeObject decorativeObject = findNearestDecorObjectWithin(worldPoint, dist);

        if (decorativeObject != null) {
            return decorativeObject;
        }

        return findNearestGroundObjectWithin(worldPoint, dist);
    }

    @Nullable
    public TileObject findNearestObjectMenuWithin(WorldPoint worldPoint, int dist, String menuAction) {
        GameObject gameObject = findNearestGameObjectMenuWithin(worldPoint, dist, menuAction);

        if (gameObject != null) {
            return gameObject;
        }

        WallObject wallObject = findNearestWallObjectMenuWithin(worldPoint, dist, menuAction);

        if (wallObject != null) {
            return wallObject;
        }
        DecorativeObject decorativeObject = findNearestDecorObjectMenuWithin(worldPoint, dist, menuAction);

        if (decorativeObject != null) {
            return decorativeObject;
        }

        return findNearestGroundObjectMenuWithin(worldPoint, dist, menuAction);
    }

    @Nullable
    public WallObject findNearestWallObject(int... ids) {
        if (!validChecks()) return null;
        Collection<WallObject> wallObjects = getWallObjects();

        return wallObjects.stream()
                .filter(wallObject -> Arrays.stream(ids).anyMatch(id -> id == wallObject.getId()))
                .min(Comparator.comparingInt(wallObject -> calculateDistance(wallObject.getWorldLocation(), client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }


    @Nullable
    public WallObject findWallObjectWithin(WorldPoint worldPoint, int radius, int... ids) {
        if (!validChecks()) return null;
        Collection<WallObject> wallObjects = getWallObjects();

        return wallObjects.stream()
                .filter(wallObject -> Arrays.stream(ids).anyMatch(id -> id == wallObject.getId()))
                .filter(wallObject -> wallObject.getWorldLocation().distanceTo(worldPoint) <= radius)
                .min(Comparator.comparingInt(wallObject -> wallObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }


    @Nullable
    public WallObject findWallObjectWithin(WorldPoint worldPoint, int radius) {
        if (!validChecks()) return null;
        Collection<WallObject> wallObjects = getWallObjects();

        return wallObjects.stream()
                .filter(wallObject -> wallObject.getWorldLocation().distanceTo(worldPoint) <= radius)
                .min(Comparator.comparingInt(wallObject -> wallObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }


    @Nullable
    public WallObject findWallObjectWithin(WorldPoint worldPoint, int radius, Collection<Integer> ids) {
        if (!validChecks()) return null;
        Collection<WallObject> wallObjects = getWallObjects();

        return wallObjects.stream()
                .filter(wallObject -> ids.contains(wallObject.getId()))
                .filter(wallObject -> wallObject.getWorldLocation().distanceTo(worldPoint) <= radius)
                .min(Comparator.comparingInt(wallObject -> wallObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }


    @Nullable
    public WallObject findNearestWallObjectMenuWithin(WorldPoint worldPoint, int dist, String menuAction) {
        if (!validChecks()) return null;
        Collection<WallObject> wallObjects = getWallObjects();

        return wallObjects.stream()
                .filter(wallObject -> wallObject.getWorldLocation().distanceTo(worldPoint) <= dist)
                .filter(wallObject -> ArrayUtils.contains(client.getObjectDefinition(wallObject.getId()).getActions(), menuAction))
                .min(Comparator.comparingInt(wallObject -> wallObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }


    public WallObject getNearestWallObjectInArea(WorldArea area, int... ids) {
        if (!validChecks()) return null;
        Collection<WallObject> wallObjects = getWallObjects();

        return wallObjects.stream()
                .filter(wallObject -> Arrays.stream(ids).anyMatch(id -> id == wallObject.getId()))
                .filter(wallObject -> wallObject.getWorldLocation().toWorldArea().intersectsWith(area))
                .min(Comparator.comparingInt(wallObject -> calculateDistance(wallObject.getWorldLocation(), client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }


    public GameObject getNearestGameObjectInArea(WorldArea area, int... ids) {
        if (!validChecks()) return null;
        Collection<GameObject> gameObjects = getGameObjects();

        return gameObjects.stream()
                .filter(gameObject -> Arrays.stream(ids).anyMatch(id -> id == gameObject.getId()))
                .filter(gameObject -> gameObject.getWorldLocation().toWorldArea().intersectsWith(area))
                .min(Comparator.comparingInt(gameObject -> calculateDistance(gameObject.getWorldLocation(), client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }


    @Nullable
    public DecorativeObject findNearestDecorObject(int... ids) {
        if(!validChecks()) return null;
        Collection<DecorativeObject> decorativeObjects = getDecorativeObjects();

        return decorativeObjects.stream()
                .filter(decorativeObject -> Arrays.stream(ids).anyMatch(id -> id == decorativeObject.getId()))
                .min(Comparator.comparingInt(decorativeObject -> calculateDistance(decorativeObject.getWorldLocation(), client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }

    @Nullable
    public DecorativeObject findNearestDecorObjectWithin(WorldPoint worldPoint, int dist, int... ids) {
        if(!validChecks()) return null;
        Collection<DecorativeObject> decorativeObjects = getDecorativeObjects();


        return decorativeObjects.stream()
                .filter(decorativeObject -> Arrays.stream(ids).anyMatch(id -> id == decorativeObject.getId()))
                .filter(decorativeObject -> decorativeObject.getWorldLocation().distanceTo(worldPoint) <= dist)
                .min(Comparator.comparingInt(wallObject -> wallObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }

    @Nullable
    public DecorativeObject findNearestDecorObjectWithin(WorldPoint worldPoint, int dist) {
        if(!validChecks()) return null;
        Collection<DecorativeObject> decorativeObjects = getDecorativeObjects();

        return decorativeObjects.stream()
                .filter(decorativeObject -> decorativeObject.getWorldLocation().distanceTo(worldPoint) <= dist)
                .min(Comparator.comparingInt(decorativeObject -> decorativeObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }

    @Nullable
    public DecorativeObject findNearestDecorObjectMenuWithin(WorldPoint worldPoint, int dist, String menuAction) {
        if(!validChecks()) return null;
        Collection<DecorativeObject> decorativeObjects = getDecorativeObjects();

        return decorativeObjects.stream()
                .filter(decorativeObject -> decorativeObject.getWorldLocation().distanceTo(worldPoint) <= dist)
                .filter(decorativeObject -> ArrayUtils.contains(client.getObjectDefinition(decorativeObject.getId()).getActions(), menuAction))
                .min(Comparator.comparingInt(wallObject -> wallObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }

    @Nullable
    public GroundObject findNearestGroundObject(int... ids) {
        if(!validChecks()) return null;
        Collection<GroundObject> groundObjects = getGroundObjects();

        return groundObjects.stream()
                .filter(groundObject -> Arrays.stream(ids).anyMatch(id -> id == groundObject.getId()))
                .min(Comparator.comparingInt(groundObject -> calculateDistance(groundObject.getWorldLocation(), client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }

    @Nullable
    public GroundObject findNearestGroundObjectWithin(WorldPoint worldPoint, int dist, int... ids) {
        if(!validChecks()) return null;
        Collection<GroundObject> groundObjects = getGroundObjects();


        return groundObjects.stream()
                .filter(groundObject -> Arrays.stream(ids).anyMatch(id -> id == groundObject.getId()))
                .filter(groundObject -> groundObject.getWorldLocation().distanceTo(worldPoint) <= dist)
                .min(Comparator.comparingInt(wallObject -> wallObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }

    @Nullable
    public GroundObject findNearestGroundObjectWithin(WorldPoint worldPoint, int dist) {
        if(!validChecks()) return null;
        Collection<GroundObject> groundObjects = getGroundObjects();

        return groundObjects.stream()
                .filter(groundObject -> groundObject.getWorldLocation().distanceTo(worldPoint) <= dist)
                .min(Comparator.comparingInt(groundObject -> groundObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }

    @Nullable
    public GroundObject findNearestGroundObjectMenuWithin(WorldPoint worldPoint, int dist, String menuAction) {
        if(!validChecks()) return null;
        Collection<GroundObject> groundObjects = getGroundObjects();

        return groundObjects.stream()
                .filter(groundObject -> groundObject.getWorldLocation().distanceTo(worldPoint) <= dist)
                .filter(groundObject -> ArrayUtils.contains(client.getObjectDefinition(groundObject.getId()).getActions(), menuAction))
                .min(Comparator.comparingInt(wallObject -> wallObject.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }

    public List<GameObject> getGameObjects(int... ids) {
        if(!validChecks()) return null;
        Collection<GameObject> gameObjects = getGameObjects();

        return gameObjects.stream()
                .filter(gameObject -> Arrays.stream(ids).anyMatch(id -> id == gameObject.getId()))
                .collect(Collectors.toList());
    }

    public List<GameObject> getLocalGameObjects(int distanceAway, int... ids) {
        if (client.getLocalPlayer() == null) {
            return new ArrayList<>();
        }
        List<GameObject> localGameObjects = new ArrayList<>();
        for (GameObject gameObject : getGameObjects(ids)) {
            if (gameObject.getWorldLocation().distanceTo2D(client.getLocalPlayer().getWorldLocation()) < distanceAway) {
                localGameObjects.add(gameObject);
            }
        }
        return localGameObjects;
    }

    public GameObject getGameObjectAtWorldPoint(WorldPoint worldPoint) {
        if (!validChecks()) return null;
        Collection<GameObject> gameObjects = getGameObjects();

        return gameObjects.stream()
                .filter(gameObject -> gameObject.getWorldLocation().equals(worldPoint))
                .findFirst()
                .orElse(null);
    }


    public GameObject getGameObjectAtLocalPoint(LocalPoint localPoint) {
        if (!validChecks()) return null;
        Collection<GameObject> gameObjects = getGameObjects();

        return gameObjects.stream()
                .filter(gameObject -> gameObject.getLocalLocation().equals(localPoint))
                .findFirst()
                .orElse(null);
    }


    public WallObject getWallObjectAtWorldPoint(WorldPoint worldPoint) {
        if(!validChecks()) return null;
        Collection<WallObject> objects = getWallObjects();

        return objects.stream()
                .filter(object -> object.getWorldLocation().equals(worldPoint))
                .findFirst()
                .orElse(null);
    }

    public GroundObject getGroundObjectAtWorldPoint(WorldPoint worldPoint) {
        if(!validChecks()) return null;
        Collection<GroundObject> objects = getGroundObject();

        return objects.stream()
                .filter(object -> object.getWorldLocation().equals(worldPoint))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public GameObject findNearestBank() {
        if (!validChecks()) return null;
        Collection<GameObject> objects = getGameObjects();

        return objects.stream()
                .filter(gameObject -> ALL_BANKS.contains(gameObject.getId()))
                .min(Comparator.comparingInt(gameObject -> calculateDistance(gameObject.getWorldLocation(), client.getLocalPlayer().getWorldLocation())))
                .orElse(null);
    }



    private boolean validChecks(){
        if (client.getLocalPlayer() == null || !client.isClientThread()) {
            return false;
        }

        return true;
    }

    private Collection<GameObject> getGameObjects() {
        Collection<GameObject> objects = new ArrayList<>();
        for (Tile tile : getTiles()) {
            GameObject[] gameObjects = tile.getGameObjects();
            if (gameObjects != null) {
                objects.addAll(Arrays.asList(gameObjects));
            }
        }

        return objects;
    }

    private Collection<WallObject> getWallObjects() {
        Collection<WallObject> objects = new ArrayList<>();
        for (Tile tile : getTiles()) {
            WallObject wallObject = tile.getWallObject();
            if (wallObject != null) {
                objects.add(wallObject);
            }
        }

        return objects;
    }

    private Collection<DecorativeObject> getDecorativeObjects() {
        Collection<DecorativeObject> objects = new ArrayList<>();
        for (Tile tile : getTiles()) {
            DecorativeObject decorativeObject = tile.getDecorativeObject();
            if (decorativeObject != null) {
                objects.add(decorativeObject);
            }
        }

        return objects;
    }

    private Collection<GroundObject> getGroundObject() {
        Collection<GroundObject> objects = new ArrayList<>();
        for (Tile tile : getTiles()) {
            GroundObject groundObject = tile.getGroundObject();
            if (groundObject != null) {
                objects.add(groundObject);
            }
        }

        return objects;
    }

    protected List<Tile> getTiles() {
        List<Tile> tilesList = new ArrayList<>();
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = client.getPlane();
        for (Tile[] tileRow : tiles[z]) {
            for (Tile tile : tileRow) {
                if (tile != null) {
                    tilesList.add(tile);
                }
            }
        }
        return tilesList;
    }

    private int calculateDistance(WorldPoint point1, WorldPoint point2) {
        return point1.distanceTo(point2);
    }

    private GameObject findNearestGameObject(int... gameObjectIds) {
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        return getGameObjects().stream()
                .filter(gameObject -> Arrays.stream(gameObjectIds).anyMatch(id -> id == gameObject.getId()))
                .min(Comparator.comparingInt(gameObject -> calculateDistance(playerLocation, gameObject.getWorldLocation())))
                .orElse(null);
    }
}
