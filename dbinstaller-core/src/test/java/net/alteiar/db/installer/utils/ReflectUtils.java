package net.alteiar.db.installer.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectUtils {

  public static Field getField(Object obj, String fieldName) {

    Field fieldFound = null;
    Class<?> currentClass = obj.getClass();

    try {

      while (currentClass != null) {

        try {

          fieldFound = currentClass.getDeclaredField(fieldName);
          currentClass = null;
        } catch (NoSuchFieldException ex) {

          currentClass = currentClass.getSuperclass();
        }
      }
    } catch (SecurityException e) {

      throw new RuntimeException("Unexpected security exception", e);
    }

    if (fieldFound == null) {

      throw new RuntimeException(String.format("Field %s is not found in object of class %s", fieldName,
                                               obj.getClass()));
    }

    return fieldFound;
  }

  public static void setField(Object obj, String fieldName, Object value) {

    Field field = getField(obj, fieldName);

    boolean isAccessible = field.isAccessible();

    field.setAccessible(true);

    try {

      int modifier = removeFinalModifier(field);
      field.set(obj, value);
      restoreFinalModifier(field, modifier);

    } catch (IllegalArgumentException | IllegalAccessException e) {

      throw new RuntimeException("Unexpected exception", e);
    }

    field.setAccessible(isAccessible);
  }

  private static int removeFinalModifier(Field field) {

    int modifier = -1;
    try {

      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);

      modifier = modifiersField.getInt(field);
      modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {

      throw new RuntimeException("Unexpected exception", e);
    }

    return modifier;
  }

  private static void restoreFinalModifier(Field field, int orgValue) {

    try {

      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, orgValue);
      modifiersField.setAccessible(false);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {

      throw new RuntimeException("Unexpected exception", e);
    }
  }
}
