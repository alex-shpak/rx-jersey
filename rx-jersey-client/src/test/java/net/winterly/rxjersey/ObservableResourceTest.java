package net.winterly.rxjersey;

import net.winterly.rxjersey.client.WebResourceFactoryPatched;
import org.junit.Test;
import rx.Observable;

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
        ObservableResource resource = WebResourceFactoryPatched.newResource(ObservableResource.class, remote());
        String message = resource.echo("hello").toBlocking().first();

        assertEquals("hello", message);
    }

    @Test
    public void shouldReturnNoContentOnNull() {
        ObservableResource resource = WebResourceFactoryPatched.newResource(ObservableResource.class, remote());
        String message = resource.empty().toBlocking().first();

        assertEquals("", message);
    }

    @Test(expected = ResponseProcessingException.class)
    public void shouldHandleError() {
        ObservableResource resource = WebResourceFactoryPatched.newResource(ObservableResource.class, remote());
        String message = resource.error().toBlocking().first();

        assertEquals("", message);
    }

    @Path("/observable")
    public interface ObservableResource {

        @GET
        @Path("echo")
        Observable<String> echo(@QueryParam("message") String message);

        @GET
        @Path("empty")
        Observable<String> empty();

        @GET
        @Path("error")
        Observable<String> error();

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
        public Observable<String> empty() {
            return null;
        }

        @GET
        @Path("error")
        public Observable<String> error() {
            throw new BadRequestException();
        }

    }
}
