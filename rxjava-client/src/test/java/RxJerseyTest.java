import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import net.winterly.rxjersey.client.WebResourceFactory;
import net.winterly.rxjersey.client.rxjava.ObservableClientMethodInvoker;
import net.winterly.rxjersey.client.rxjava.RxJerseyClientFeature;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.RxWebTarget;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;

public class RxJerseyTest extends JerseyTest {

    @Inject
    private Provider<RxClient> clientProvider;

    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new ResourceConfig()
                .register(LocatorFeature.class)
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

    }

    @Override
    protected void configureClient(ClientConfig config) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JacksonJaxbJsonProvider jacksonProvider = new JacksonJaxbJsonProvider();
        jacksonProvider.setMapper(objectMapper);
        config.register(jacksonProvider);
    }

    protected <T> T resource(Class<T> resource) {
        return WebResourceFactory.newResource(resource, remote(), new ObservableClientMethodInvoker());
    }

    protected RxWebTarget remote() {
        return clientProvider.get().target(getBaseUri());
    }

    public static class LocatorFeature implements Feature {

        @Inject
        private ServiceLocator serviceLocator;

        @Inject
        private JerseyTest jerseyTest;

        @Override
        public boolean configure(FeatureContext context) {
            serviceLocator.inject(jerseyTest);
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
    }

    public static class Entity {

        @JsonProperty
        public String message;

        public Entity() {
        }

        public Entity(String message) {
            this.message = message;
        }
    }
}
