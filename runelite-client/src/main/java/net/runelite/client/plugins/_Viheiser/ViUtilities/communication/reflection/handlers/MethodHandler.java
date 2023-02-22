package net.runelite.client.plugins._Viheiser.ViUtilities.communication.reflection.handlers;

import net.runelite.client.plugins._Viheiser.ViUtilities.communication.mappings.MethodNameMapping;
import net.runelite.client.plugins._Viheiser.ViUtilities.communication.reflection.wrappers.ReflectedMethodWrapper;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodHandler {
    @Inject
    private ReflectionHandler reflectionHandler;

    public void invokeMethod(MethodNameMapping methodNameMapping, Object[] argList) throws InvocationTargetException, IllegalAccessException {
        ReflectedMethodWrapper invokeMenuActionWrapper = reflectionHandler.getMethod(methodNameMapping);
        Method insertMenuItem = invokeMenuActionWrapper.getReflectedMethod();
        Object reflectedClass = invokeMenuActionWrapper.getReflectedClass();

        insertMenuItem.setAccessible(true);
        insertMenuItem.invoke(reflectedClass, argList);
    }
}
