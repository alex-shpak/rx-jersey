import org.junit.Test;
import rx.Single;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.ResponseProcessingException;

import static org.junit.Assert.assertEquals;

public class SingleResourceTest extends RxJerseyTest {

    @Test
    public void shouldReturnContent() {
        SingleResource resource = resource(SingleResource.class);
        String message = resource.echo("hello").toBlocking().value();

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        SingleResource resource = resource(SingleResource.class);
        String message = resource.empty().toBlocking().value();

        assertEquals("", message);
    }

    @Test(expected = ResponseProcessingException.class)
    public void shouldHandleError() {
        SingleResource resource = resource(SingleResource.class);
        String message = resource.error().toBlocking().value();

        assertEquals("", message);
    }

    @Path("/endpoint")
    public interface SingleResource {

        @GET
        @Path("echo")
        Single<String> echo(@QueryParam("message") String message);

        @GET
        @Path("empty")
        Single<String> empty();

        @GET
        @Path("error")
        Single<String> error();
    }
}
