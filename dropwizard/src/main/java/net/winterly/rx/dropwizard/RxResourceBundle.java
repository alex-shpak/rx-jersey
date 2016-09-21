package net.winterly.rx.dropwizard;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.rx.jersey.client.RxJerseyClientFeature;
import net.winterly.rx.jersey.server.RxJerseyServerFeature;


public class RxResourceBundle implements Bundle {

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(RxJerseyServerFeature.class);
        environment.jersey().register(RxJerseyClientFeature.class);
    }

}
