package net.winterly.rx.jersey;

import org.junit.Test;
import rx.Observable;
import rx.Single;

import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class SingleResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(SingleResource.class);
    }

    @Test
    public void shouldReturnContent() {
        final String message = target("echo")
                .queryParam("message", "hello")
                .request()
                .get(String.class);

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNullEntity() {
        final int status = target("nullable")
                .request()
                .get()
                .getStatus();

        assertEquals(204, status);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowOnNullObservable() {
        target("npe")
                .request()
                .get(String.class);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowOnMultipleEntities() {
        target("multiple")
                .request()
                .get(String.class);
    }

    @Path("/")
    public static class SingleResource {

        @GET
        @Path("echo")
        public Single<String> echo(@QueryParam("message") String message) {
            return Single.just(message);
        }

        @GET
        @Path("nullable")
        public Single<String> nullable() {
            return Single.just(null);
        }

        @GET
        @Path("npe")
        public Single<String> npe() {
            return null;
        }

        @GET
        @Path("multiple")
        public Single<String> multiple() {
            return Observable.just("hello", "rx").toSingle();
        }

    }
}
