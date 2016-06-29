package net.winterly.rx.jersey;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public final class RxJerseyFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(RxBodyWriter.class);
        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(RxInvocationHandlerProvider.class).to(ResourceMethodInvocationHandlerProvider.class).in(Singleton.class);
            }
        });
        return true;
    }

}
