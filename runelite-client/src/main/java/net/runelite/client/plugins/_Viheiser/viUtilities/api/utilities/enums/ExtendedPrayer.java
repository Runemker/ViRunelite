package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.enums;

import net.runelite.api.Prayer;
import net.runelite.api.widgets.WidgetInfo;

public enum ExtendedPrayer {
    /**
     * Thick Skin (Level 1, Defence).
     */
    THICK_SKIN(Prayer.THICK_SKIN,  WidgetInfo.PRAYER_THICK_SKIN),
    /**
     * Burst of Strength (Level 4, Strength).
     */
    BURST_OF_STRENGTH(Prayer.BURST_OF_STRENGTH,  WidgetInfo.PRAYER_BURST_OF_STRENGTH),
    /**
     * Clarity of Thought (Level 7, Attack).
     */
    CLARITY_OF_THOUGHT(Prayer.CLARITY_OF_THOUGHT,  WidgetInfo.PRAYER_CLARITY_OF_THOUGHT),
    /**
     * Sharp Eye (Level 8, Ranging).
     */
    SHARP_EYE(Prayer.SHARP_EYE,  WidgetInfo.PRAYER_SHARP_EYE),
    /**
     * Mystic Will (Level 9, Magic).
     */
    MYSTIC_WILL(Prayer.MYSTIC_WILL,  WidgetInfo.PRAYER_MYSTIC_WILL),
    /**
     * Rock Skin (Level 10, Defence).
     */
    ROCK_SKIN(Prayer.ROCK_SKIN, WidgetInfo.PRAYER_ROCK_SKIN),
    /**
     * Superhuman Strength (Level 13, Strength).
     */
    SUPERHUMAN_STRENGTH(Prayer.SUPERHUMAN_STRENGTH, WidgetInfo.PRAYER_SUPERHUMAN_STRENGTH),
    /**
     * Improved Reflexes (Level 16, Attack).
     */
    IMPROVED_REFLEXES(Prayer.IMPROVED_REFLEXES,  WidgetInfo.PRAYER_IMPROVED_REFLEXES),
    /**
     * Rapid Restore (Level 19, Stats).
     */
    RAPID_RESTORE(Prayer.RAPID_RESTORE, WidgetInfo.PRAYER_RAPID_RESTORE),
    /**
     * Rapid Heal (Level 22, Hitpoints).
     */
    RAPID_HEAL(Prayer.RAPID_HEAL,  WidgetInfo.PRAYER_RAPID_HEAL),
    /**
     * Protect Item (Level 25).
     */
    PROTECT_ITEM(Prayer.PROTECT_ITEM,  WidgetInfo.PRAYER_PROTECT_ITEM),
    /**
     * Hawk Eye (Level 26, Ranging).
     */
    HAWK_EYE(Prayer.HAWK_EYE,  WidgetInfo.PRAYER_HAWK_EYE),
    /**
     * Mystic Lore (Level 27, Magic).
     */
    MYSTIC_LORE(Prayer.MYSTIC_LORE,  WidgetInfo.PRAYER_MYSTIC_LORE),
    /**
     * Steel Skin (Level 28, Defence).
     */
    STEEL_SKIN(Prayer.STEEL_SKIN,  WidgetInfo.PRAYER_STEEL_SKIN),
    /**
     * Ultimate Strength (Level 31, Strength).
     */
    ULTIMATE_STRENGTH(Prayer.ULTIMATE_STRENGTH,  WidgetInfo.PRAYER_ULTIMATE_STRENGTH),
    /**
     * Incredible Reflexes (Level 34, Attack).
     */
    INCREDIBLE_REFLEXES(Prayer.INCREDIBLE_REFLEXES, WidgetInfo.PRAYER_INCREDIBLE_REFLEXES),
    /**
     * Protect from Magic (Level 37).
     */
    PROTECT_FROM_MAGIC(Prayer.PROTECT_FROM_MAGIC, WidgetInfo.PRAYER_PROTECT_FROM_MAGIC),
    /**
     * Protect from Missiles (Level 40).
     */
    PROTECT_FROM_MISSILES(Prayer.PROTECT_FROM_MISSILES, WidgetInfo.PRAYER_PROTECT_FROM_MISSILES),
    /**
     * Protect from Melee (Level 43).
     */
    PROTECT_FROM_MELEE(Prayer.PROTECT_FROM_MELEE,  WidgetInfo.PRAYER_PROTECT_FROM_MELEE),
    /**
     * Eagle Eye (Level 44, Ranging).
     */
    EAGLE_EYE(Prayer.EAGLE_EYE,  WidgetInfo.PRAYER_EAGLE_EYE),
    /**
     * Mystic Might (Level 45, Magic).
     */
    MYSTIC_MIGHT(Prayer.MYSTIC_MIGHT,  WidgetInfo.PRAYER_MYSTIC_MIGHT),
    /**
     * Retribution (Level 46).
     */
    RETRIBUTION(Prayer.RETRIBUTION,  WidgetInfo.PRAYER_RETRIBUTION),
    /**
     * Redemption (Level 49).
     */
    REDEMPTION(Prayer.REDEMPTION,  WidgetInfo.PRAYER_REDEMPTION),
    /**
     * Smite (Level 52).
     */
    SMITE(Prayer.SMITE,  WidgetInfo.PRAYER_SMITE),
    /**
     * Chivalry (Level 60, Defence/Strength/Attack).
     */
    CHIVALRY(Prayer.CHIVALRY, WidgetInfo.PRAYER_CHIVALRY),
    /**
     * Piety (Level 70, Defence/Strength/Attack).
     */
    PIETY(Prayer.PIETY, WidgetInfo.PRAYER_PIETY),
    /**
     * Preserve (Level 55).
     */
    PRESERVE(Prayer.PRESERVE, WidgetInfo.PRAYER_PRESERVE),
    /**
     * Rigour (Level 74, Ranging/Damage/Defence).
     */
    RIGOUR(Prayer.RIGOUR, WidgetInfo.PRAYER_RIGOUR),
    /**
     * Augury (Level 77, Magic/Magic Def./Defence).
     */
    AUGURY(Prayer.AUGURY, WidgetInfo.PRAYER_AUGURY);

    private final Prayer prayer;
    private final WidgetInfo widgetInfo;

    ExtendedPrayer(Prayer prayer, WidgetInfo widgetInfo)
    {
        this.widgetInfo = widgetInfo;
        this.prayer = prayer;
    }

    public Prayer getPrayer(){ return prayer; }

    public WidgetInfo getWidgetInfo()
    {
        return widgetInfo;
    }
}
