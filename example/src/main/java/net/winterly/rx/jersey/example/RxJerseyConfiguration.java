package net.winterly.rx.jersey.example;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RxJerseyConfiguration extends Configuration {

    @Valid
    @NotNull
    public JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

}
