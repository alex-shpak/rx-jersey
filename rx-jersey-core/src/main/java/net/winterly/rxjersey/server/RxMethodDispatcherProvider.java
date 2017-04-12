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

/**
 * Provider for {@link RxMethodDispatcher} that will wrap original dispatcher.
 */
public abstract class RxMethodDispatcherProvider implements ResourceMethodDispatcher.Provider {

    private final ServiceLocator serviceLocator;
    private final Set<ResourceMethodDispatcher.Provider> providers;
    private final List<Class> allowedTypes;

    /**
     * @param serviceLocator service locator
     * @param allowedTypes   list of types that supported by {@link ResourceMethodDispatcher}
     */
    public RxMethodDispatcherProvider(ServiceLocator serviceLocator, Class... allowedTypes) {
        this.serviceLocator = serviceLocator;
        this.providers = getProviders(serviceLocator, getClass());
        this.allowedTypes = Arrays.asList(allowedTypes);
    }

    /**
     * @param serviceLocator service locator
     * @param skip           type of {@link ResourceMethodDispatcher.Provider} to skip in list
     * @return other {@link ResourceMethodDispatcher.Provider}s except one marked to skip
     */
    private static Set<ResourceMethodDispatcher.Provider> getProviders(ServiceLocator serviceLocator, Class<?> skip) {

        Function<ServiceHandle, Boolean> filter = handle -> !handle.getActiveDescriptor()
                .getImplementationClass()
                .equals(skip);

        return serviceLocator.getAllServiceHandles(ResourceMethodDispatcher.Provider.class)
                .stream()
                .filter(filter::apply)
                .map(ServiceHandle::getService)
                .collect(toSet());
    }

    /**
     * Should create instance of {@link RxMethodDispatcher}
     *
     * @param dispatcher original {@link ResourceMethodDispatcher}
     * @return method dispatcher
     */
    protected abstract RxMethodDispatcher create(ResourceMethodDispatcher dispatcher);

    /**
     * Creates new {@link RxMethodDispatcher} if return type is in {@link RxMethodDispatcherProvider#allowedTypes}<br>
     * Original {@link ResourceMethodDispatcher} will be provided to created method dispatcher
     *
     * @param method    the invocable resource method.
     * @param handler   invocation handler to be used for the resource method invocation.
     * @param validator configured validator to be used for validation during resource method invocation
     * @return {@link RxMethodDispatcher}, or {@code null} if it could not be created for the given resource method.
     */
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
}
