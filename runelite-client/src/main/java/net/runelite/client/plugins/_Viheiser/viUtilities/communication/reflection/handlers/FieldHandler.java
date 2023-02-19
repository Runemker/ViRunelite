package net.runelite.client.plugins._Viheiser.viUtilities.communication.reflection.handlers;

import net.runelite.api.Client;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.mappings.FieldNameMapping;
import net.runelite.client.plugins._Viheiser.viUtilities.communication.reflection.wrappers.ReflectedFieldWrapper;

import javax.inject.Inject;
import javax.management.ReflectionException;
import java.lang.reflect.Field;

public class FieldHandler {
    @Inject
    private ReflectionHandler reflectionHandler;
    @Inject
    private Client client;

    public void setField(FieldNameMapping fieldNameMapping, Object value) throws ReflectionException {
        ReflectedFieldWrapper fieldWrapper = reflectionHandler.getField(fieldNameMapping);
        Field reflectedField = fieldWrapper.getReflectedField();
        Object reflectedClass = fieldWrapper.getReflectedClass();
        reflectedField.setAccessible(true);

        try {
            setPrimitiveFieldValue(reflectedField, reflectedClass, value);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new ReflectionException(e, "Error setting field");
        } catch (Exception e) {
            throw new ReflectionException(e, "Error setting field");
        }
    }

    public void setField(FieldNameMapping fieldNameMapping, Object value, Object instance) throws ReflectionException {
        ReflectedFieldWrapper fieldWrapper = reflectionHandler.getField(fieldNameMapping);
        Field reflectedField = fieldWrapper.getReflectedField();
        reflectedField.setAccessible(true);

        try {
            setPrimitiveFieldValue(reflectedField, instance, value);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new ReflectionException(e, "Error setting field");
        } catch (Exception e) {
            throw new ReflectionException(e, "Error setting field");
        }
    }

    public int getIntField(FieldNameMapping fieldNameMapping) throws ReflectionException {
        ReflectedFieldWrapper fieldWrapper = reflectionHandler.getField(fieldNameMapping);
        try {
            Field reflectedField = fieldWrapper.getReflectedField();
            Object reflectedClass = fieldWrapper.getReflectedClass();
            reflectedField.setAccessible(true);
            return (int) reflectedField.get(reflectedClass);
        } catch (IllegalAccessException | ClassCastException e) {
            throw new ReflectionException(e, "Error getting integer field");
        } catch (Exception e) {
            throw new ReflectionException(e, "Error getting integer field");
        }
    }

    //This is for non-static int such as int fields inside player etc, you need the instance of player to get a hold of this
    public int getIntField(FieldNameMapping fieldNameMapping, Object instance) throws ReflectionException {
        ReflectedFieldWrapper fieldWrapper = reflectionHandler.getField(fieldNameMapping);
        try {
            Field reflectedField = fieldWrapper.getReflectedField();
            reflectedField.setAccessible(true);
            return (int) reflectedField.get(instance);
        } catch (IllegalAccessException | ClassCastException e) {
            throw new ReflectionException(e, "Error getting integer field");
        } catch (Exception e) {
            throw new ReflectionException(e, "Error getting integer field");
        }
    }


    public String getStringField(FieldNameMapping fieldNameMapping) throws ReflectionException {
        ReflectedFieldWrapper fieldWrapper = reflectionHandler.getField(fieldNameMapping);
        try {
            Field reflectedField = fieldWrapper.getReflectedField();
            Object reflectedClass = fieldWrapper.getReflectedClass();
            reflectedField.setAccessible(true);
            return (String) reflectedField.get(reflectedClass);
        } catch (IllegalAccessException | ClassCastException e) {
            throw new ReflectionException(e, "Error getting string field");
        } catch (Exception e) {
            throw new ReflectionException(e, "Error getting string field");
        }
    }

