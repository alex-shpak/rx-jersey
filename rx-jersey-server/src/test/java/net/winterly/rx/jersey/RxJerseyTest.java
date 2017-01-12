package net.winterly.rx.jersey;

import net.winterly.rx.jersey.server.RxJerseyServerFeature;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

public class RxJerseyTest extends JerseyTest {

    protected ResourceConfig config() {
        return new ResourceConfig()
                .register(RxJerseyServerFeature.class);
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(JacksonFeature.class);
    }
}
