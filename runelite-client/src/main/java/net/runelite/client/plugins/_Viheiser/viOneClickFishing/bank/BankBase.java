package net.runelite.client.plugins._Viheiser.viOneClickFishing.bank;

import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.coords.WorldArea;

public abstract class BankBase {
    public Client client;
    public BankBase(Client client) {
        this.client = client;
    }

    public WorldArea getAreaNearBank(){
        return null;
    }

    public WorldArea getAreaNearFish(){
        return null;
    }
    public boolean isNpc(){
        return false;
    }
    public boolean isBooth(){
        return false;
    }
    public MenuEntry openBank(){
        return null;
    }

    public boolean canSeeBank(){
        return false;
    }

    public boolean canSeeFish(){
        return false;
    }

}
