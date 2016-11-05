package net.winterly.rx.jersey.server;

import net.winterly.rx.jersey.server.invoker.ObservableInvocationHandler;
import net.winterly.rx.jersey.server.invoker.SingleInvocationHandler;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;
import rx.Observable;
import rx.Single;

import javax.inject.Inject;
import java.lang.reflect.InvocationHandler;

/**
 * Provides {@link InvocationHandler} for resources returning {@link Observable} or {@link Single}
 *
 * @see RxInvocationHandler
 */
public class RxInvocationHandlerProvider implements ResourceMethodInvocationHandlerProvider {

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    public InvocationHandler create(Invocable method) {
        Class type = method.getRawResponseType();

        if(Observable.class.isAssignableFrom(type)) {
            return serviceLocator.createAndInitialize(ObservableInvocationHandler.class);
        }

        if(Single.class.isAssignableFrom(type)) {
            return serviceLocator.createAndInitialize(SingleInvocationHandler.class);
        }

        return null;
    }

}
