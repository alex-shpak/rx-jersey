package net.winterly.rxjersey.server;

import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.ConfiguredValidator;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodDispatcher;

import java.lang.reflect.InvocationHandler;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;

public abstract class RxMethodDispatcherProvider implements ResourceMethodDispatcher.Provider {

    private final ServiceLocator serviceLocator;
    private final Set<ResourceMethodDispatcher.Provider> providers;
    private final List<Class> allowedTypes;

    public RxMethodDispatcherProvider(ServiceLocator serviceLocator, Class... allowedTypes) {
        this.serviceLocator = serviceLocator;
        this.providers = getProviders(serviceLocator, getClass());
        this.allowedTypes = Arrays.asList(allowedTypes);
    }

    protected abstract ResourceMethodDispatcher create(ResourceMethodDispatcher dispatcher);

    @Override
    public ResourceMethodDispatcher create(Invocable method, InvocationHandler handler, ConfiguredValidator validator) {

        final Class<?> returnType = method.getHandlingMethod().getReturnType();
        if (!allowedTypes.contains(returnType)) {
            return null;
        }

        for (ResourceMethodDispatcher.Provider provider : providers) {
            ResourceMethodDispatcher dispatcher = provider.create(method, handler, validator);
            if (dispatcher != null) {
                dispatcher = create(dispatcher);
                serviceLocator.inject(dispatcher);
                return dispatcher;
            }
        }

        return null;
    }

    private static Set<ResourceMethodDispatcher.Provider> getProviders(ServiceLocator serviceLocator, Class<?> self) {

        Function<ServiceHandle, Boolean> filter = handle -> !handle.getActiveDescriptor()
                .getImplementationClass()
                .equals(self);

        return serviceLocator.getAllServiceHandles(ResourceMethodDispatcher.Provider.class)
                .stream()
                .filter(filter::apply)
                .map(ServiceHandle::getService)
                .collect(toSet());
    }
}
