import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import net.winterly.rxjersey.server.rxjava2.CompletableRequestInterceptor;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.junit.Test;

import javax.inject.Singleton;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
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

    @Test(expected = BadRequestException.class)
    public void shouldHandleInterceptorError() {
        target("interceptors").path("error")
                .request()
                .header("error", true)
                .get(String.class);
    }

    @Path("/interceptors")
    public static class EchoResource {

        @GET
        @Path("echo")
        public Observable<String> echo(@Context ContainerRequestContext request) {
            return Observable.just(request.getProperty("message").toString());
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
            return Single.just(requestContext).flatMapCompletable(it -> {
                it.setProperty("message", "intercepted");
                return Completable.complete();
            });
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

    public static class ErrorInterceptor implements CompletableRequestInterceptor {

        @Override
        public Completable intercept(ContainerRequestContext requestContext) {
            if (requestContext.getHeaders().containsKey("error")) {
                return Completable.error(new BadRequestException("Surprise!"));
            }
            return Completable.complete();
        }
    }

    public static class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            Stream.of(Interceptor.class, EmptyInterceptor.class, ThrowingInterceptor.class, ErrorInterceptor.class).forEach(interceptor -> {
                bind(interceptor)
                        .to(CompletableRequestInterceptor.class)
                        .in(Singleton.class);
            });

        }
    }

}
