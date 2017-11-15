import org.junit.Test;
import rx.Completable;

import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class CompletableResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(CompletableResource.class);
    }

    @Test
    public void shouldReturnNoContentOnNullEntity() {
        final int status = target("completable").path("empty")
                .request()
                .get()
                .getStatus();

        assertEquals(204, status);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowOnNullObservable() {
        target("completable").path("npe")
                .request()
                .get(String.class);
    }

    @Path("/completable")
    public static class CompletableResource {

        @GET
        @Path("empty")
        public Completable empty() {
            return Completable.complete();
        }

        @GET
        @Path("npe")
        public Completable npe() {
            return null;
        }

    }
}
