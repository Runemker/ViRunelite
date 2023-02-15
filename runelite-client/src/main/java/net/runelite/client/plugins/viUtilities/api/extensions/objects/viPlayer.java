package net.runelite.client.plugins.viUtilities.api.extensions.objects;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.viUtilities.api.extensions.interfaces.PlayerExtensions;
import net.runelite.client.plugins.viUtilities.communication.mappings.FieldNameMapping;
import net.runelite.client.plugins.viUtilities.communication.reflection.ReflectionManager;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;

public class viPlayer implements PlayerExtensions {
    @Inject
    private Client client;
    @Inject
    private ReflectionManager reflectionManager;

    private Player getPlayer(){
        return client.getLocalPlayer();
    }

    @Override
    public boolean isMoving() {
        return reflectionManager.getIntField(FieldNameMapping.PATH_LENGTH, getPlayer()) > 0;
    }

    @Nullable
    @Override
    public String getName() {
        return getPlayer().getName();
    }

    @Override
    public Actor getInteracting() {
        return getPlayer().getInteracting();
    }

    @Override
    public int getHealthRatio() {
        return getPlayer().getHealthRatio();
    }

    @Override
    public int getHealthScale() {
        return getPlayer().getHealthScale();
    }

    @Override
    public WorldPoint getWorldLocation() {
        return getPlayer().getWorldLocation();
    }

    @Override
    public LocalPoint getLocalLocation() {
        return getPlayer().getLocalLocation();
    }

    @Override
    public int getOrientation() {
        return getPlayer().getOrientation();
    }

    @Override
    public int getCurrentOrientation() {
        return getPlayer().getCurrentOrientation();
    }

    @Override
    public int getAnimation() {
        return getPlayer().getAnimation();
    }

    @Override
    public int getPoseAnimation() {
        return getPlayer().getPoseAnimation();
    }

    @Override
    public void setPoseAnimation(int animation) {
        getPlayer().setPoseAnimation(animation);
    }

    @Override
    public int getPoseAnimationFrame() {
        return getPlayer().getPoseAnimationFrame();
    }

    @Override
    public void setPoseAnimationFrame(int frame) {
        getPlayer().setPoseAnimationFrame(frame);
    }

    @Override
    public int getIdlePoseAnimation() {
        return getPlayer().getIdlePoseAnimation();
    }

    @Override
    public void setIdlePoseAnimation(int animation) {
        getPlayer().setIdlePoseAnimation(animation);
    }

    @Override
    public int getIdleRotateLeft() {
        return getPlayer().getIdleRotateLeft();
    }

    @Override
    public void setIdleRotateLeft(int animationID) {
        getPlayer().setIdleRotateLeft(animationID);
    }

    @Override
    public int getIdleRotateRight() {
        return getPlayer().getIdleRotateRight();
    }

    @Override
    public void setIdleRotateRight(int animationID) {
        getPlayer().setIdleRotateRight(animationID);
    }

    @Override
    public int getWalkAnimation() {
        return getPlayer().getWalkAnimation();
    }

    @Override
    public void setWalkAnimation(int animationID) {
        getPlayer().setWalkAnimation(animationID);
    }

    @Override
    public int getWalkRotateLeft() {
        return getPlayer().getWalkRotateLeft();
    }

    @Override
    public void setWalkRotateLeft(int animationID) {
        getPlayer().setWalkRotateLeft(animationID);
    }

    @Override
    public int getWalkRotateRight() {
        return getPlayer().getWalkRotateRight();
    }

    @Override
    public void setWalkRotateRight(int animationID) {
        getPlayer().setWalkRotateRight(animationID);
    }

    @Override
    public int getWalkRotate180() {
        return getPlayer().getWalkRotate180();
    }

    @Override
    public void setWalkRotate180(int animationID) {
        getPlayer().setWalkRotate180(animationID);
    }

    @Override
    public int getRunAnimation() {
        return getPlayer().getRunAnimation();
    }

    @Override
    public void setRunAnimation(int animationID) {
        getPlayer().setRunAnimation(animationID);
    }

    @Override
    public void setAnimation(int animation) {
        getPlayer().setAnimation(animation);
    }

    @Override
    public int getAnimationFrame() {
        return getPlayer().getAnimationFrame();
    }

