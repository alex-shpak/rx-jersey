import org.junit.Test;
import rx.Completable;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class CompletableResourceTest extends RxJerseyTest {

    @Test
    public void shouldReturnNoContentOnNull() {
        CompletableResource resource = target(CompletableResource.class);
        boolean completed = resource.empty().await(5, TimeUnit.SECONDS);

        assertEquals(completed, true);
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleError() {
        CompletableResource resource = target(CompletableResource.class);
        boolean completed = resource.error().await(5, TimeUnit.SECONDS);

        assertEquals(completed, true);
    }

    @Path("/endpoint")
    public interface CompletableResource {

        @GET
        @Path("empty")
        Completable empty();

        @GET
        @Path("error")
        Completable error();
    }
}
