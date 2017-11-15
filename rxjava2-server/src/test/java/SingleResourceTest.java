import io.reactivex.Single;
import org.junit.Test;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class SingleResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(SingleResource.class);
    }

    @Test
    public void shouldReturnContent() {
        final String message = target("single").path("echo")
                .queryParam("message", "hello")
                .request()
                .get(String.class);

        assertEquals("hello", message);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowOnErrorObservable() {
        target("single").path("error")
                .request()
                .get(String.class);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowOnNullObservable() {
        target("single").path("npe")
                .request()
                .get(String.class);
    }

    @Path("/single")
    public static class SingleResource {

        @GET
        @Path("echo")
        public Single<String> echo(@QueryParam("message") String message) {
            return Single.just(message);
        }

        @GET
        @Path("error")
        public Single<String> error() {
            return Single.error(new BadRequestException());
        }

        @GET
        @Path("npe")
        public Single<String> npe() {
            return null;
        }

    }
}