    @Override
    public void setActionFrame(int frame) {
        getPlayer().setActionFrame(frame);
    }

    @Override
    public void setAnimationFrame(int frame) {
        getPlayer().setAnimationFrame(frame);
    }

    @Override
    public int getGraphic() {
        return getPlayer().getGraphic();
    }

    @Override
    public void setGraphic(int graphic) {
        getPlayer().setGraphic(graphic);
    }

    @Override
    public int getGraphicHeight() {
        return getPlayer().getGraphicHeight();
    }

    @Override
    public void setGraphicHeight(int height) {
        getPlayer().setGraphicHeight(height);
    }

    @Override
    public int getSpotAnimFrame() {
        return getPlayer().getSpotAnimFrame();
    }

    @Override
    public void setSpotAnimFrame(int spotAnimFrame) {
        getPlayer().setSpotAnimFrame(spotAnimFrame);
    }

    @Override
    public Polygon getCanvasTilePoly() {
        return getPlayer().getCanvasTilePoly();
    }

    @Nullable
    @Override
    public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
        return getPlayer().getCanvasTextLocation(graphics, text, zOffset);
    }

    @Override
    public Point getCanvasImageLocation(BufferedImage image, int zOffset) {
        return getPlayer().getCanvasImageLocation(image, zOffset);
    }

    @Override
    public Point getCanvasSpriteLocation(SpritePixels sprite, int zOffset) {
        return getPlayer().getCanvasSpriteLocation(sprite, zOffset);
    }

    @Override
    public Point getMinimapLocation() {
        return getPlayer().getMinimapLocation();
    }

    @Override
    public int getLogicalHeight() {
        return getPlayer().getLogicalHeight();
    }

    @Override
    public Shape getConvexHull() {
        return getPlayer().getConvexHull();
    }

    @Override
    public WorldArea getWorldArea() {
        return getPlayer().getWorldArea();
    }

    @Override
    public String getOverheadText() {
        return getPlayer().getOverheadText();
    }

    @Override
    public void setOverheadText(String overheadText) {
        getPlayer().setOverheadText(overheadText);
    }

    @Override
    public int getOverheadCycle() {
        return getPlayer().getOverheadCycle();
    }

    @Override
    public void setOverheadCycle(int cycles) {
        getPlayer().setOverheadCycle(cycles);
    }

    @Override
    public boolean isDead() {
        return getPlayer().isDead();
    }

    @Override
    public void setDead(boolean dead) {
        getPlayer().setDead(dead);
    }

    @Override
    public Node getNext() {
        return getPlayer().getNext();
    }

    @Override
    public Node getPrevious() {
        return getPlayer().getPrevious();
    }

    @Override
    public long getHash() {
        return getPlayer().getHash();
    }

    @Override
    public int getId() {
        return getPlayer().getId();
    }

    @Override
    public int getCombatLevel() {
        return getPlayer().getCombatLevel();
    }

    @Override
    public PlayerComposition getPlayerComposition() {
        return getPlayer().getPlayerComposition();
    }

    @Override
    public Polygon[] getPolygons() {
        return getPlayer().getPolygons();
    }

    @Override
    public int getTeam() {
        return getPlayer().getTeam();
    }

    @Override
    public boolean isFriendsChatMember() {
        return getPlayer().isFriendsChatMember();
    }

    @Override
    public boolean isFriend() {
        return getPlayer().isFriend();
    }

    @Override
    public boolean isClanMember() {
        return getPlayer().isClanMember();
    }

    @Override
    public HeadIcon getOverheadIcon() {
        return getPlayer().getOverheadIcon();
    }

    @Nullable
    @Override
    public SkullIcon getSkullIcon() {
        return getPlayer().getSkullIcon();
    }

    @Override
    public Model getModel() {
        return getPlayer().getModel();
    }

    @Override
    public int getModelHeight() {
        return getPlayer().getModelHeight();
    }

    @Override
    public void setModelHeight(int modelHeight) {
        getPlayer().setModelHeight(modelHeight);
    }

    @Override
    public void draw(int orientation, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y, int z, long hash) {
        getPlayer().draw(orientation, pitchSin, pitchCos, yawSin, yawCos, x, y, z, hash);
    }
}
