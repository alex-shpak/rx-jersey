import org.junit.Test;
import rx.Observable;

import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class ObservableResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(ObservableResource.class);
    }

    @Test
    public void shouldReturnContent() {
        final String message = target("observable").path("echo")
                .queryParam("message", "hello")
                .request()
                .get(String.class);

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnEmptyObservable() {
        final int status = target("observable").path("empty")
                .request()
                .get()
                .getStatus();

        assertEquals(204, status);
    }

    @Test
    public void shouldReturnNoContentOnNullEntity() {
        final int status = target("observable").path("nullable")
                .request()
                .get()
                .getStatus();

        assertEquals(204, status);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowOnNullObservable() {
        target("observable").path("npe")
                .request()
                .get(String.class);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowOnMultipleEntities() {
        target("observable").path("multiple")
                .request()
                .get(String.class);
    }


    @Path("/observable")
    public static class ObservableResource {

        @GET
        @Path("echo")
        public Observable<String> echo(@QueryParam("message") String message) {
            return Observable.just(message);
        }

        @GET
        @Path("empty")
        public Observable<String> empty() {
            return Observable.empty();
        }

        @GET
        @Path("nullable")
        public Observable<String> nullable() {
            return Observable.just(null);
        }

        @GET
        @Path("npe")
        public Observable<String> npe() {
            return null;
        }

        @GET
        @Path("multiple")
        public Observable<String> multiple() {
            return Observable.just("hello", "rx");
        }

    }
}
