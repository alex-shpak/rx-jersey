import io.reactivex.Flowable;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.junit.Assert.assertEquals;

public class FlowableResourceTest extends RxJerseyTest {

    @Test
    public void shouldReturnContent() {
        ObservableResource resource = target(ObservableResource.class);
        String message = resource.echo("hello").blockingFirst();

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        ObservableResource resource = target(ObservableResource.class);
        String message = resource.empty().blockingFirst();

        assertEquals("", message);
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleError() {
        ObservableResource resource = target(ObservableResource.class);
        String message = resource.error().blockingFirst();

        assertEquals("", message);
    }

    @Path("/endpoint")
    public interface ObservableResource {

        @GET
        @Path("echo")
        Flowable<String> echo(@QueryParam("message") String message);

        @GET
        @Path("empty")
        Flowable<String> empty();

        @GET
        @Path("error")
        Flowable<String> error();
    }
}
