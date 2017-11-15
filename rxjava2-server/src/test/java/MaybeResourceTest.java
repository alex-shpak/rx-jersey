import io.reactivex.Maybe;
import org.junit.Test;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class MaybeResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(MaybeResource.class);
    }

    @Test
    public void shouldReturnContent() {
        final String message = target("maybe").path("echo")
                .queryParam("message", "hello")
                .request()
                .get(String.class);

        assertEquals("hello", message);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowOnErrorObservable() {
        target("maybe").path("error")
                .request()
                .get(String.class);
    }

    @Test
    public void shouldReturnNoContentOnEmptyObservable() {
        final int status = target("maybe").path("empty")
                .request()
                .get()
                .getStatus();

        assertEquals(204, status);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowOnNullObservable() {
        target("maybe").path("npe")
                .request()
                .get(String.class);
    }

    @Path("/maybe")
    public static class MaybeResource {

        @GET
        @Path("echo")
        public Maybe<String> echo(@QueryParam("message") String message) {
            return Maybe.just(message);
        }

        @GET
        @Path("error")
        public Maybe<String> error() {
            return Maybe.error(new BadRequestException());
        }

        @GET
        @Path("npe")
        public Maybe<String> npe() {
            return null;
        }

        @GET
        @Path("empty")
        public Maybe<String> empty() {
            return Maybe.empty();
        }

    }
}
