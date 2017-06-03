package net.winterly.rxjersey.dropwizard;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.rxjersey.client.RxJerseyClientFeature;
import net.winterly.rxjersey.server.ObservableRequestInterceptor;
import net.winterly.rxjersey.server.RxJerseyServerFeature;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;

import javax.ws.rs.client.Client;
import java.util.function.Function;


public class RxJerseyBundle implements ConfiguredBundle<Configuration> {

    private final RxJerseyServerFeature rxJerseyServerFeature = new RxJerseyServerFeature();
    private final RxJerseyClientFeature rxJerseyClientFeature = new RxJerseyClientFeature();

    private Function<Configuration, JerseyClientConfiguration> clientConfigurationProvider;

    public RxJerseyBundle() {
        setClientConfigurationProvider(configuration -> {
            int cores = Runtime.getRuntime().availableProcessors();
            JerseyClientConfiguration clientConfiguration = new JerseyClientConfiguration();
            clientConfiguration.setMaxThreads(cores);

            return clientConfiguration;
        });
    }

    public RxJerseyBundle setClientConfigurationProvider(Function<Configuration, JerseyClientConfiguration> provider) {
        clientConfigurationProvider = provider;
        return this;
    }

    public RxJerseyBundle register(Class<? extends ObservableRequestInterceptor<?>> interceptor) {
        rxJerseyServerFeature.register(interceptor);
        return this;
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        JerseyEnvironment jersey = environment.jersey();

        JerseyClientConfiguration clientConfiguration = clientConfigurationProvider.apply(configuration);
        Client client = getClient(environment, clientConfiguration);

        rxJerseyClientFeature.register(client);

        jersey.register(rxJerseyServerFeature);
        jersey.register(rxJerseyClientFeature);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    private Client getClient(Environment environment, JerseyClientConfiguration jerseyClientConfiguration) {
        return new JerseyClientBuilder(environment)
                .using(jerseyClientConfiguration)
                .using(new GrizzlyConnectorProvider())
                .buildRx("rxJerseyClient", RxObservableInvoker.class);
    }
}
