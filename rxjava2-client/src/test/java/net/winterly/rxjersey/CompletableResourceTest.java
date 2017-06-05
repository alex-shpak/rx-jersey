package net.winterly.rxjersey;

import io.reactivex.Completable;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class CompletableResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(ObservableServerResource.class);
    }

    @Test
    public void shouldReturnContent() {
        ObservableResource resource = resource(ObservableResource.class);
        boolean completed = resource.echo("hello").blockingAwait(5, TimeUnit.SECONDS);

        assertEquals(completed, true);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        ObservableResource resource = resource(ObservableResource.class);
        boolean completed = resource.empty().blockingAwait(5, TimeUnit.SECONDS);

        assertEquals(completed, true);
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleError() {
        ObservableResource resource = resource(ObservableResource.class);
        boolean completed = resource.error().blockingAwait(5, TimeUnit.SECONDS);

        assertEquals(completed, true);
    }

    @Path("/completable")
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

    @Path("/completable")
    public static class ObservableServerResource {

        @GET
        @Path("echo")
        public String echo(@QueryParam("message") String message) {
            return message;
        }

        @GET
        @Path("empty")
        public String empty() {
            return null;
        }

        @GET
        @Path("error")
        public String error() {
            throw new BadRequestException();
        }

    }
}
