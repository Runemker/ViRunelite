package net.runelite.client.plugins._Viheiser.viUtilities.communication.reflection;

import net.runelite.client.plugins._Viheiser.viUtilities.communication.reflection.handlers.FieldHandler;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.reflection.handlers.MethodHandler;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.mappings.MethodNameMapping;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.mappings.FieldNameMapping;

import javax.inject.Inject;
import javax.management.ReflectionException;

public class ReflectionManager {
    @Inject
    public MethodHandler methodHandler;
    @Inject
    public FieldHandler fieldHandler;

    public void invokeMenuAction(MethodNameMapping methodNameMapping, Object[] argList) {
        try{
            methodHandler.invokeMethod(methodNameMapping, argList);
        } catch (Exception e){
            System.err.println(methodErrorMessage(methodNameMapping.getReadableName()));
        }
    }

    public void insertMenuEntry(MethodNameMapping methodNameMapping, Object[] argList) {
        try{
            methodHandler.invokeMethod(methodNameMapping, argList);
        } catch (Exception e){
            System.err.println(methodErrorMessage(methodNameMapping.getReadableName()));
        }
    }

    public int getIntField(FieldNameMapping fieldName) {
        try {
            return fieldHandler.getIntField(fieldName);
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }
    }

    public int getIntField(FieldNameMapping fieldName, Object instance) {
        try {
            return fieldHandler.getIntField(fieldName, instance);
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getBooleanField(FieldNameMapping fieldName) {
        try {
            return fieldHandler.getBooleanField(fieldName);
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getBooleanField(FieldNameMapping fieldName, Object instance) {
        try {
            return fieldHandler.getBooleanField(fieldName, instance);
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }
    }


    public String getStringField(FieldNameMapping fieldName) {
        try {
            return fieldHandler.getStringField(fieldName);
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStringField(FieldNameMapping fieldName, Object instance) {
        try {
            return fieldHandler.getStringField(fieldName, instance);
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFieldValue(FieldNameMapping fieldNameMapping, Object value){
        try {
            fieldHandler.setField(fieldNameMapping, value);
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFieldValue(FieldNameMapping fieldNameMapping, Object value, Object instance){
        try {
            fieldHandler.setField(fieldNameMapping, value, instance);
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }
    }

    private String methodErrorMessage(String methodName){
        return "Failed when invoking method: " + methodName;
    }


}
