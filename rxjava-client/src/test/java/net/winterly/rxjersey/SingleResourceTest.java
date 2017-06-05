package net.winterly.rxjersey;

import org.junit.Test;
import rx.Single;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class SingleResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(ServerResource.class);
    }

    @Test
    public void shouldReturnContent() {
        ObservableResource resource = resource(ObservableResource.class);
        String message = resource.echo("hello").toBlocking().value();

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        ObservableResource resource = resource(ObservableResource.class);
        String message = resource.empty().toBlocking().value();

        assertEquals("", message);
    }

    @Test(expected = ResponseProcessingException.class)
    public void shouldHandleError() {
        ObservableResource resource = resource(ObservableResource.class);
        String message = resource.error().toBlocking().value();

        assertEquals("", message);
    }

    @Path("/single")
    public interface ObservableResource {

        @GET
        @Path("echo")
        Single<String> echo(@QueryParam("message") String message);

        @GET
        @Path("empty")
        Single<String> empty();

        @GET
        @Path("error")
        Single<String> error();

    }

    @Path("/single")
    public static class ServerResource {

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
