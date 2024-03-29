package net.runelite.client.plugins._Viheiser.ViUtilities.api.utilities.interactions;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins._Viheiser.ViUtilities.ViUtilitiesPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Singleton
@PluginDependency(ViUtilitiesPlugin.class)
public class ActionQueue {
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private MouseInteractions mouseInteractions;
    @Inject
    private ViUtilitiesPlugin plugin;
    public final List<DelayedAction> delayedActions = new ArrayList<>();
    private int clientTick = 0;
    private int gameTick = 0;

    public void runDelayedActions() {
        Iterator<DelayedAction> it = delayedActions.iterator();
        while (it.hasNext()) {
            DelayedAction action = it.next();
            if (action.shouldRun.get()) {
                action.runnable.run();
                it.remove();
            }
        }
    }

    @Subscribe
    public void onClientTick(ClientTick e) {
        clientTick++;
        runDelayedActions();
    }

    @Subscribe
    public void onGameTick(GameTick e) {
        gameTick++;
        runDelayedActions();
    }

    public static void sleep(long toSleep) {
        try {
            long start = System.currentTimeMillis();
            Thread.sleep(toSleep);

            // Guarantee minimum sleep
            long now;
            while (start + toSleep > (now = System.currentTimeMillis())) {
                Thread.sleep(start + toSleep - now);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runLater(Supplier<Boolean> condition, Runnable runnable) {
        if (condition.get()) {
            runnable.run();
        } else {
            delayedActions.add(new DelayedAction(condition, runnable));
        }
    }

    public void delayGameTicks(long delay, Runnable runnable) {
        long when = gameTick + delay;
        runLater(() -> gameTick >= when, runnable);
    }

    public void delayClientTicks(long delay, Runnable runnable) {
        long when = clientTick + delay;
        runLater(() -> clientTick >= when, runnable);
    }

    public void delayInvokesTime(long delay, Runnable runnable) {
        plugin.getExecutorService().submit(() -> {
            clientThread.invoke(() -> {
                sleep(delay);
                runnable.run();
            });
        });
    }

    public void delayMouseTime(long delay, Runnable runnable) {
        long when = System.currentTimeMillis() + delay;
        runLater(() -> System.currentTimeMillis() >= when, runnable);
    }

    @Value
    public static class DelayedAction {
        Supplier<Boolean> shouldRun;
        Runnable runnable;
    }
}
