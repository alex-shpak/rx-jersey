import io.reactivex.Maybe;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class ResponseResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(ResponseResource.class);
    }

    @Test
    public void shouldHandleResponseReturnType() {
        final String message = target("response")
                .path("hello")
                .request()
                .get(String.class);

        assertEquals("hello", message);
    }


    @Path("/response")
    public static class ResponseResource {

        @GET
        @Path("hello")
        public Maybe<Response> get() {
            return Maybe.just(Response.ok("hello").build());
        }

    }

}
