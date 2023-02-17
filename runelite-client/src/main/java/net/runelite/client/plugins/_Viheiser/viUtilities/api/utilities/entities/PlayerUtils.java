package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.entities;

import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.ChatMessageHandler;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.calculations.CalculatorUtils;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.interactions.MenuEntryInteraction;
import net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.menuentries.WidgetMenuEntries;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.mappings.FieldNameMapping;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.reflection.ReflectionManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class PlayerUtils {
    @Inject
    private InventoryUtils inventoryUtils;
    @Inject
    private BankUtils bankUtils;
    @Inject
    private CalculatorUtils calculatorUtils;
    @Inject
    private ReflectionManager reflectionManager;
    @Inject
    private ChatMessageHandler chatMessageHandler;
    @Inject
    private MenuEntryInteraction menuEntryInteraction;
    @Inject
    private WidgetMenuEntries widgetMenuEntries;
    @Inject
    private Client client;
    private int nextRunEnergy;
    public boolean isMoving() {
        return reflectionManager.getIntField(FieldNameMapping.PATH_LENGTH, client.getLocalPlayer()) > 0;
    }

    public boolean isInteractingWith(NPC npc) {
        Player player = client.getLocalPlayer();
        if (player.getInteracting() == null)
            return false;

        if (npc.getName().contains(player.getInteracting().getName())) {
            return true;
        }

        return false;
    }

    public boolean isAnimating() {
        return client.getLocalPlayer().getAnimation() != -1;
    }

    public boolean isRunEnabled() {
        return client.getVarpValue(173) == 1;
    }

    //enables run if below given minimum energy with random positive variation
    public void handleRun(int minEnergy, int randMax) {
        assert client.isClientThread();
        if (nextRunEnergy < minEnergy || nextRunEnergy > minEnergy + randMax) {
            nextRunEnergy = calculatorUtils.getRandomIntBetweenRange(minEnergy, minEnergy + calculatorUtils.getRandomIntBetweenRange(0, randMax));
        }
        if (client.getEnergy() > nextRunEnergy ||
                client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) != 0) {
            if (drinkStaminaPotion(15 + calculatorUtils.getRandomIntBetweenRange(0, 30))) {
                return;
            }
            if (!isRunEnabled()) {
                nextRunEnergy = 0;
                enableRun();
            }
        }
    }

    public void handleRun(int minEnergy, int randMax, int potEnergy) {
        assert client.isClientThread();
        if (nextRunEnergy < minEnergy || nextRunEnergy > minEnergy + randMax) {
            nextRunEnergy = calculatorUtils.getRandomIntBetweenRange(minEnergy, minEnergy + calculatorUtils.getRandomIntBetweenRange(0, randMax));
        }
        if (client.getEnergy() > (minEnergy + calculatorUtils.getRandomIntBetweenRange(0, randMax)) ||
                client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) != 0) {
            if (drinkStaminaPotion(potEnergy)) {
                return;
            }
            if (!isRunEnabled()) {
                nextRunEnergy = 0;
                    enableRun();
            }
        }
    }

    public void enableRun() {
        chatMessageHandler.sendGameMessage("Enabling Run");
        menuEntryInteraction.invokeMenuAction(widgetMenuEntries.createToggleRunEntry());
    }

    //Checks if Stamina enhancement is active and if stamina potion is in inventory
    public Widget shouldDrunkStaminaPotion(int energy) {
        final java.util.List<Integer> STAMINA_POTIONS = java.util.List.of(ItemID.STAMINA_POTION1, ItemID.STAMINA_POTION2, ItemID.STAMINA_POTION3,
                ItemID.STAMINA_POTION4, ItemID.ENERGY_POTION1, ItemID.ENERGY_POTION2, ItemID.ENERGY_POTION3, ItemID.ENERGY_POTION4,
                ItemID.SUPER_ENERGY1, ItemID.SUPER_ENERGY2, ItemID.SUPER_ENERGY3, ItemID.SUPER_ENERGY4, ItemID.EGNIOL_POTION_1,
                ItemID.EGNIOL_POTION_2, ItemID.EGNIOL_POTION_3, ItemID.EGNIOL_POTION_4);

        if (!inventoryUtils.getInventoryItems(STAMINA_POTIONS).isEmpty()
                && client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) == 0 && client.getEnergy() < energy && !bankUtils.isOpen()) {
            return inventoryUtils.getWidget(STAMINA_POTIONS);
        } else {
            return null;
        }
    }

    public boolean drinkStaminaPotion(int energy) {
        Widget staminaPotion = shouldDrunkStaminaPotion(energy);
        if (staminaPotion != null) {
            chatMessageHandler.sendGameMessage("Drinking Stamina Potion");
            inventoryUtils.interactWithItemInvoke(
                    staminaPotion.getId(),
                    "drink"
            );
            return true;
        }
        return false;
    }

    public java.util.List<Item> getEquippedItems() {
        assert client.isClientThread();

        List<Item> equipped = new ArrayList<>();
        Item[] items = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
        for (Item item : items) {
            if (item.getId() == -1 || item.getId() == 0) {
                continue;
            }
            equipped.add(item);
        }
        return equipped;
    }

    /*
     *
     * Returns if a specific item is equipped
     *
     * */
    public boolean isItemEquipped(Collection<Integer> itemIds) {
        assert client.isClientThread();

        ItemContainer equipmentContainer = client.getItemContainer(InventoryID.EQUIPMENT);
        if (equipmentContainer != null) {
            Item[] items = equipmentContainer.getItems();
            for (Item item : items) {
                if (itemIds.contains(item.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
}
