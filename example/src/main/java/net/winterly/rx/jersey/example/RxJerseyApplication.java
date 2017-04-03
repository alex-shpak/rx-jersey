package net.winterly.rx.jersey.example;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import net.winterly.rx.jersey.client.RxJerseyClientFeature;
import net.winterly.rx.jersey.server.CompletableRequestInterceptor;
import net.winterly.rx.jersey.server.RxJerseyServerFeature;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class RxJerseyApplication extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new RxJerseyApplication().run(args);
    }

    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.jersey().register(RxJerseyServerFeature.class);
        environment.jersey().register(RxJerseyClientFeature.class);

        environment.jersey().register(ReactiveResource.class);
        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(ReactiveInterceptor.class)
                        .to(CompletableRequestInterceptor.class)
                        .in(Singleton.class);
            }
        });
    }
}
