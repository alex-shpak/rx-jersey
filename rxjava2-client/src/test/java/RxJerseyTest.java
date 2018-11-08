import com.fasterxml.jackson.annotation.JsonProperty;
import net.winterly.rxjersey.client.WebResourceFactory;
import net.winterly.rxjersey.client.rxjava2.FlowableClientMethodInvoker;
import net.winterly.rxjersey.client.rxjava2.RxJerseyClientFeature;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;

public class RxJerseyTest extends JerseyTest {

    @Inject
    private Provider<Client> clientProvider;

    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new ResourceConfig()
                .register(InjectTestFeature.class)
                .register(JacksonFeature.class)
                .register(RxJerseyClientFeature.class)
                .register(ServerResource.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(RxJerseyTest.this).to(JerseyTest.class);
                    }
                });

        configure(resourceConfig);

        return resourceConfig;
    }

    protected void configure(ResourceConfig resourceConfig) {
        //do nothing
    }

    protected <T> T target(Class<T> resource) {
        return WebResourceFactory.newResource(resource, target(), new FlowableClientMethodInvoker());
    }

    @Override
    protected Client getClient() {
        return clientProvider.get();
    }

    public static class InjectTestFeature implements Feature {

        @Inject
        private InjectionManager injectionManager;

        @Inject
        private JerseyTest jerseyTest;

        @Override
        public boolean configure(FeatureContext context) {
            injectionManager.inject(jerseyTest);
            return true;
        }
    }

    @Path("/endpoint")
    public static class ServerResource {

        @GET
        @Path("json")
        @Produces(MediaType.APPLICATION_JSON)
        public Entity json(@QueryParam("message") String message) {
            return new Entity(message);
        }

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

        @Path("subresource/{id}")
        public ServerSubResource subResource(@PathParam("id") String id) {
            return new ServerSubResource(id);
        }
    }

    public static class ServerSubResource {

        private final String id;

        public ServerSubResource(String id) {
            this.id = id;
        }

        @GET
        public String getId() {
            return id;
        }

        @GET
        @Path("attribute")
        public String attribute() {
            return "attribute";
        }

    }

    public static class Entity {

        public final String message;

        public Entity(@JsonProperty("message") String message) {
            this.message = message;
        }
    }
}
