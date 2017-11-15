import io.reactivex.Flowable;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class FlowableResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(FlowableResource.class);
    }

    @Test
    public void shouldReturnContent() {
        final String message = target("flowable").path("echo")
                .queryParam("message", "hello")
                .request()
                .get(String.class);

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnEmptyObservable() {
        final int status = target("flowable").path("empty")
                .request()
                .get()
                .getStatus();

        assertEquals(204, status);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowOnNullObservable() {
        target("flowable").path("npe")
                .request()
                .get(String.class);
    }

    @Test(expected = InternalServerErrorException.class)
    public void shouldThrowOnMultipleEntities() {
        target("flowable").path("multiple")
                .request()
                .get(String.class);
    }


    @Path("/flowable")
    public static class FlowableResource {

        @GET
        @Path("echo")
        public Flowable<String> echo(@QueryParam("message") String message) {
            return Flowable.just(message);
        }

        @GET
        @Path("empty")
        public Flowable<String> empty() {
            return Flowable.empty();
        }

        @GET
        @Path("npe")
        public Flowable<String> npe() {
            return null;
        }

        @GET
        @Path("multiple")
        public Flowable<String> multiple() {
            return Flowable.just("hello", "rx");
        }

    }
}
