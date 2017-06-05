package net.winterly.rxjersey;

import org.junit.Test;
import rx.Completable;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.Application;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class CompletableResourceTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(ServerResource.class);
    }

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

    @Path("/completable")
    public interface CompletableResource {

        @GET
        @Path("empty")
        Completable empty();

        @GET
        @Path("error")
        Completable error();

    }

    @Path("/completable")
    public static class ServerResource {

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
