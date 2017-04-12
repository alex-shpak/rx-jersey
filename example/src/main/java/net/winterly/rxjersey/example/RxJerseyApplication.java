package net.winterly.rxjersey.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.rxjersey.client.RxJerseyClientFeature;
import net.winterly.rxjersey.server.RxJerseyServerFeature;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;

import javax.ws.rs.client.Client;

public class RxJerseyApplication extends Application<RxJerseyConfiguration> {

    public static void main(String[] args) throws Exception {
        new RxJerseyApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<RxJerseyConfiguration> bootstrap) {
        bootstrap.getObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public void run(RxJerseyConfiguration configuration, Environment environment) throws Exception {
        JerseyEnvironment jersey = environment.jersey();

        Client client = new JerseyClientBuilder(environment)
                .using(configuration.client)
                .using(new GrizzlyConnectorProvider())
                .buildRx(RxJerseyClientFeature.RX_JERSEY_CLIENT_NAME, RxObservableInvoker.class);

        RxJerseyServerFeature rxJerseyServerFeature = new RxJerseyServerFeature()
                .register(HeaderInterceptor.class);

        RxJerseyClientFeature rxJerseyClientFeature = new RxJerseyClientFeature()
                .register(client);

        jersey.register(rxJerseyServerFeature);
        jersey.register(rxJerseyClientFeature);
        jersey.register(GithubResource.class);
    }

}
