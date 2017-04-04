package net.winterly.rx.jersey.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.rx.jersey.client.RxJerseyClientFeature;
import net.winterly.rx.jersey.server.ObservableRequestInterceptor;
import net.winterly.rx.jersey.server.RxJerseyServerFeature;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class RxJerseyApplication extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new RxJerseyApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {

        bootstrap.getObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.jersey().register(RxJerseyServerFeature.class);
        environment.jersey().register(RxJerseyClientFeature.class);

        environment.jersey().register(GithubResource.class);
        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(HeaderInterceptor.class)
                        .to(ObservableRequestInterceptor.class)
                        .in(Singleton.class);
            }
        });
    }
}
