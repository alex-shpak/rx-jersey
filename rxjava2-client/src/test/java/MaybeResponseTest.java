import io.reactivex.Maybe;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class MaybeResponseTest extends RxJerseyTest {

    @Test
    public void shouldHandleEmpty() {
        Resource resource = target(Resource.class);
        Response response = resource.empty().blockingGet();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void shouldParseEntity() {
        Resource resource = target(Resource.class);
        Response response = resource.json("hello").blockingGet();

        assertEquals(response.readEntity(Entity.class).message, "hello");
    }

    @Test
    public void shouldHandleError() {
        Resource resource = target(Resource.class);
        Response response = resource.error().blockingGet();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Path("/endpoint")
    public interface Resource {

        @GET
        @Path("empty")
        Maybe<Response> empty();

        @GET
        @Path("json")
        Maybe<Response> json(@QueryParam("message") String message);

        @GET
        @Path("error")
        Maybe<Response> error();
    }
}
