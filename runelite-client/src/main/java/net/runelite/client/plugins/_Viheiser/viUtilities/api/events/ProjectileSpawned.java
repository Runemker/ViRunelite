package net.runelite.client.plugins._Viheiser.viUtilities.api.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.runelite.api.Actor;
import net.runelite.api.Animation;
import net.runelite.api.Projectile;
import net.runelite.api.coords.LocalPoint;

import javax.annotation.Nullable;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class ProjectileSpawned{

    @Getter
    private final Projectile projectile;

    int getId(){
        return projectile.getId();
    }

    Actor getInteracting(){
        return projectile.getInteracting();
    }

    LocalPoint getTarget(){
        return projectile.getTarget();
    }
    int getX1(){
        return projectile.getX1();
    }
    int getY1(){
        return projectile.getY1();
    }
    int getFloor(){
        return projectile.getFloor();
    }
    int getHeight(){
        return projectile.getHeight();
    }
    int getEndHeight(){
        return projectile.getEndHeight();
    }
    int getStartCycle(){
        return projectile.getStartCycle();
    }
    int getEndCycle(){
        return projectile.getEndCycle();
    }
    int getRemainingCycles(){
        return projectile.getRemainingCycles();
    }
    int getSlope(){
        return projectile.getSlope();
    }
    int getStartHeight(){
        return projectile.getStartHeight();
    }
    double getX(){
        return projectile.getX();
    }
    double getY(){
        return projectile.getY();
    }
    double getZ(){
        return projectile.getZ();
    }
    double getScalar(){
        return projectile.getScalar();
    }
    double getVelocityX(){
        return projectile.getVelocityX();
    }
    double getVelocityY(){
        return projectile.getVelocityY();
    }
    double getVelocityZ(){
        return projectile.getVelocityZ();
    }
    @Nullable
    Animation getAnimation(){
        return projectile.getAnimation();
    }
    int getAnimationFrame(){
        return projectile.getAnimationFrame();
    }
}
