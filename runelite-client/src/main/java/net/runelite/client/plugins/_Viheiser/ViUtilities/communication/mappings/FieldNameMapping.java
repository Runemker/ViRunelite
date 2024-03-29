package net.runelite.client.plugins._Viheiser.ViUtilities.communication.mappings;

public enum FieldNameMapping {
    SELECTED_SCENE_TILE_X("selectedSceneTileX", "ax", ClassNameMapping.SCENE),
    SELECTED_SCENE_TILE_Y("selectedSceneTileY", "az", ClassNameMapping.SCENE),
    VIEWPORT_WALKING("viewportWalking", "ap", ClassNameMapping.SCENE),
    CHECK_CLICK("checkClick", "ar", ClassNameMapping.SCENE),
    PATH_LENGTH("pathLength", "ci", ClassNameMapping.ACTOR),
    SELECTED_SPELL_WIDGET("selectedSpellWidget", "on", ClassNameMapping.CLASS9),
    SELECTED_SPELL_CHILD_INDEX("selectedSpellChildIndex", "ol", ClassNameMapping.CLIENT),
    SELECTED_SPELL_ITEM_ID("selectedSpellItemId", "oo", ClassNameMapping.CLIENT),
    MENU_OPTIONS_COUNT("menuOptionsCount", "np", ClassNameMapping.CLIENT),
    MENU_ARGUMENTS_1("menuArguments1", "nu", ClassNameMapping.CLIENT),
    MENU_ARGUMENTS_2("menuArguments2", "nq", ClassNameMapping.CLIENT),
    MENU_OPCODES("menuOpcodes", "nw", ClassNameMapping.CLIENT),
    MENU_IDENTIFIERS("menuIdentifiers", "nm", ClassNameMapping.CLIENT),
    MENU_ITEM_IDS("menuItemIds", "nn", ClassNameMapping.CLIENT),
    MENU_ACTIONS("menuActions", "na", ClassNameMapping.CLIENT),
    MENU_TARGETS("menuTargets", "no", ClassNameMapping.CLIENT),
    ;

    private final String obfuscatedName;
    private final String readableName;
    private final ClassNameMapping classReference;

    private FieldNameMapping(String readableName, String obfuscatedName, ClassNameMapping classReference) {
        this.obfuscatedName = obfuscatedName;
        this.readableName = readableName;
        this.classReference = classReference;
    }

    public String getObfuscatedName() {
        return obfuscatedName;
    }

    public String getReadableName() {
        return readableName;
    }

    public ClassNameMapping getClassReference() {
        return classReference;
    }

    public String getClassObfuscatedName() {return classReference.getObfuscatedName();}
}
