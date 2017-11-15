import io.reactivex.Completable;
import org.junit.Test;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class CompletableResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(CompletableResource.class);
    }

    @Test
    public void shouldReturnNoContent() {
        final int status = target("completable").path("complete")
                .request()
                .get()
                .getStatus();

        assertEquals(204, status);
    }

    @Test(expected = BadRequestException.class)
    public void shouldThrowOnErrorObservable() {
        target("completable").path("error")
                .request()
                .get(String.class);
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
        @Path("complete")
        public Completable complete(@QueryParam("message") String message) {
            return Completable.complete();
        }

        @GET
        @Path("error")
        public Completable error() {
            return Completable.error(new BadRequestException());
        }

        @GET
        @Path("npe")
        public Completable npe() {
            return null;
        }

    }
}
