package net.winterly.rxjersey.example;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RxJerseyConfiguration extends Configuration {

    @Valid
    @NotNull
    public JerseyClientConfiguration client = new JerseyClientConfiguration();

}
