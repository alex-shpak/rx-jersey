package net.winterly.rxjersey.server.rxjava2;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.util.LinkedList;
import java.util.List;

/**
 * See <a href="https://github.com/alex-shpak/rx-jersey">Github Repository</a>
 */
public final class RxJerseyServerFeature implements Feature {

    private final List<Class<? extends CompletableRequestInterceptor>> interceptors = new LinkedList<>();

    public RxJerseyServerFeature register(Class<? extends CompletableRequestInterceptor> interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    @Override
    public boolean configure(FeatureContext context) {
        context.register(RxBodyWriter.class);
        context.register(new Binder());
        return true;
    }

    private class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(MaybeInvocationHandlerProvider.class)
                    .to(ResourceMethodInvocationHandlerProvider.class)
                    .in(Singleton.class);

            interceptors.forEach(interceptor -> bind(interceptor)
                    .to(CompletableRequestInterceptor.class)
                    .in(Singleton.class)
            );
        }
    }
}
