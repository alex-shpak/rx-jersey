package net.winterly.dropwizard.rx;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.rx.jersey.RxJerseyFeature;

public class RxResourceBundle implements Bundle {

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(RxJerseyFeature.class);
    }

}
