package net.winterly.rx.dropwizard;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.rx.jersey.client.RxJerseyClientFeature;
import net.winterly.rx.jersey.server.ObservableRequestInterceptor;
import net.winterly.rx.jersey.server.RxJerseyServerFeature;

import javax.ws.rs.client.Client;


public class RxResourceBundle implements Bundle {

    private final RxJerseyServerFeature rxJerseyServerFeature = new RxJerseyServerFeature();
    private final RxJerseyClientFeature rxJerseyClientFeature = new RxJerseyClientFeature();

    public RxResourceBundle register(Client client) {
        this.rxJerseyClientFeature.register(client);
        return this;
    }

    public RxResourceBundle register(Class<? extends ObservableRequestInterceptor<?>> interceptor) {
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
