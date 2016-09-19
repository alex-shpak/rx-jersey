package net.winterly.rx.jersey.inject;

import net.winterly.rx.jersey.WebResourceFactoryPatched;
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

import static java.lang.String.format;

@Singleton
public class RemoteResolver implements InjectionResolver<Remote> {

    @Inject
    private RxClient rxClient;

    @Inject
    private ServiceLocator serviceLocator;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> root) {
        Remote remote = injectee.getParent().getAnnotation(Remote.class);
        UriInfo uriInfo = serviceLocator.getService(UriInfo.class);

        String remoteValue = remote.value();
        String target = remoteValue.startsWith(Remote.DEFAULT) ? format("%s/%s", uriInfo.getBaseUri(), remoteValue) : remoteValue;
        target = UriBuilder.fromPath(target).toString();

        RxWebTarget rxWebTarget = rxClient.target(target);
        Class type = (Class) injectee.getRequiredType();

        if(RxWebTarget.class.isAssignableFrom(type)) {
            return rxWebTarget;
        }

        if(type.isInterface()) {
            return WebResourceFactoryPatched.newResource(type, rxWebTarget);
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