    public Object getField(FieldNameMapping fieldNameMapping) throws ReflectionException {
        ReflectedFieldWrapper fieldWrapper = reflectionHandler.getField(fieldNameMapping);
        try {
            Field reflectedField = fieldWrapper.getReflectedField();
            Object reflectedClass = fieldWrapper.getReflectedClass();
            reflectedField.setAccessible(true);
            return reflectedField.get(reflectedClass);
        } catch (IllegalAccessException | ClassCastException e) {
            throw new ReflectionException(e, "Error getting object field");
        } catch (Exception e) {
            throw new ReflectionException(e, "Error getting object field");
        }
    }

    public Object getField(FieldNameMapping fieldNameMapping, Object instance) throws ReflectionException {
        ReflectedFieldWrapper fieldWrapper = reflectionHandler.getField(fieldNameMapping);
        try {
            Field reflectedField = fieldWrapper.getReflectedField();
            reflectedField.setAccessible(true);
            return reflectedField.get(instance);
        } catch (IllegalAccessException | ClassCastException e) {
            throw new ReflectionException(e, "Error getting object field");
        } catch (Exception e) {
            throw new ReflectionException(e, "Error getting object field");
        }
    }

    public String getStringField(FieldNameMapping fieldNameMapping, Object instance) throws ReflectionException {
        ReflectedFieldWrapper fieldWrapper = reflectionHandler.getField(fieldNameMapping);
        try {
            Field reflectedField = fieldWrapper.getReflectedField();
            reflectedField.setAccessible(true);
            return (String) reflectedField.get(instance);
        } catch (IllegalAccessException | ClassCastException e) {
            throw new ReflectionException(e, "Error getting string field");
        } catch (Exception e) {
            throw new ReflectionException(e, "Error getting string field");
        }
    }


    public boolean getBooleanField(FieldNameMapping fieldNameMapping) throws ReflectionException {
        ReflectedFieldWrapper fieldWrapper = reflectionHandler.getField(fieldNameMapping);
        try {
            Field reflectedField = fieldWrapper.getReflectedField();
            Object reflectedClass = fieldWrapper.getReflectedClass();
            reflectedField.setAccessible(true);
            return (boolean) reflectedField.get(reflectedClass);
        } catch (IllegalAccessException | ClassCastException e) {
            throw new ReflectionException(e, "Error getting boolean field");
        } catch (Exception e) {
            throw new ReflectionException(e, "Error getting boolean field");
        }
    }

    public boolean getBooleanField(FieldNameMapping fieldNameMapping, Object instance) throws ReflectionException {
        ReflectedFieldWrapper fieldWrapper = reflectionHandler.getField(fieldNameMapping);
        try {
            Field reflectedField = fieldWrapper.getReflectedField();
            reflectedField.setAccessible(true);
            return (boolean) reflectedField.get(instance);
        } catch (IllegalAccessException | ClassCastException e) {
            throw new ReflectionException(e, "Error getting boolean field");
        } catch (Exception e) {
            throw new ReflectionException(e, "Error getting boolean field");
        }
    }

    private void setPrimitiveFieldValue(Field reflectedField, Object reflectedClass, Object value) throws IllegalAccessException {
        Class<?> type = reflectedField.getType();
        switch (type.getSimpleName()) {
            case "int":
                reflectedField.setInt(reflectedClass, (int) value);
                break;
            case "boolean":
                reflectedField.setBoolean(reflectedClass, (boolean) value);
                break;
            case "double":
                reflectedField.setDouble(reflectedClass, (double) value);
                break;
            case "float":
                reflectedField.setFloat(reflectedClass, (float) value);
                break;
            case "long":
                reflectedField.setLong(reflectedClass, (long) value);
                break;
            case "short":
                reflectedField.setShort(reflectedClass, (short) value);
                break;
            case "byte":
                reflectedField.setByte(reflectedClass, (byte) value);
                break;
            default:
                reflectedField.set(reflectedClass, value);
        }
    }


}
