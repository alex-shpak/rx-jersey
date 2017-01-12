package net.winterly.rx.jersey;

import net.winterly.rx.jersey.server.filter.RxRequestInterceptor;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.Test;
import rx.Observable;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import static org.junit.Assert.assertEquals;

public class InterceptorsTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config()
                .register(EchoResource.class)
                .register(new Binder());
    }

    @Test
    public void shouldInterceptRequests() {
        final String message = target("interceptors").path("echo")
                .request()
                .header("message", "hello")
                .get(String.class);

        assertEquals("hello", message);
    }

    @Path("/interceptors")
    public static class EchoResource {

        @GET
        @Path("echo")
        public Observable<String> echo(@HeaderParam("message") String message) {
            return Observable.just(message);
        }
    }

    public static class Interceptor implements RxRequestInterceptor {

        @Context
        private SecurityContext securityContext;

        @Override
        public Observable<?> filter(ContainerRequestContext requestContext) {

            return Observable.just(requestContext).doOnNext(it -> {
                it.getHeaders().putSingle("message", "intercepted");
            });
        }
    }

    public static class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(Interceptor.class).to(RxRequestInterceptor.class).in(Singleton.class);
        }
    }

}
