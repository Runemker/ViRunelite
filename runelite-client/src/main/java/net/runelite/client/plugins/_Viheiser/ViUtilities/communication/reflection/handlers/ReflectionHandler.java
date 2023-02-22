package net.runelite.client.plugins._Viheiser.ViUtilities.communication.reflection.handlers;

import net.runelite.api.Client;
import net.runelite.client.plugins._Viheiser.ViUtilities.communication.mappings.ClassNameMapping;
import net.runelite.client.plugins._Viheiser.ViUtilities.communication.mappings.FieldNameMapping;
import net.runelite.client.plugins._Viheiser.ViUtilities.communication.mappings.MethodNameMapping;
import net.runelite.client.plugins._Viheiser.ViUtilities.communication.reflection.wrappers.ReflectedFieldWrapper;
import net.runelite.client.plugins._Viheiser.ViUtilities.communication.reflection.wrappers.ReflectedMethodWrapper;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionHandler {
    @Inject
    public Client client;
    public ReflectedMethodWrapper getMethod(MethodNameMapping method){
        try {
            Class<?> obfClass = getClass(method.getClassReference());
            Method obfMethod = obfClass.getDeclaredMethod(method.getObfuscatedName(), method.getArgumentTypes());
            return new ReflectedMethodWrapper(obfClass, obfMethod);
        }
        catch (Exception e){
            System.err.println(writeErrorMessage(e));
        }

        return null;
    }

    public ReflectedFieldWrapper getField(FieldNameMapping field){
        try {
            Class<?> obfClass = getClass(field.getClassReference());
            Field obfField = obfClass.getDeclaredField(field.getObfuscatedName());
            return new ReflectedFieldWrapper(obfClass, obfField);
        }
        catch (Exception e){
            System.err.println(writeErrorMessage(e));
        }

        return null;
    }

    public Class<?> getClass(ClassNameMapping classNameMapping) throws ClassNotFoundException {
        return Class.forName(classNameMapping.getObfuscatedName(), true, client.getClass().getClassLoader());
    }

    private String writeErrorMessage(Exception e) {
        return "Failed to get class or method: " + e.getMessage();
    }
}
