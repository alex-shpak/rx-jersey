import io.reactivex.Completable;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompletableResourceTest extends RxJerseyTest {

    @Test
    public void shouldReturnContent() {
        ObservableResource resource = target(ObservableResource.class);
        boolean completed = resource.echo("hello").blockingAwait(5, TimeUnit.SECONDS);

        assertTrue(completed);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        ObservableResource resource = target(ObservableResource.class);
        boolean completed = resource.empty().blockingAwait(5, TimeUnit.SECONDS);

        assertTrue(completed);
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleError() {
        ObservableResource resource = target(ObservableResource.class);
        boolean completed = resource.error().blockingAwait(5, TimeUnit.SECONDS);

        assertTrue(completed);
    }

    @Path("/endpoint")
    public interface ObservableResource {

        @GET
        @Path("echo")
        Completable echo(@QueryParam("message") String message);

        @GET
        @Path("empty")
        Completable empty();

        @GET
        @Path("error")
        Completable error();
    }
}
