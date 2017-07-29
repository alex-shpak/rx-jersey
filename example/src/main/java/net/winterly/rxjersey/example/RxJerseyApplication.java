package net.winterly.rxjersey.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.rxjersey.dropwizard.RxJerseyBundle;

public class RxJerseyApplication extends Application<RxJerseyConfiguration> {

    public static void main(String[] args) throws Exception {
        new RxJerseyApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<RxJerseyConfiguration> bootstrap) {
        bootstrap.getObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        bootstrap.addBundle(new RxJerseyBundle<RxJerseyConfiguration>()
                .setClientConfigurationProvider(config -> config.client)
                .register(HeaderInterceptor.class)
        );
    }

    public void run(RxJerseyConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(GithubResourceImpl.class);
    }
}
