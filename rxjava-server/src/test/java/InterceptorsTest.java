import net.winterly.rxjersey.server.rxjava.CompletableRequestInterceptor;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.junit.Test;
import rx.Completable;
import rx.Observable;
import rx.Single;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.stream.Stream;

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

        assertEquals("intercepted", message);
    }

    @Test(expected = NotAuthorizedException.class)
    public void shouldHandleInterceptorException() {
        target("interceptors").path("error")
                .request()
                .header("throw", true)
                .get(String.class);
    }

    @Path("/interceptors")
    public static class EchoResource {

        @GET
        @Path("echo")
        public Observable<String> echo(@HeaderParam("message") String message) {
            return Observable.just(message);
        }

        @GET
        @Path("error")
        public Observable<String> error() {
            return Observable.just(null);
        }
    }

    public static class Interceptor implements CompletableRequestInterceptor {

        @Context
        private SecurityContext securityContext;

        @Override
        public Completable intercept(ContainerRequestContext requestContext) {
            return Single.just(requestContext).doOnSuccess(it -> {
                it.getHeaders().putSingle("message", "intercepted");
            }).toCompletable();
        }
    }

    public static class ThrowingInterceptor implements CompletableRequestInterceptor {

        @Override
        public Completable intercept(ContainerRequestContext requestContext) {
            if (requestContext.getHeaders().containsKey("throw")) {
                throw new NotAuthorizedException("Surprise!");
            }
            return Completable.complete();
        }
    }

    public static class EmptyInterceptor implements CompletableRequestInterceptor {

        @Override
        public Completable intercept(ContainerRequestContext requestContext) {
            return Completable.complete();
        }
    }

    public static class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            Stream.of(Interceptor.class, EmptyInterceptor.class, ThrowingInterceptor.class).forEach(interceptor -> {
                bind(interceptor)
                        .to(CompletableRequestInterceptor.class)
                        .in(Singleton.class);
            });

        }
    }

}
