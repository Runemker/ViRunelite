package net.runelite.client.plugins._Viheiser.ViUtilities.communication.mappings;

public enum ClassNameMapping {
    CLIENT("Client", "client"),
    SCENE("Scene", "hn"),
    FILE_SYSTEM("FileSystem", "fw"),
    ACTOR("Actor", "cb"),
    CLASS9("Class9", "k"),
    PROJECTILE("Projectile", "bp"),
    ;

    private final String obfuscatedName;
    private final String readableName;

    private ClassNameMapping(String readableName, String obfuscatedName) {
        this.obfuscatedName = obfuscatedName;
        this.readableName = readableName;
    }

    public String getObfuscatedName() {
        return obfuscatedName;
    }

    public String getReadableName() {
        return readableName;
    }
}
