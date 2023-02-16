package net.runelite.client.plugins._Viheiser.viUtilities.communication.reflection.wrappers;

import java.lang.reflect.Field;

public class ReflectedFieldWrapper {
    public ReflectedFieldWrapper(Class<?> reflectedClass, Field reflectedField){
        this.reflectedClass = reflectedClass;
        this.reflectedField = reflectedField;
    }
    private Class<?> reflectedClass;
    private Field reflectedField;

    public Field getReflectedField() {
        return reflectedField;
    }

    public Class<?> getReflectedClass() {
        return reflectedClass;
    }
}
