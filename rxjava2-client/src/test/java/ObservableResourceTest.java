import io.reactivex.Observable;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.junit.Assert.assertEquals;

public class ObservableResourceTest extends RxJerseyTest {

    @Test
    public void shouldReturnContent() {
        Resource resource = target(Resource.class);
        String message = resource.echo("hello").blockingFirst();

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        Resource resource = target(Resource.class);
        String message = resource.empty().blockingFirst();

        assertEquals("", message);
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleError() {
        Resource resource = target(Resource.class);
        String message = resource.error().blockingFirst();

        assertEquals("", message);
    }

    @Path("/endpoint")
    public interface Resource {

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
