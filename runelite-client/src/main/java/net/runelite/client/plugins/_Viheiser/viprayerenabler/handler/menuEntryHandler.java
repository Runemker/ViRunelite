package net.runelite.client.plugins._Viheiser.viprayerenabler.handler;

import net.runelite.api.Client;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public class menuEntryHandler {

    @Inject
    public Client client;

    public Consumer consumer;
    public int idx = 499;



    public int getIdentifier()
    {
        return menuIdentifiers();
    }

    private int menuIdentifiers(){
        try {
            Class<?> clientClass = client.getClass();
            Field menuOptionField = clientClass.getDeclaredField("nm");
            menuOptionField.setAccessible(true);
            int[] IdentifierField = (int[]) menuOptionField.get(clientClass);
            return IdentifierField[idx];
        } catch (Exception e) {
            System.err.println("Failed to set new identifier: " + e.getMessage());
        }

        return -1;
    }
}
