package net.winterly.rxjersey.dropwizard;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.rxjersey.client.rxjava2.RxJerseyClientFeature;
import net.winterly.rxjersey.server.rxjava2.CompletableRequestInterceptor;
import net.winterly.rxjersey.server.rxjava2.RxJerseyServerFeature;
import org.glassfish.jersey.client.rx.rxjava2.RxFlowableInvokerProvider;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;

import javax.ws.rs.client.Client;
import java.util.function.Function;


public class RxJerseyBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private final RxJerseyServerFeature rxJerseyServerFeature = new RxJerseyServerFeature();
    private final RxJerseyClientFeature rxJerseyClientFeature = new RxJerseyClientFeature();

    private Function<T, JerseyClientConfiguration> clientConfigurationProvider;

    public RxJerseyBundle() {
        setClientConfigurationProvider(configuration -> {
            int cores = Runtime.getRuntime().availableProcessors();
            JerseyClientConfiguration clientConfiguration = new JerseyClientConfiguration();
            clientConfiguration.setMaxThreads(cores);

            return clientConfiguration;
        });
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        JerseyEnvironment jersey = environment.jersey();

        JerseyClientConfiguration clientConfiguration = clientConfigurationProvider.apply(configuration);
        Client client = getClient(environment, clientConfiguration);

        rxJerseyClientFeature.setClient(client);

        jersey.register(rxJerseyServerFeature);
        jersey.register(rxJerseyClientFeature);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    public RxJerseyClientFeature client() {
        return rxJerseyClientFeature;
    }

    public RxJerseyServerFeature server() {
        return rxJerseyServerFeature;
    }

    public RxJerseyBundle<T> setClientConfigurationProvider(Function<T, JerseyClientConfiguration> provider) {
        clientConfigurationProvider = provider;
        return this;
    }

    public RxJerseyBundle<T> register(Class<? extends CompletableRequestInterceptor> interceptor) {
        rxJerseyServerFeature.register(interceptor);
        return this;
    }

    private Client getClient(Environment environment, JerseyClientConfiguration jerseyClientConfiguration) {
        return new JerseyClientBuilder(environment)
                .using(jerseyClientConfiguration)
                .using(new GrizzlyConnectorProvider())
                .buildRx("rxJerseyClient", RxFlowableInvokerProvider.class);
    }
}
