package net.winterly.rx.jersey.server;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;
import rx.Observable;

import javax.inject.Inject;
import java.lang.reflect.InvocationHandler;

/**
 * Provides {@link InvocationHandler} for resources returning {@link Observable}
 *
 * @see RxInvocationHandler
 */
public class RxInvocationHandlerProvider implements ResourceMethodInvocationHandlerProvider {

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    public InvocationHandler create(Invocable method) {
        if(!isRx(method)) {
            return null;
        }

        return serviceLocator.createAndInitialize(RxInvocationHandler.class);
    }

    private static boolean isRx(Invocable method) {
        return Observable.class.isAssignableFrom(method.getRawResponseType());
    }
}
