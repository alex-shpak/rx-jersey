package net.winterly.rxjersey;

import org.junit.Test;
import rx.Completable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.ResponseProcessingException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class CompletableResourceTest extends RxJerseyTest {

    @Test
    public void shouldReturnNoContentOnNull() {
        CompletableResource resource = resource(CompletableResource.class);
        boolean completed = resource.empty().await(5, TimeUnit.SECONDS);

        assertEquals(completed, true);
    }

    @Test(expected = ResponseProcessingException.class)
    public void shouldHandleError() {
        CompletableResource resource = resource(CompletableResource.class);
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
