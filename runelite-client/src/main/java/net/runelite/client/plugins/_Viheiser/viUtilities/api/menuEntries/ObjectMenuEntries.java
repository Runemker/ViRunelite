package net.runelite.client.plugins._Viheiser.viUtilities.api.menuEntries;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.retrievers.GameObjectRetriever;

import javax.inject.Inject;

public class ObjectMenuEntries {
    @Inject
    private Client client;
    @Inject
    private GameObjectRetriever gameObjectRetriever;

    public MenuEntry openBankBooth(int boothId){
        GameObject bankObject = gameObjectRetriever.getNearestGameObjectWithIds(boothId);

        if (bankObject != null) {
            return createMenuEntry(
                    boothId,
                    MenuAction.GAME_OBJECT_SECOND_OPTION,
                    bankObject.getSceneMinLocation().getX(),
                    bankObject.getSceneMinLocation().getY(),
                    false);
        }

        return null;
    }

    public MenuEntry openBankChest(int chestId){
        GameObject bankObject = gameObjectRetriever.getNearestGameObjectWithIds(chestId);

        if (bankObject != null) {
            return createMenuEntry(
                    chestId,
                    MenuAction.GAME_OBJECT_FIRST_OPTION,
                    bankObject.getSceneMinLocation().getX(),
                    bankObject.getSceneMinLocation().getY(),
                    false);
        }

        return null;
    }

    private MenuEntry createMenuEntry(int identifier, MenuAction type, int param0, int param1, boolean forceLeftClick) {
        return client.createMenuEntry(0).setOption("").setTarget("").setIdentifier(identifier).setType(type)
                .setParam0(param0).setParam1(param1).setForceLeftClick(forceLeftClick);
    }
}
