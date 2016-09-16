package net.winterly.rx.jersey.inject;

import net.winterly.rx.jersey.WebResourceFactoryPatched;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.RxWebTarget;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RemoteResolver implements InjectionResolver<Remote> {

    @Inject
    private RxClient rxClient;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> root) {
        Remote remote = injectee.getParent().getAnnotation(Remote.class);

        RxWebTarget rxWebTarget = rxClient.target(remote.value());
        Class<?> type = (Class<?>) injectee.getRequiredType();
        if(RxWebTarget.class.isAssignableFrom(type)) {
            return rxWebTarget;
        }

        if(type.isInterface()) {
            return WebResourceFactoryPatched.newResource(type, rxWebTarget);
        }

        throw new IllegalStateException(String.format("Can't find proper injection for %s", type));
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
