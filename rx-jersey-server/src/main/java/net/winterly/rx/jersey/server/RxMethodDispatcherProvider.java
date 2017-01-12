package net.winterly.rx.jersey.server;

import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.ConfiguredValidator;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodDispatcher;

import javax.inject.Inject;
import java.lang.reflect.InvocationHandler;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;
import static net.winterly.rx.jersey.server.RxJerseyServerFeature.isRx;

public class RxMethodDispatcherProvider implements ResourceMethodDispatcher.Provider {

    private final ServiceLocator serviceLocator;
    private final Set<ResourceMethodDispatcher.Provider> providers;

    @Inject
    public RxMethodDispatcherProvider(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
        this.providers = getProviders(serviceLocator);
    }

    @Override
    public ResourceMethodDispatcher create(Invocable method, InvocationHandler handler, ConfiguredValidator validator) {

        final Class<?> returnType = method.getHandlingMethod().getReturnType();
        if (!isRx(returnType)) {
            return null;
        }

        for (ResourceMethodDispatcher.Provider provider : providers) {
            ResourceMethodDispatcher dispatcher = provider.create(method, handler, validator);
            if (dispatcher != null) {
                dispatcher = new RxMethodDispatcher(dispatcher);
                serviceLocator.inject(dispatcher);
                return dispatcher;
            }
        }

        return null;
    }

    private static Set<ResourceMethodDispatcher.Provider> getProviders(ServiceLocator serviceLocator) {

        Function<ServiceHandle, Boolean> filter = handle -> !handle.getActiveDescriptor()
                .getImplementationClass()
                .equals(RxMethodDispatcherProvider.class);

        return serviceLocator.getAllServiceHandles(ResourceMethodDispatcher.Provider.class)
                .stream()
                .filter(filter::apply)
                .map(ServiceHandle::getService)
                .collect(toSet());
    }
}
