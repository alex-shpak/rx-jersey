import net.winterly.rxjersey.client.WebResourceFactory;
import net.winterly.rxjersey.client.rxjava2.SyncClientMethodInvoker;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class SyncResourceTest extends RxJerseyTest {

    @Override
    protected <T> T target(Class<T> resource) {
        return WebResourceFactory.newResource(resource, target(), new SyncClientMethodInvoker());
    }

    @Test
    public void shouldReturnContentForResponse() {
        SyncResource resource = target(SyncResource.class);
        Response response = resource.echo("hello");

        assertEquals("hello", response.readEntity(String.class));
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        SyncResource resource = target(SyncResource.class);
        String message = resource.empty();

        assertEquals("", message);
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleError() {
        SyncResource resource = target(SyncResource.class);
        String message = resource.error();

        assertEquals("", message);
    }

    @Test
    public void shouldReturnJsonContent() {
        SyncResource resource = target(SyncResource.class);
        Entity entity = resource.json("message");

        assertEquals("message", entity.message);
    }


    @Path("/endpoint")
    public interface SyncResource {

        @GET
        @Path("echo")
        Response echo(@QueryParam("message") String message);

        @GET
        @Path("empty")
        String empty();

        @GET
        @Path("error")
        String error();

        @GET
        @Path("json")
        Entity json(@QueryParam("message") String message);
    }

}
