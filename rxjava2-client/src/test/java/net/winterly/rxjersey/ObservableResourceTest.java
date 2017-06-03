package net.winterly.rxjersey;

import io.reactivex.Flowable;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class ObservableResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(ObservableServerResource.class);
    }

    @Test
    public void shouldReturnContent() {
        ObservableResource resource = resource(ObservableResource.class);
        String message = resource.echo("hello").blockingFirst();

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        ObservableResource resource = resource(ObservableResource.class);
        String message = resource.empty().blockingFirst();

        assertEquals("", message);
    }

    @Test(expected = ResponseProcessingException.class)
    public void shouldHandleError() {
        ObservableResource resource = resource(ObservableResource.class);
        String message = resource.error().blockingFirst();

        assertEquals("", message);
    }

    @Path("/observable")
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

    @Path("/observable")
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
