import io.reactivex.Flowable;
import net.winterly.rxjersey.client.inject.Remote;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;

public class InjectionTest extends RxJerseyTest {

    @Override
    protected void configure(ResourceConfig resourceConfig) {
        resourceConfig.register(Resource.class);
    }

    @Test
    public void shouldReturnContent() {
        ResourceAPI resource = target(ResourceAPI.class);
        String message = resource.inject("hello").blockingFirst();

        assertEquals("hello", message);
    }

    @Path("/injection")
    public interface ResourceAPI {

        @GET
        @Path("echo")
        Flowable<String> echo(@QueryParam("message") String message);

        @GET
        @Path("inject")
        Flowable<String> inject(@QueryParam("message") String message);

    }

    @Path("/injection")
    public static class Resource {

        @Remote
        private ResourceAPI remote;

        @Remote
        private WebTarget webTarget;

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
