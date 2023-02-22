package net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.menuentries;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.retrievers.GameObjectRetriever;

import javax.inject.Inject;

public class ObjectMenuEntries {
    @Inject
    private Client client;
    @Inject
    private GameObjectRetriever gameObjectRetriever;

    public MenuEntry openBankBooth(GameObject booth){
        if (booth != null) {
            return createMenuEntry(
                    booth.getId(),
                    MenuAction.GAME_OBJECT_SECOND_OPTION,
                    booth.getSceneMinLocation().getX(),
                    booth.getSceneMinLocation().getY(),
                    false);
        }

        return null;
    }

    public MenuEntry firstOption(GameObject gameObject){
        if (gameObject != null) {
            return createMenuEntry(
                    gameObject.getId(),
                    MenuAction.GAME_OBJECT_FIRST_OPTION,
                    gameObject.getSceneMinLocation().getX(),
                    gameObject.getSceneMinLocation().getY(),
                    false);
        }

        return null;
    }

    public MenuEntry secondOption(GameObject gameObject){
        if (gameObject != null) {
            return createMenuEntry(
                    gameObject.getId(),
                    MenuAction.GAME_OBJECT_SECOND_OPTION,
                    gameObject.getSceneMinLocation().getX(),
                    gameObject.getSceneMinLocation().getY(),
                    false);
        }

        return null;
    }

    public MenuEntry openBankChest(GameObject chest){
        if (chest != null) {
            return createMenuEntry(
                    chest.getId(),
                    MenuAction.GAME_OBJECT_FIRST_OPTION,
                    chest.getSceneMinLocation().getX(),
                    chest.getSceneMinLocation().getY(),
                    false);
        }

        return null;
    }

    private MenuEntry createMenuEntry(int identifier, MenuAction type, int param0, int param1, boolean forceLeftClick) {
        return client.createMenuEntry(0).setOption("").setTarget("").setIdentifier(identifier).setType(type)
                .setParam0(param0).setParam1(param1).setForceLeftClick(forceLeftClick);
    }
}
