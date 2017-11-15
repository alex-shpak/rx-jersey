import net.winterly.rxjersey.server.rxjava2.RxJerseyServerFeature;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

public class RxJerseyTest extends JerseyTest {

    protected ResourceConfig config() {
        return new ResourceConfig()
                .register(JacksonFeature.class)
                .register(RxJerseyServerFeature.class);
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(JacksonFeature.class);
    }
}
