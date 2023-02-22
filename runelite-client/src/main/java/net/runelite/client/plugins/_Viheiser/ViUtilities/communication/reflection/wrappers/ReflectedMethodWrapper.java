package net.runelite.client.plugins._Viheiser.ViUtilities.communication.reflection.wrappers;

import java.lang.reflect.Method;

public class ReflectedMethodWrapper {
    public ReflectedMethodWrapper(Class<?> reflectedClass, Method reflectedMethod){
        this.reflectedClass = reflectedClass;
        this.reflectedMethod = reflectedMethod;
    }
    private Class<?> reflectedClass;
    private Method reflectedMethod;

    public Method getReflectedMethod() {
        return reflectedMethod;
    }

    public Class<?> getReflectedClass() {
        return reflectedClass;
    }
}
