package net.runelite.client.plugins._Viheiser.viUtilities.communication.mappings;

public enum MethodNameMapping {
    INVOKE_MENU_ACTION("invokeMenuAction", "im", ClassNameMapping.FILE_SYSTEM,
            new Class[]{
                    int.class, int.class, int.class, int.class, int.class,
                    String.class, String.class, int.class, int.class, byte.class
            }),
    INSERT_MENU_ACTION("insertMenuAction", "xg", ClassNameMapping.CLIENT,
            new Class[]{
                    String.class, String.class, int.class, int.class,
                    int.class, int.class, int.class, boolean.class
            }),
    ;

    private final String obfuscatedName;
    private final String readableName;
    private final ClassNameMapping classReference;
    private final Class<?>[] argumentTypes;

    MethodNameMapping(String readableName, String obfuscatedName,
                      ClassNameMapping classReference, Class<?>[] argumentTypes) {
        this.obfuscatedName = obfuscatedName;
        this.readableName = readableName;
        this.classReference = classReference;
        this.argumentTypes = argumentTypes;
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

    public Class<?>[] getArgumentTypes() {
        return argumentTypes;
    }
}
