package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field decField = target.getClass().getDeclaredField(fieldName);

            if(!decField.isAccessible()) {
                decField.setAccessible(true);
                wasPrivate = true;
            }
            decField.set(target, toInject);
            if(wasPrivate) {
                decField.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
