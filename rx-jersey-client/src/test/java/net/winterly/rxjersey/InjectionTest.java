package net.winterly.rxjersey;

import net.winterly.rxjersey.client.WebResourceFactoryPatched;
import net.winterly.rxjersey.client.inject.Remote;
import org.junit.Test;
import rx.Observable;

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
        InjectionResource resource = WebResourceFactoryPatched.newResource(InjectionResource.class, remote());
        String message = resource.inject("hello").toBlocking().first();

        assertEquals("hello", message);
    }

    @Path("/observable")
    public interface InjectionResource {

        @GET
        @Path("echo")
        Observable<String> echo(@QueryParam("message") String message);

        @GET
        @Path("inject")
        Observable<String> inject(@QueryParam("message") String message);

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
            return remote.echo(message).toBlocking().first();
        }

    }
}
