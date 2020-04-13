import org.junit.Test;
import rx.Observable;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.junit.Assert.assertEquals;

public class ObservableResourceTest extends RxJerseyTest {

    @Test
    public void shouldReturnContent() {
        ObservableResource resource = target(ObservableResource.class);
        String message = resource.echo("hello").toBlocking().first();

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        ObservableResource resource = target(ObservableResource.class);
        String message = resource.empty().toBlocking().first();

        assertEquals("", message);
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleError() {
        ObservableResource resource = target(ObservableResource.class);
        String message = resource.error().toBlocking().first();

        assertEquals("", message);
    }

    @Path("/endpoint")
    public interface ObservableResource {

        @GET
        @Path("echo")
        Observable<String> echo(@QueryParam("message") String message);

        @GET
        @Path("empty")
        Observable<String> empty();

        @GET
        @Path("error")
        Observable<String> error();
    }
}
