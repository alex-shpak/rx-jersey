package net.winterly.rxjersey.client.inject;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.InjectionManager;

import java.lang.reflect.Field;

public abstract class RxJerseyBinder extends AbstractBinder {

    private final Field injectionManagerField;

    public RxJerseyBinder() {
        try {
            // Damn private properties everywhere
            injectionManagerField = AbstractBinder.class.getDeclaredField("injectionManager");
            injectionManagerField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    protected InjectionManager getInjectionManager() {
        try {
            return (InjectionManager) injectionManagerField.get(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
