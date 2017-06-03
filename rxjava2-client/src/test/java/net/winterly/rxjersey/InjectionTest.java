package net.winterly.rxjersey;

import io.reactivex.Flowable;
import net.winterly.rxjersey.client.inject.Remote;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class InjectionTest extends RxJerseyTest {

    @Override
    protected Application configure() {
        return config().register(InjectionServerResource.class);
    }

    @Test
    public void shouldReturnContent() {
        InjectionResource resource = resource(InjectionResource.class);
        String message = resource.inject("hello").blockingFirst();

        assertEquals("hello", message);
    }

    @Path("/observable")
    public interface InjectionResource {

        @GET
        @Path("echo")
        Flowable<String> echo(@QueryParam("message") String message);

        @GET
        @Path("inject")
        Flowable<String> inject(@QueryParam("message") String message);

    }

    @Path("/observable")
    public static class InjectionServerResource {

        @Remote
        private InjectionResource remote;

        @GET
        @Path("echo")
        public String echo(@QueryParam("message") String message) {
            return message;
        }

        @GET
        @Path("inject")
        public String inject(@QueryParam("message") String message) {
            return remote.echo(message).blockingFirst();
        }

    }
}
