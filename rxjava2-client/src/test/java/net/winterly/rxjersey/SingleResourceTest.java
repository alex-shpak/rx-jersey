package net.winterly.rxjersey;

import io.reactivex.Single;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class SingleResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(ServerResource.class);
    }

    @Test
    public void shouldReturnContent() {
        Resource resource = resource(Resource.class);
        String message = resource.echo("hello").blockingGet();

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        Resource resource = resource(Resource.class);
        String message = resource.empty().blockingGet();

        assertEquals("", message);
    }

    @Test(expected = BadRequestException.class)
    public void shouldHandleError() {
        Resource resource = resource(Resource.class);
        String message = resource.error().blockingGet();

        assertEquals("", message);
    }

    @Path("/single")
    public interface Resource {

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
