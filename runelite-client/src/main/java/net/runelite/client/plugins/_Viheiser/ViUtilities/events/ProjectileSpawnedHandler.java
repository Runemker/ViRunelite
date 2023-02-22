package net.runelite.client.plugins._Viheiser.ViUtilities.events;

import net.runelite.api.Client;
import net.runelite.api.Projectile;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins._Viheiser.ViUtilities.api.events.ProjectileSpawned;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectileSpawnedHandler {
    @Inject
    private Client client;

    private List<Projectile> previousProjectiles = new ArrayList<>();
    @Subscribe
    private void onGameTick(GameTick event) {
        Iterator<Projectile> projectiles = client.getProjectiles().iterator();
        if(!projectiles.hasNext()) return;

        List<Projectile> currentProjectiles = getProjectiles(projectiles);
        List<Projectile> newProjectiles = findNewProjectiles(currentProjectiles);
        if(!newProjectiles.isEmpty())
            triggerProjectileSpawnedEvent(newProjectiles);

        previousProjectiles = currentProjectiles;
    }

    private void triggerProjectileSpawnedEvent(List<Projectile> newProjectiles) {
        for(Projectile projectile : newProjectiles){
            client.getCallbacks().post(new ProjectileSpawned(projectile));
        }
    }

    private List<Projectile> findNewProjectiles(List<Projectile> currentProjectiles) {
        List<Projectile> foundProjectiles = new ArrayList<>();
        for(Projectile projectile : currentProjectiles){
            if(!previousProjectiles.contains(projectile)){
                foundProjectiles.add(projectile);
            }
        }

        return foundProjectiles;

    }

    private List<Projectile> getProjectiles(Iterator<Projectile> projectileIterator) {
        List<Projectile> projectiles = new ArrayList<>();
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();
            projectiles.add(projectile);
        }

        return projectiles;
    }
}
