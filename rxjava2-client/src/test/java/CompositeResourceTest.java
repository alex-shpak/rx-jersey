import io.reactivex.Flowable;
import net.winterly.rxjersey.client.WebResourceFactory;
import net.winterly.rxjersey.client.rxjava2.CompositeClientMethodInvoker;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class CompositeResourceTest extends RxJerseyTest {

    @Override
    protected <T> T target(final Class<T> resource) {
        return WebResourceFactory.newResource(resource, target(), new CompositeClientMethodInvoker());
    }

    @Test
    public void shouldReturnContent() {
        final CompositeResource resource = target(CompositeResource.class);
        final String message = resource.echo("hello").blockingFirst();

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        final CompositeResource resource = target(CompositeResource.class);
        final String message = resource.empty().blockingFirst();

        assertEquals("", message);
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleError() {
        final CompositeResource resource = target(CompositeResource.class);
        final String message = resource.error().blockingFirst();

        assertEquals("", message);
    }

    @Test
    public void shouldReturnContentForNonRxType() {
        final CompositeResource resource = target(CompositeResource.class);
        final String message = resource.string();

        assertEquals("string", message);
    }

    @Test
    public void shouldReturnContentForNonRxResponse() {
        final CompositeResource resource = target(CompositeResource.class);
        final Response response = resource.json("message");

        assertEquals("message", response.readEntity(Entity.class).message);
    }

    @Path("/endpoint")
    public interface CompositeResource {

        @GET
        @Path("echo")
        Flowable<String> echo(@QueryParam("message") String message);

        @GET
        @Path("empty")
        Flowable<String> empty();

        @GET
        @Path("error")
        Flowable<String> error();

        @GET
        @Path("string")
        String string();

        @GET
        @Path("json")
        Response json(@QueryParam("message") String message);
    }
}
