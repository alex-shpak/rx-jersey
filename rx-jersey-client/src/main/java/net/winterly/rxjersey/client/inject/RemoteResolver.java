package net.winterly.rxjersey.client.inject;

import net.winterly.rxjersey.client.WebResourceFactoryPatched;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.RxWebTarget;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;

import static java.lang.String.format;

/**
 * Injection resolver for RxWebTarget and Proxy client instances based on value of {@link Remote} annotation
 *
 * @see Remote
 */
@Singleton
public class RemoteResolver implements InjectionResolver<Remote> {

    @Inject
    private RxClient rxClient;

    @Inject
    private ServiceLocator serviceLocator;

    public static URI merge(String value, UriInfo uriInfo) {
        URI target = URI.create(value);
        if (target.getHost() == null) {
            target = UriBuilder.fromUri(uriInfo.getBaseUri()).uri(target).build();
        }

        return target;
    }

    /**
     * @return RxWebTarget or Proxy client of specified injectee interface
     * @throws IllegalStateException if uri is not correct or there is no sufficient injection resolved
     */
    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> root) {
        Remote remote = injectee.getParent().getAnnotation(Remote.class);
        UriInfo uriInfo = serviceLocator.getService(UriInfo.class);

        URI target = merge(remote.value(), uriInfo);

        RxWebTarget rxWebTarget = rxClient.target(target);
        Type type = injectee.getRequiredType();

        if (type instanceof ParameterizedType) {
            return rxWebTarget;
        }

        if (type instanceof Class) {
            Class<?> resource = (Class) type;
            if (resource.isInterface()) {
                return WebResourceFactoryPatched.newResource(resource, rxWebTarget);
            }
        }

        throw new IllegalStateException(format("Can't find proper injection for %s", type));
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return true;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return false;
    }
}
