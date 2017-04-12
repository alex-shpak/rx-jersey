package net.winterly.rxjersey.dropwizard;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.rxjersey.client.RxJerseyClientFeature;
import net.winterly.rxjersey.server.ObservableRequestInterceptor;
import net.winterly.rxjersey.server.RxJerseyServerFeature;

import javax.ws.rs.client.Client;


public class RxJerseyBundle implements Bundle {

    private final RxJerseyServerFeature rxJerseyServerFeature = new RxJerseyServerFeature();
    private final RxJerseyClientFeature rxJerseyClientFeature = new RxJerseyClientFeature();

    public RxJerseyBundle register(Client client) {
        this.rxJerseyClientFeature.register(client);
        return this;
    }

    public RxJerseyBundle register(Class<? extends ObservableRequestInterceptor<?>> interceptor) {
        rxJerseyServerFeature.register(interceptor);
        return this;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(rxJerseyServerFeature);
        environment.jersey().register(rxJerseyClientFeature);
    }

}
