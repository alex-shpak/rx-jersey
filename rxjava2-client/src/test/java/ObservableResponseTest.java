import io.reactivex.Observable;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class ObservableResponseTest extends RxJerseyTest {

    @Test
    public void shouldHandleEmpty() {
        Resource resource = target(Resource.class);
        Response response = resource.empty().blockingFirst();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void shouldParseEntity() {
        Resource resource = target(Resource.class);
        Response response = resource.json("hello").blockingFirst();

        assertEquals(response.readEntity(Entity.class).message, "hello");
    }

    @Test
    public void shouldHandleError() {
        Resource resource = target(Resource.class);
        Response response = resource.error().blockingFirst();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Path("/endpoint")
    public interface Resource {

        @GET
        @Path("empty")
        Observable<Response> empty();

        @GET
        @Path("json")
        Observable<Response> json(@QueryParam("message") String message);

        @GET
        @Path("error")
        Observable<Response> error();
    }
}
